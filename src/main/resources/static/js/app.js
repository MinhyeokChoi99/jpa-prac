const app = document.getElementById("app");

let currentUser = null;
let currentView = "products";

const ENDPOINTS = {
  login: "/members/login",
  logout: "/members/logout",
  join: "/members",
  me: "/members/me",
  products: "/products",
  orders: "/orders",
  myOrders: "/members/me/orders",
  adminProducts: "/admin/products",
  adminOrders: "/admin/orders",
  adminMembers: "/admin/members",
};

document.addEventListener("DOMContentLoaded", restoreSession);

async function restoreSession() {
  try {
    currentUser = await api(ENDPOINTS.me);
    currentView = currentUser.role === "ADMIN" ? "admin-products" : "products";
    renderApp();
  } catch (error) {
    currentUser = null;
    renderLogin();
  }
}

async function api(url, options = {}) {
  const config = {
    method: options.method || "GET",
    credentials: "include",
    headers: options.headers ? { ...options.headers } : {},
  };

  if (options.body !== undefined) {
    config.headers["Content-Type"] = "application/json";
    config.body = JSON.stringify(options.body);
  }

  const response = await fetch(url, config);

  if (response.status === 204 || response.headers.get("content-length") === "0") {
    if (!response.ok) {
      throw { status: response.status, message: "요청에 실패했습니다." };
    }
    return null;
  }

  const text = await response.text();
  let data = null;

  if (text) {
    try {
      data = JSON.parse(text);
    } catch {
      data = text;
    }
  }

  if (!response.ok) {
    const message =
      data && typeof data === "object" && data.message
        ? data.message
        : `요청 실패: ${response.status}`;
    throw {
      status: response.status,
      code: data && data.code,
      message,
      data,
    };
  }

  return data;
}

function renderLogin() {
  app.innerHTML = `
    <main class="login-page">
      <section class="auth-grid">
        <form id="loginForm" class="card auth-card">
          <h2>로그인</h2>
          <label>
            이메일
            <input name="email" type="email" placeholder="admin@test.com" required />
          </label>
          <label>
            비밀번호
            <input name="password" type="password" placeholder="password" required />
          </label>
          <button class="primary-btn" type="submit">로그인</button>
          <p class="hint">로그인 성공 후 role 값에 따라 USER/ADMIN 화면이 분기됩니다.</p>
        </form>

        <form id="joinForm" class="card auth-card">
          <h2>회원가입</h2>
          <label>
            이름
            <input name="name" type="text" placeholder="Ella" required />
          </label>
          <label>
            이메일
            <input name="email" type="email" placeholder="ella@test.com" required />
          </label>
          <label>
            비밀번호
            <input name="password" type="password" placeholder="password" required />
          </label>
          <button class="secondary-btn" type="submit">회원가입</button>
          <p class="hint">회원가입 계정은 기본 USER 권한으로 생성됩니다.</p>
        </form>
      </section>
    </main>
  `;

  document.getElementById("loginForm").addEventListener("submit", handleLogin);
  document.getElementById("joinForm").addEventListener("submit", handleJoin);
}

async function handleLogin(event) {
  event.preventDefault();
  const form = event.currentTarget;
  const body = readForm(form);

  try {
    await api(ENDPOINTS.login, {
      method: "POST",
      body,
    });

    currentUser = await api(ENDPOINTS.me);
    currentView = currentUser.role === "ADMIN" ? "admin-products" : "products";
    renderApp();
    toast("로그인되었습니다.");
  } catch (error) {
    toast(error.message || "로그인에 실패했습니다.", "error");
  }
}

async function handleJoin(event) {
  event.preventDefault();
  const form = event.currentTarget;
  const body = readForm(form);

  try {
    await api(ENDPOINTS.join, {
      method: "POST",
      body,
    });
    form.reset();
    toast("회원가입이 완료되었습니다. 로그인하세요.");
  } catch (error) {
    toast(error.message || "회원가입에 실패했습니다.", "error");
  }
}

