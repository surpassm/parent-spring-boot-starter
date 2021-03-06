package com.example.demo.security;

import com.example.demo.entity.user.UserInfo;
import com.example.demo.mapper.user.RoleMapper;
import com.example.demo.mapper.user.UserInfoMapper;
import com.github.surpassm.common.jackson.Tips;
import com.github.surpassm.security.exception.SurpassmAuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author mc
 * @version 1.0
 * @date 2018/9/10 10:24
 * @description
 */
@Slf4j
@Component
public class SurpassmUserDetailsServiceImpl implements UserDetailsService {

	@Resource
	private UserInfoMapper userInfoMapper;
	@Resource
	private RoleMapper roleMapper;

	@Resource
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return buildUser(username);
	}


	private UserDetails buildUser(String username) {
//		log.info("用户开始登陆:"+username);
//		if (StringUtils.isEmpty(username)) {
//			throw new SurpassmAuthenticationException(Tips.PARAMETER_ERROR.msg);
//		}
//		UserInfo build = new UserInfo();
//		build.setUsername(username);
//		build.setIsDelete(0);
//		UserInfo loginUser = userInfoMapper.selectOne(build);
//		if (loginUser == null) {
//			throw new SurpassmAuthenticationException(Tips.USER_INFO_ERROR.msg);
//		}
//		UserInfo userInfo = userInfoMapper.selectByUserInfoAndRolesAndMenus(loginUser.getId());
//		userInfo.setLandingTime(LocalDateTime.now());
//		userInfoMapper.updateByPrimaryKeySelective(userInfo);
//		return userInfo;
		String password = bCryptPasswordEncoder.encode("123456");
		log.info("数据库密码是:"+password);
		return new UserInfo(1,username, password,
				true, true, true, true,
				AuthorityUtils.commaSeparatedStringToAuthorityList("admin,ROLE_USER"));
	}
}
