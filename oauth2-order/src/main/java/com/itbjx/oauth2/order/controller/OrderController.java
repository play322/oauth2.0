package com.itbjx.oauth2.order.controller;

import com.itbjx.oauth2.order.model.UserDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {


	@GetMapping("/r1")
	@PreAuthorize("hasAuthority('p1')")
	public String r1(){
		//获取用户身份信息
		UserDTO userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return userDTO.getUsername()+"访问资源1";
	}
}