function renderApp() {
  const isAdmin = currentUser?.role === "ADMIN";

  app.innerHTML = `
    <div class="shell">
      <aside class="sidebar">
        <div class="sidebar-title">
          <div class="brand-mark small">JPA</div>
          <div>
            <strong>Shop Console</strong>
            <span>${isAdmin ? "Admin Mode" : "User Mode"}</span>
          </div>
        </div>

        <nav class="menu">
          ${
            isAdmin
              ? `
                <button data-view="admin-products">상품 관리</button>
                <button data-view="admin-orders">주문 관리</button>
                <button data-view="admin-members">회원 관리</button>
              `
              : `
                <button data-view="products">상품 목록</button>
                <button data-view="my-orders">내 주문</button>
                <button data-view="profile">내 정보</button>
              `
          }
        </nav>
      </aside>

      <section class="main">
        <header class="topbar">
          <div>
            <h1 id="pageTitle"></h1>
            <p id="pageDescription"></p>
          </div>
          <div class="user-box">
            <div class="user-meta">
              <strong>${escapeHtml(currentUser?.name || "-")}</strong>
              <span>${escapeHtml(currentUser?.email || "")}</span>
            </div>
            <span class="role-badge">${escapeHtml(currentUser?.role || "")}</span>
            <button id="logoutBtn" class="ghost-btn">로그아웃</button>
          </div>
        </header>

        <div id="content" class="content"></div>
      </section>
    </div>
  `;

  document.querySelectorAll(".menu button").forEach((button) => {
    button.classList.toggle("active", button.dataset.view === currentView);
    button.addEventListener("click", () => {
      currentView = button.dataset.view;
      renderApp();
    });
  });

  document.getElementById("logoutBtn").addEventListener("click", handleLogout);

  routeView();
}

async function handleLogout() {
  try {
    await api(ENDPOINTS.logout, { method: "POST" });
  } catch {
  } finally {
    currentUser = null;
    renderLogin();
    toast("로그아웃되었습니다.");
  }
}

function routeView() {
  const routes = {
    products: renderProductList,
    "my-orders": renderMyOrders,
    profile: renderProfile,
    "admin-products": renderAdminProducts,
    "admin-orders": renderAdminOrders,
    "admin-members": renderAdminMembers,
  };

  const renderer = routes[currentView] || renderProductList;
  renderer();
}

function setPage(title, description) {
  document.getElementById("pageTitle").textContent = title;
  document.getElementById("pageDescription").textContent = description || "";
}

async function renderProductList() {
  setPage("상품 목록", "ACTIVE 상태의 상품만 표시됩니다.");
  const content = document.getElementById("content");
  content.innerHTML = loading();

  try {
    const products = await api(ENDPOINTS.products);

    if (!products.length) {
      content.innerHTML = emptyState("현재 구매 가능한 상품이 없습니다.");
      return;
    }

    content.innerHTML = `
      <div class="product-grid">
        ${products.map(productCard).join("")}
      </div>
      <div id="detailPanel"></div>
    `;

    document.querySelectorAll("[data-order-product]").forEach((button) => {
      button.addEventListener("click", async () => {
        const productNumber = Number(button.dataset.orderProduct);
        const input = document.querySelector(`[data-count-for="${productNumber}"]`);
        const count = Number(input.value);

        if (!count || count < 1) {
          toast("수량은 1 이상이어야 합니다.", "error");
          return;
        }

        try {
          const order = await api(ENDPOINTS.orders, {
            method: "POST",
            body: [{ productNumber, count }],
          });

          renderOrderDetailResult(order, "주문이 생성되었습니다.");
          renderProductList();
        } catch (error) {
          toast(error.message || "주문 생성에 실패했습니다.", "error");
        }
      });
    });
  } catch (error) {
    content.innerHTML = errorState(error.message);
  }
}

