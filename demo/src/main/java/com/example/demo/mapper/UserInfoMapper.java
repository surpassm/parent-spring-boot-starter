package com.example.demo.mapper;

import com.example.demo.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
  * @author mc
  * Create date 2019-03-05 10:09:50
  * Version 1.0
  * Description
  */
@Mapper
public interface UserInfoMapper extends tk.mybatis.mapper.common.Mapper<UserInfo> {

    int insertSelectiveCustom(UserInfo userInfo);

	/**
	 * 根据账号查询用户信息
	 * @param username
	 * @return
	 */
	UserInfo selectOneByUserName(@Param("username") String username);

	/**
	 * 根据用户id删除角色关联
	 * @param id
	 */
	void deleteUserAndRole(Integer id);

	/**
	 * 新增用户角色
	 * @param id
	 * @param roleIdList
	 */
	void insertUserAndRole(@Param("id") Integer id, @Param("roleIdList") List<Integer> roleIdList);

	int selectUserAndRoleCount(Integer id);
}
