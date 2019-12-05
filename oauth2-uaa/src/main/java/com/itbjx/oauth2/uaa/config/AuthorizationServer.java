package com.itbjx.oauth2.uaa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.Arrays;

/**
 * 授权服务配置
 **/
@Configuration
@EnableAuthorizationServer  //开启授权服务
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private TokenStore tokenStore;

	@Autowired
	private ClientDetailsService clientDetailsService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private AuthorizationCodeServices authorizationCodeServices;

	@Autowired
	JwtAccessTokenConverter accessTokenConverter;

	@Autowired
	PasswordEncoder passwordEncoder;

	//配置客户端详细服务
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

		clients.withClientDetails(clientDetailsService);

		/*clients.inMemory()// 使用in-memory存储
				.withClient("c1")// client_id
				.secret(new BCryptPasswordEncoder().encode("secret"))//客户端密钥
				.resourceIds("res1")//资源列表
				.authorizedGrantTypes("authorization_code", "password","client_credentials","implicit","refresh_token")// 该client允许的授权类型authorization_code,password,refresh_token,implicit,client_credentials
				.scopes("all")// 允许的授权范围
				.autoApprove(false)//false跳转到授权页面
				//加上验证回调地址
				.redirectUris("http://www.baidu.com");*/
	}

	//将客户端信息存储到数据库
	@Bean
	public ClientDetailsService clientDetailsService(DataSource dataSource) {
		ClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
		((JdbcClientDetailsService) clientDetailsService).setPasswordEncoder(passwordEncoder);
		return clientDetailsService;
	}

	//令牌管理服务
	@Bean
	public AuthorizationServerTokenServices tokenService() {
		DefaultTokenServices service=new DefaultTokenServices();
		service.setClientDetailsService(clientDetailsService);//客户端详情服务
		service.setSupportRefreshToken(true);//支持刷新令牌
		service.setTokenStore(tokenStore);//令牌存储策略

		//令牌增强
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter));
		service.setTokenEnhancer(tokenEnhancerChain);

		service.setAccessTokenValiditySeconds(7200); // 令牌默认有效期2小时
		service.setRefreshTokenValiditySeconds(259200); // 刷新令牌默认有效期3天
		return service;
	}

	//令牌访问端点配置
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		endpoints
				.authenticationManager(authenticationManager)  //认证管理器
				.authorizationCodeServices(authorizationCodeServices)  //授权码服务
				.tokenServices(tokenService())  //令牌管理服务
				.allowedTokenEndpointRequestMethods(HttpMethod.POST); //允许post提交
	}

	//设置授权码模式的授权码如何存取，暂时采用内存方式
	/*@Bean
	public AuthorizationCodeServices authorizationCodeServices() {
		return new InMemoryAuthorizationCodeServices();
	}*/

	//设置授权码模式的授权码存取数据库
  @Bean
    public AuthorizationCodeServices authorizationCodeServices(DataSource dataSource) {
        return new JdbcAuthorizationCodeServices(dataSource);
    }


	//令牌端点的安全约束
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) {
		security
				.tokenKeyAccess("permitAll()")
				.checkTokenAccess("permitAll()")
				.allowFormAuthenticationForClients();
	}


}
