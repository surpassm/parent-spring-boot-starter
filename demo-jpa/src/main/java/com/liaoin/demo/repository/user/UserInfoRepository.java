package com.liaoin.demo.repository.user;

import com.liaoin.demo.entity.user.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
  * @author mc
  * Create date 2019-04-10 12:49:52
  * Version 1.0
  * Description
  */
public interface UserInfoRepository extends JpaRepository<UserInfo, Integer>, JpaSpecificationExecutor<UserInfo> {
	/**
	 * 根据账号查询列表
	 * @param username
	 * @return
	 */
	UserInfo findByUsername(String username);
}