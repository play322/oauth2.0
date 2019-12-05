package com.itbjx.oauth2.order.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itbjx.oauth2.order.model.UserDTO;
import com.itbjx.oauth2.order.utils.EncryptUtil;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sun.plugin.liveconnect.SecurityContextHelper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		//从请求头中获取网关转发过来的token
		String token = request.getHeader("json-token");
		if (token != null) {
			//1.解析token
			String json = EncryptUtil.decodeUTF8StringBase64(token); //解码
			//把token转化为json对象
			JSONObject jsonObject = JSON.parseObject(json);
			//用户身份信息
//			UserDTO userDTO = new UserDTO();
//			String principal = jsonObject.getString("principal");
//			userDTO.setUsername(principal);
			UserDTO userDTO = JSON.parseObject(jsonObject.getString("principal"), UserDTO.class);
			//获取用户权限信息
			JSONArray authoritiesArray = jsonObject.getJSONArray("authorities");
			//转化为数组
			String[] authorities = authoritiesArray.toArray(new String[authoritiesArray.size()]);

			//将用户的身份信息和权限信息填充到用户身份token对象中
			UsernamePasswordAuthenticationToken authenticationToken =
					new UsernamePasswordAuthenticationToken(userDTO,null, AuthorityUtils.createAuthorityList(authorities));
			authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

			//将authenticationToken保存进安全上下文
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		}
		filterChain.doFilter(request,response);
	}
}