function productCard(product) {
  return `
    <article class="card product-card">
      <div class="card-header">
        <h3>${escapeHtml(product.name)}</h3>
        <span class="status ${statusClass(product.productStatus)}">${escapeHtml(product.productStatus)}</span>
      </div>
      <p class="description">${escapeHtml(product.description || "설명이 없습니다.")}</p>
      <div class="product-info">
        <span>가격 <strong>${formatPrice(product.price)}</strong></span>
        <span>재고 <strong>${product.stock}</strong></span>
      </div>
      <div class="inline-form">
        <input data-count-for="${product.number}" type="number" min="1" max="${product.stock}" value="1" />
        <button class="primary-btn" data-order-product="${product.number}" ${product.stock <= 0 ? "disabled" : ""}>주문</button>
      </div>
    </article>
  `;
}

async function renderMyOrders() {
  setPage("내 주문", "내 주문 목록을 조회하고 상세 확인 또는 취소할 수 있습니다.");
  const content = document.getElementById("content");
  content.innerHTML = loading();

  try {
    const orders = await api(ENDPOINTS.myOrders);

    content.innerHTML = `
      ${orders.length ? orderTable(orders, false) : emptyState("주문 내역이 없습니다.")}
      <div id="detailPanel"></div>
    `;

    bindOrderActions(false);
  } catch (error) {
    content.innerHTML = errorState(error.message);
  }
}

function orderTable(orders, adminMode) {
  return `
    <div class="card table-card">
      <table>
        <thead>
          <tr>
            <th>주문번호</th>
            <th>회원번호</th>
            <th>주문일</th>
            <th>상태</th>
            <th>작업</th>
          </tr>
        </thead>
        <tbody>
          ${orders
            .map(
              (order) => `
              <tr>
                <td>#${order.number}</td>
                <td>${order.memberId}</td>
                <td>${formatDate(order.orderDate)}</td>
                <td><span class="status ${statusClass(order.status)}">${order.status}</span></td>
                <td class="actions">
                  <button class="ghost-btn" data-order-detail="${order.number}" data-admin="${adminMode}">상세</button>
                  ${
                    !adminMode && order.status !== "CANCEL"
                      ? `<button class="danger-btn" data-order-cancel="${order.number}">취소</button>`
                      : ""
                  }
                </td>
              </tr>
            `
            )
            .join("")}
        </tbody>
      </table>
    </div>
  `;
}

function bindOrderActions(adminMode) {
  document.querySelectorAll("[data-order-detail]").forEach((button) => {
    button.addEventListener("click", async () => {
      const orderId = button.dataset.orderDetail;
      const url = adminMode ? `${ENDPOINTS.adminOrders}/${orderId}` : `${ENDPOINTS.orders}/${orderId}`;

      try {
        const detail = await api(url);
        renderOrderDetailResult(detail);
      } catch (error) {
        toast(error.message || "주문 상세 조회에 실패했습니다.", "error");
      }
    });
  });

  document.querySelectorAll("[data-order-cancel]").forEach((button) => {
    button.addEventListener("click", async () => {
      const orderId = button.dataset.orderCancel;
      if (!confirm(`주문 #${orderId}을 취소할까요?`)) return;

      try {
        await api(`${ENDPOINTS.orders}/${orderId}/cancel`, { method: "POST" });
        toast("주문이 취소되었습니다.");
        renderMyOrders();
      } catch (error) {
        toast(error.message || "주문 취소에 실패했습니다.", "error");
      }
    });
  });
}

function renderOrderDetailResult(order, message) {
  const panel = document.getElementById("detailPanel") || document.getElementById("content");

  panel.innerHTML = `
    ${message ? `<div class="success-banner">${message}</div>` : ""}
    <section class="card detail-card">
      <div class="card-header">
        <h3>주문 상세 #${order.number}</h3>
        <span class="status ${statusClass(order.status)}">${order.status}</span>
      </div>
      <div class="detail-grid">
        <span>회원번호</span><strong>${order.memberId}</strong>
        <span>주문일</span><strong>${formatDate(order.orderDate)}</strong>
        <span>총 금액</span><strong>${formatPrice(order.totalPrice)}</strong>
      </div>
      <table class="nested-table">
        <thead>
          <tr>
            <th>상품명</th>
            <th>단가</th>
            <th>수량</th>
            <th>합계</th>
          </tr>
        </thead>
        <tbody>
          ${order.orderItems
            .map(
              (item) => `
              <tr>
                <td>${escapeHtml(item.productName)}</td>
                <td>${formatPrice(item.unitPrice)}</td>
                <td>${item.count}</td>
                <td>${formatPrice(item.totalPrice)}</td>
              </tr>
            `
            )
            .join("")}
        </tbody>
      </table>
    </section>
  `;
}

