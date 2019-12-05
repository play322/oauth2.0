package com.itbjx.oauth2.uaa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.itbjx.oauth2.uaa"})
public class UAAAerver {
	public static void main(String[] args) {
		SpringApplication.run(UAAAerver.class,args);
	}
}

