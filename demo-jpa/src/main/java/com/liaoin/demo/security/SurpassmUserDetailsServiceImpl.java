package com.liaoin.demo.security;

import com.liaoin.demo.entity.user.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.AuthorityUtils;
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
@Slf4j
@Component
public class SurpassmUserDetailsServiceImpl implements UserDetailsService {


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