async function renderProfile() {
  setPage("내 정보", "회원 정보를 조회, 수정, 탈퇴할 수 있습니다.");
  const content = document.getElementById("content");
  content.innerHTML = loading();

  try {
    const me = await api(ENDPOINTS.me);

    content.innerHTML = `
      <form id="profileForm" class="card form-card">
        <h2>내 정보 수정</h2>
        <label>
          이름
          <input name="name" value="${escapeAttribute(me.name)}" />
        </label>
        <label>
          이메일
          <input name="email" type="email" value="${escapeAttribute(me.email)}" />
        </label>
        <div class="form-actions">
          <button class="primary-btn" type="submit">수정 저장</button>
          <button class="danger-btn" type="button" id="deleteMeBtn">회원 탈퇴</button>
        </div>
      </form>
    `;

    document.getElementById("profileForm").addEventListener("submit", async (event) => {
      event.preventDefault();
      try {
        const updated = await api(ENDPOINTS.me, {
          method: "PUT",
          body: readForm(event.currentTarget),
        });
        currentUser = updated;
        toast("회원 정보가 수정되었습니다.");
        renderApp();
      } catch (error) {
        toast(error.message || "회원 정보 수정에 실패했습니다.", "error");
      }
    });

    document.getElementById("deleteMeBtn").addEventListener("click", async () => {
      if (!confirm("정말 탈퇴할까요?")) return;

      try {
        await api(ENDPOINTS.me, { method: "DELETE" });
        currentUser = null;
        renderLogin();
        toast("회원 탈퇴가 완료되었습니다.");
      } catch (error) {
        toast(error.message || "회원 탈퇴에 실패했습니다.", "error");
      }
    });
  } catch (error) {
    content.innerHTML = errorState(error.message);
  }
}

async function renderAdminProducts() {
  setPage("관리자 상품 관리", "전체 상품을 조회하고 생성, 수정, 숨김, 재노출, 삭제 처리할 수 있습니다.");
  const content = document.getElementById("content");
  content.innerHTML = loading();

  try {
    const products = await api(ENDPOINTS.adminProducts);

    content.innerHTML = `
      ${adminProductForm()}
      ${
        products.length
          ? `
            <div class="card table-card">
              <table>
                <thead>
                  <tr>
                    <th>번호</th>
                    <th>상품명</th>
                    <th>가격</th>
                    <th>재고</th>
                    <th>상태</th>
                    <th>설명</th>
                    <th>작업</th>
                  </tr>
                </thead>
                <tbody>
                  ${products.map(adminProductRow).join("")}
                </tbody>
              </table>
            </div>
          `
          : emptyState("등록된 상품이 없습니다.")
      }
    `;

    bindAdminProductActions(products);
  } catch (error) {
    content.innerHTML = errorState(error.message);
  }
}

function adminProductForm(product = null) {
  const isEdit = Boolean(product);

  return `
    <form id="adminProductForm" class="card form-card">
      <h2>${isEdit ? "상품 수정" : "상품 생성"}</h2>
      <input type="hidden" name="number" value="${product?.number || ""}" />
      <div class="form-grid">
        <label>
          상품명
          <input name="name" value="${escapeAttribute(product?.name || "")}" required />
        </label>
        <label>
          가격
          <input name="price" type="number" min="0" value="${product?.price ?? ""}" required />
        </label>
        <label>
          재고
          <input name="stock" type="number" min="0" value="${product?.stock ?? ""}" required />
        </label>
        <label class="full">
          설명
          <textarea name="description" maxlength="1000">${escapeHtml(product?.description || "")}</textarea>
        </label>
      </div>
      <div class="form-actions">
        <button class="primary-btn" type="submit">${isEdit ? "수정 저장" : "상품 생성"}</button>
        ${isEdit ? `<button class="secondary-btn" type="button" id="cancelEditProduct">수정 취소</button>` : ""}
      </div>
    </form>
  `;
}

