package com.example.demo.security;

import com.github.surpassm.security.authorize.AuthorizeCofigProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * @author mc
 * version 1.0
 * date 2018/11/6 14:54
 * description
 */
@Configuration
public class SurpassmAuthorizeConfigProvider implements AuthorizeCofigProvider {
	@Override
	public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
		config.anyRequest().access("@rbacService.hasPermission(request,authentication)");
	}
}
