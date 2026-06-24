package kr.co.prac.admin.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.prac.global.session.SessionUtil;
import kr.co.prac.orders.dto.OrderResponse;
import kr.co.prac.orders.repository.OrdersRepository;
import kr.co.prac.orders.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping("/admin/orders")
    public List<OrderResponse> ordersList(HttpServletRequest httpServletRequest) {
        SessionUtil.requireAdmin(httpServletRequest);
        return orderService.findAll();
    }
}