function adminProductRow(product) {
  return `
    <tr>
      <td>#${product.number}</td>
      <td>${escapeHtml(product.name)}</td>
      <td>${formatPrice(product.price)}</td>
      <td>${product.stock}</td>
      <td><span class="status ${statusClass(product.productStatus)}">${product.productStatus}</span></td>
      <td class="truncate">${escapeHtml(product.description || "")}</td>
      <td class="actions">
        <button class="ghost-btn" data-edit-product="${product.number}">수정</button>
        <button class="secondary-btn" data-hide-product="${product.number}" ${product.productStatus !== "ACTIVE" ? "disabled" : ""}>숨김</button>
        <button class="secondary-btn" data-show-product="${product.number}" ${product.productStatus === "ACTIVE" ? "disabled" : ""}>재노출</button>
        <button class="danger-btn" data-delete-product="${product.number}" ${product.productStatus === "DELETED" ? "disabled" : ""}>삭제</button>
      </td>
    </tr>
  `;
}

function bindAdminProductActions(products) {
  const form = document.getElementById("adminProductForm");

  form.addEventListener("submit", async (event) => {
    event.preventDefault();

    const data = readForm(form);
    const productNumber = data.number;
    const body = {
      name: data.name,
      price: Number(data.price),
      stock: Number(data.stock),
      description: data.description,
    };

    try {
      if (productNumber) {
        await api(`${ENDPOINTS.adminProducts}/${productNumber}`, {
          method: "PUT",
          body,
        });
        toast("상품이 수정되었습니다.");
      } else {
        await api(ENDPOINTS.adminProducts, {
          method: "POST",
          body,
        });
        toast("상품이 생성되었습니다.");
      }

      renderAdminProducts();
    } catch (error) {
      toast(error.message || "상품 저장에 실패했습니다.", "error");
    }
  });

  document.querySelectorAll("[data-edit-product]").forEach((button) => {
    button.addEventListener("click", () => {
      const product = products.find((item) => String(item.number) === button.dataset.editProduct);
      const existingForm = document.getElementById("adminProductForm");
      existingForm.outerHTML = adminProductForm(product);
      bindAdminProductActions(products);

      const cancelButton = document.getElementById("cancelEditProduct");
      if (cancelButton) {
        cancelButton.addEventListener("click", renderAdminProducts);
      }

      document.getElementById("adminProductForm").scrollIntoView({ behavior: "smooth", block: "start" });
    });
  });

  document.querySelectorAll("[data-hide-product]").forEach((button) => {
    button.addEventListener("click", () => patchProductStatus(button.dataset.hideProduct, "hide", "상품을 숨김 처리했습니다."));
  });

  document.querySelectorAll("[data-show-product]").forEach((button) => {
    button.addEventListener("click", () => patchProductStatus(button.dataset.showProduct, "show", "상품을 재노출했습니다."));
  });

  document.querySelectorAll("[data-delete-product]").forEach((button) => {
    button.addEventListener("click", async () => {
      const productNumber = button.dataset.deleteProduct;
      if (!confirm(`상품 #${productNumber}을 삭제 처리할까요?`)) return;

      try {
        await api(`${ENDPOINTS.adminProducts}/${productNumber}`, { method: "DELETE" });
        toast("상품을 삭제 처리했습니다.");
        renderAdminProducts();
      } catch (error) {
        toast(error.message || "상품 삭제에 실패했습니다.", "error");
      }
    });
  });
}

async function patchProductStatus(productNumber, action, message) {
  try {
    await api(`${ENDPOINTS.adminProducts}/${productNumber}/${action}`, {
      method: "PATCH",
    });
    toast(message);
    renderAdminProducts();
  } catch (error) {
    toast(error.message || "상품 상태 변경에 실패했습니다.", "error");
  }
}

