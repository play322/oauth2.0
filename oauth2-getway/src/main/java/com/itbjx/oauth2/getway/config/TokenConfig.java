package com.itbjx.oauth2.getway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;


/*
*  专门做令牌管理
* */
@Configuration
public class TokenConfig {

	private String SIGNING_KEY = "uaa123";

	//令牌存储策略
	@Bean
	public TokenStore tokenStore() {
		//生成jwt令牌
		return new JwtTokenStore(accessTokenConverter());
	}
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setSigningKey(SIGNING_KEY); //对称秘钥，资源服务器使用该秘钥来验证
		return converter;
	}


/*	@Bean
	public TokenStore tokenStore() {
		//内存方式，生成普通令牌
		return new InMemoryTokenStore();
	}*/


}
