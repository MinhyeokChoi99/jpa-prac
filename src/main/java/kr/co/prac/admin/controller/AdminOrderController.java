package kr.co.prac.admin.controller;


import kr.co.prac.orders.dto.OrderDetailResponse;
import kr.co.prac.orders.dto.OrderResponse;
import kr.co.prac.orders.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/orders")
public class AdminOrderController {

    private final OrderService orderService;
    

    @GetMapping
    public List<OrderResponse> ordersList() {
        return orderService.findAll();
    }

    @GetMapping("/{orderId}")
    public OrderDetailResponse orderDetail(@PathVariable Long orderId) {
    	
        return orderService.findOneForAdmin(orderId);
    }
}
