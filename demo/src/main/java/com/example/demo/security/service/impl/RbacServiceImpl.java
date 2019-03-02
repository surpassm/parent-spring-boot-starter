package com.example.demo.security.service.impl;

import com.example.demo.security.service.RbacService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;

/**
 * @author mc
 * version 1.0
 * date 2018/11/6 14:55
 * description 授权业务逻辑处理类
 */
@Slf4j
@Component("rbacService")
public class RbacServiceImpl implements RbacService {

	private AntPathMatcher antPathMatcher = new AntPathMatcher();
	@Override
	public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
		return true;
	}
}
