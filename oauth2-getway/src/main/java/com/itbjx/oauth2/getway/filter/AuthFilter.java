package com.itbjx.oauth2.getway.filter;

import com.alibaba.fastjson.JSON;
import com.itbjx.oauth2.getway.utils.EncryptUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthFilter extends ZuulFilter {

	@Override
	public boolean shouldFilter() {   //必须进行过滤
		return true;
	}

	@Override
	public int filterOrder() {  //优先过滤（数值越小越优先）
		return 0;
	}
	@Override
	public String filterType() {  //请求前过滤拦截
		return "pre";
	}

	@Override
	public Object run() throws ZuulException {
		//上下文获取request对象
		RequestContext ctx = RequestContext.getCurrentContext();
		//从安全上下文中得到用户身份信息对象
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		//如果身份信息对象不是oauth2的格式，那么不进行解析
		if(!(authentication instanceof OAuth2Authentication)){
			return null;
		}
		//把得到的身份信息对象转换为OAuth2Authentication
		OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;

		Authentication userAuthentication = oAuth2Authentication.getUserAuthentication();

		// 获取当前用户的身份
		String principal = userAuthentication.getName();

		// 2，获取当前用户的权限信息
		List<String> authorities = new ArrayList<>();
		//从userAuthentication中取出权限，放入list集合
		userAuthentication.getAuthorities().stream().forEach(c->authorities.add(((GrantedAuthority) c).getAuthority()));
		//从oAuth2Authentication中得到其他请求参数
		OAuth2Request oAuth2Request = oAuth2Authentication.getOAuth2Request();
		Map<String, String> requestParameters = oAuth2Request.getRequestParameters();
		Map<String, Object>  jsonToken = new HashMap<>(requestParameters);

		if (userAuthentication !=null){
			jsonToken.put("principal",principal);
			jsonToken.put("authorities",authorities);
		}
		// 3，把身份信息和权限信息放到json中，加入http的header中,转发给微服务
		ctx.addZuulRequestHeader("json-token",EncryptUtil.encodeUTF8StringBase64(JSON.toJSONString(jsonToken)));

		return null;
	}
}