async function renderAdminOrders() {
  setPage("관리자 주문 관리", "전체 주문을 조회하고 주문 상세를 확인합니다.");
  const content = document.getElementById("content");
  content.innerHTML = loading();

  try {
    const orders = await api(ENDPOINTS.adminOrders);

    content.innerHTML = `
      ${orders.length ? orderTable(orders, true) : emptyState("주문 내역이 없습니다.")}
      <div id="detailPanel"></div>
    `;

    bindOrderActions(true);
  } catch (error) {
    content.innerHTML = errorState(error.message);
  }
}

async function renderAdminMembers() {
  setPage("관리자 회원 관리", "전체 회원을 조회하고 회원을 삭제할 수 있습니다.");
  const content = document.getElementById("content");
  content.innerHTML = loading();

  try {
    const members = await api(ENDPOINTS.adminMembers);

    if (!members.length) {
      content.innerHTML = emptyState("등록된 회원이 없습니다.");
      return;
    }

    content.innerHTML = `
      <div class="card table-card">
        <table>
          <thead>
            <tr>
              <th>회원번호</th>
              <th>이름</th>
              <th>이메일</th>
              <th>권한</th>
              <th>작업</th>
            </tr>
          </thead>
          <tbody>
            ${members
              .map(
                (member) => `
                <tr>
                  <td>#${member.number}</td>
                  <td>${escapeHtml(member.name)}</td>
                  <td>${escapeHtml(member.email)}</td>
                  <td><span class="role-badge">${member.role}</span></td>
                  <td>
                    <button class="danger-btn" data-delete-member="${member.number}">삭제</button>
                  </td>
                </tr>
              `
              )
              .join("")}
          </tbody>
        </table>
      </div>
    `;

    document.querySelectorAll("[data-delete-member]").forEach((button) => {
      button.addEventListener("click", async () => {
        const memberNumber = button.dataset.deleteMember;
        if (!confirm(`회원 #${memberNumber}을 삭제할까요?`)) return;

        try {
          await api(`${ENDPOINTS.adminMembers}/${memberNumber}`, { method: "DELETE" });
          toast("회원이 삭제되었습니다.");
          renderAdminMembers();
        } catch (error) {
          toast(error.message || "회원 삭제에 실패했습니다.", "error");
        }
      });
    });
  } catch (error) {
    content.innerHTML = errorState(error.message);
  }
}

function readForm(form) {
  return Object.fromEntries(new FormData(form).entries());
}

function loading() {
  return `<div class="card loading">데이터를 불러오는 중입니다...</div>`;
}

function emptyState(message) {
  return `<div class="card empty-state">${escapeHtml(message)}</div>`;
}

function errorState(message) {
  return `<div class="card error-state">${escapeHtml(message || "오류가 발생했습니다.")}</div>`;
}

function toast(message, type = "success") {
  const oldToast = document.querySelector(".toast");
  if (oldToast) oldToast.remove();

  const el = document.createElement("div");
  el.className = `toast ${type}`;
  el.textContent = message;
  document.body.appendChild(el);

  window.setTimeout(() => {
    el.classList.add("hide");
    window.setTimeout(() => el.remove(), 250);
  }, 2500);
}

function formatPrice(value) {
  return `${Number(value || 0).toLocaleString("ko-KR")}원`;
}

function formatDate(value) {
  if (!value) return "-";
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return value;
  return date.toLocaleString("ko-KR");
}

function escapeHtml(value) {
  return String(value ?? "")
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#039;");
}

function escapeAttribute(value) {
  return escapeHtml(value).replaceAll("`", "&#096;");
}

function statusClass(status) {
  const normalized = String(status || "").toLowerCase();
  if (normalized.includes("active") || normalized.includes("ready")) return "ok";
  if (normalized.includes("hidden")) return "warn";
  if (normalized.includes("deleted") || normalized.includes("cancel")) return "danger";
  return "neutral";
}
