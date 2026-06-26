package kr.co.prac.admin.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.prac.orders.dto.OrderResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/product")
public class AdminProductController {
	/**
	 * 목록은 누구나 확인가능 추가, 수정, 삭제
	 */
	
}
