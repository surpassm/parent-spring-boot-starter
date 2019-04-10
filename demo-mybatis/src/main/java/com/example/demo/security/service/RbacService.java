package com.example.demo.security.service;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * @author mc
 * version 1.0
 * date 2018/11/6 14:55
 * description
 */
public interface RbacService {
	boolean hasPermission(HttpServletRequest request, Authentication authentication);
}
