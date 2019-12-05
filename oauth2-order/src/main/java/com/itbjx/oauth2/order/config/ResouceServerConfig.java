package com.itbjx.oauth2.order.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableResourceServer
public class ResouceServerConfig extends ResourceServerConfigurerAdapter {

			//资源ID，必须与客户端配置的资源列表（resourceIds）一致
			public static final String RESOURCE_ID = "res1";

			@Autowired
			TokenStore tokenStore;
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.resourceId(RESOURCE_ID)   //资源ID
			.tokenStore(tokenStore)  //本地校验jwt令牌
			//	.tokenServices(tokenService())  //验证令牌的服务(远程校验)
				.stateless(true);
	}
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests()
				.antMatchers("/**").access("#oauth2.hasScope('ROLE_ADMIN')")
				.and().csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	//资源服务，远程令牌解析服务
	/*@Bean
	public ResourceServerTokenServices tokenService() {
//使用远程服务请求授权服务器校验token,必须指定校验token 的url、client_id，client_secret
		RemoteTokenServices service=new RemoteTokenServices();
		//当客户端携带令牌来请求服务时，资源服务器就会通过下面的路径验证令牌
		service.setCheckTokenEndpointUrl("http://localhost:53020/uaa/oauth/check_token");
		service.setClientId("c1");
		service.setClientSecret("secret");
		return service;
	}*/
}
