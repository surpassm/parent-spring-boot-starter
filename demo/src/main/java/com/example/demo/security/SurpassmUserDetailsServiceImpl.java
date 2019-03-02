package com.example.demo.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author mc
 * @version 1.0
 * @date 2018/9/10 10:24
 * @description
 */
@Component
public class SurpassmUserDetailsServiceImpl implements UserDetailsService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return buildUser(username);
	}


	UserDetails buildUser(String username) {
		// 根据用户名查找用户信息
		//根据查找到的用户信息判断用户是否被冻结
		String password = bCryptPasswordEncoder.encode("123456");
		logger.info("数据库密码是:"+password);
		return new User(username, password,
				true, true, true, true,
				AuthorityUtils.commaSeparatedStringToAuthorityList("admin,ROLE_USER"));
	}
}
