package com.itbjx.oauth2.getway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableResourceServer
public class ResouceServerConfig{

			//资源ID，必须与客户端配置的资源列表（resourceIds）一致
			public static final String RESOURCE_ID = "res1";


	//uaa资源服务配置
	@Configuration
	@EnableResourceServer
	public class UAAServerConfig extends ResourceServerConfigurerAdapter {

		@Autowired
		private TokenStore tokenStore;

		@Override
		public void configure(ResourceServerSecurityConfigurer resources){
			resources.tokenStore(tokenStore).resourceId(RESOURCE_ID)
					.stateless(true);
		}
		@Override
		public void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests()
					.antMatchers("/uaa/**").permitAll();
		}
	}

	//order资源服务配置
	@Configuration
	@EnableResourceServer
	public class OrderServerConfig extends ResourceServerConfigurerAdapter {
		@Autowired
		private TokenStore tokenStore;
		@Override
		public void configure(ResourceServerSecurityConfigurer resources) {
			resources.tokenStore(tokenStore).resourceId(RESOURCE_ID)
					.stateless(true);
		}
		@Override
		public void configure(HttpSecurity http) throws Exception {
			http
					.authorizeRequests()
					//拦截/order/**请求，并进行检验，是否含ROLE_API
					.antMatchers("/order/**").access("#oauth2.hasScope('ROLE_API')");
		}

	}
}
