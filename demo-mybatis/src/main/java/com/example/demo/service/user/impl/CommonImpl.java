package com.example.demo.service.user.impl;

import com.example.demo.entity.user.*;
import com.example.demo.mapper.user.*;

import java.time.LocalDateTime;
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
			groupMenuMapper.updateByPrimaryKeySelective(groupMenu);
		}
	}

	static void roleMenuDeleteUpdata(UserInfo loginUserInfo, RoleMenu roleMenu, int roleMenuCount, RoleMenuMapper roleMenuMapper) {
		if (roleMenuCount != 0){
			roleMenu.setIsDelete(1);
			roleMenuMapper.updateByPrimaryKeySelective(roleMenu);
		}
	}
	static void userMenuDeleteUpdata(UserInfo loginUserInfo, UserMenu userMenu, int userMenuCount, UserMenuMapper userMenuMapper) {
		if (userMenuCount!=0){
			userMenu.setIsDelete(1);
			userMenuMapper.updateByPrimaryKeySelective(userMenu);
		}
	}
	static void groupRoleDeleteUpdata(UserInfo loginUserInfo, GroupRole groupRole, int groupRoleCount, GroupRoleMapper groupRoleMapper) {
		if (groupRoleCount != 0){
			groupRole.setIsDelete(1);
			groupRoleMapper.updateByPrimaryKeySelective(groupRole);
		}
	}
	static void userGroupDeleteUpdata(UserInfo loginUserInfo, UserGroup userGroup, int userGroupCount, UserGroupMapper userGroupMapper) {
		if (userGroupCount != 0){
			userGroup.setIsDelete(1);
			userGroupMapper.updateByPrimaryKeySelective(userGroup);
		}
	}
	static void userInfoDeleteUpdata(UserInfo loginUserInfo, UserInfo userinfo, int userCount, UserInfoMapper userInfoMapper) {
		if (userCount != 0){
			userinfo.setIsDelete(1);
			userInfoMapper.updateByPrimaryKeySelective(userinfo);
		}
	}
}
