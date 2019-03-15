package com.example.demo.service.user.impl;

import com.example.demo.entity.user.*;
import com.example.demo.mapper.user.*;

import java.util.Date;

/**
 * @author mc
 * Create date 2019/3/15 13:20
 * Version 1.0
 * Description
 */
public class CommonImpl {

	static void groupMenuDeleteUpdata(UserInfo loginUserInfo, GroupMenu groupMenu, int groupMenuCount, GroupMenuMapper groupMenuMapper) {
		if (groupMenuCount != 0){
			groupMenu.setIsDelete(1);
			groupMenu.setDeleteTime(new Date());
			groupMenu.setDeleteUserId(loginUserInfo.getId());
			groupMenuMapper.updateByPrimaryKeySelective(groupMenu);
		}
	}

	static void roleMenuDeleteUpdata(UserInfo loginUserInfo, RoleMenu roleMenu, int roleMenuCount, RoleMenuMapper roleMenuMapper) {
		if (roleMenuCount != 0){
			roleMenu.setIsDelete(1);
			roleMenu.setDeleteTime(new Date());
			roleMenu.setDeleteUserId(loginUserInfo.getId());
			roleMenuMapper.updateByPrimaryKeySelective(roleMenu);
		}
	}
	static void userMenuDeleteUpdata(UserInfo loginUserInfo, UserMenu userMenu, int userMenuCount, UserMenuMapper userMenuMapper) {
		if (userMenuCount!=0){
			userMenu.setIsDelete(1);
			userMenu.setDeleteTime(new Date());
			userMenu.setDeleteUserId(loginUserInfo.getId());
			userMenuMapper.updateByPrimaryKeySelective(userMenu);
		}
	}
	static void groupRoleDeleteUpdata(UserInfo loginUserInfo, GroupRole groupRole, int groupRoleCount, GroupRoleMapper groupRoleMapper) {
		if (groupRoleCount != 0){
			groupRole.setIsDelete(1);
			groupRole.setDeleteTime(new Date());
			groupRole.setDeleteUserId(loginUserInfo.getId());
			groupRoleMapper.updateByPrimaryKeySelective(groupRole);
		}
	}
	static void userGroupDeleteUpdata(UserInfo loginUserInfo, UserGroup userGroup, int userGroupCount, UserGroupMapper userGroupMapper) {
		if (userGroupCount != 0){
			userGroup.setIsDelete(1);
			userGroup.setDeleteTime(new Date());
			userGroup.setDeleteUserId(loginUserInfo.getId());
			userGroupMapper.updateByPrimaryKeySelective(userGroup);
		}
	}
	static void userInfoDeleteUpdata(UserInfo loginUserInfo, UserInfo userinfo, int userCount, UserInfoMapper userInfoMapper) {
		if (userCount != 0){
			userinfo.setIsDelete(1);
			userinfo.setDeleteTime(new Date());
			userinfo.setDeleteUserId(loginUserInfo.getId());
			userInfoMapper.updateByPrimaryKeySelective(userinfo);
		}
	}
}
