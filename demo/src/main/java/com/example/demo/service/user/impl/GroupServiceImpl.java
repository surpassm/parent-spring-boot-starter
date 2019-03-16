package com.example.demo.service.user.impl;

import com.example.demo.entity.user.*;
import com.example.demo.mapper.user.GroupMapper;
import com.example.demo.mapper.user.GroupMenuMapper;
import com.example.demo.mapper.user.GroupRoleMapper;
import com.example.demo.mapper.user.UserGroupMapper;
import com.example.demo.security.BeanConfig;
import com.example.demo.service.user.GroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.surpassm.common.jackson.Result;
import com.github.surpassm.common.jackson.Tips;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import javax.annotation.Resource;
import java.util.*;

import static com.github.surpassm.common.jackson.Result.fail;
import static com.github.surpassm.common.jackson.Result.ok;


/**
 * @author mc
 * Create date 2019-03-14 20:41:03
 * Version 1.0
 * Description 权限实现类
 */
@Slf4j
@Service
@Transactional(rollbackFor = {RuntimeException.class, Exception.class})
public class GroupServiceImpl implements GroupService {
	@Resource
	private GroupMapper groupMapper;
	@Resource
	private BeanConfig beanConfig;
	@Resource
	private GroupMenuMapper groupMenuMapper;
	@Resource
	private GroupRoleMapper groupRoleMapper;
	@Resource
	private UserGroupMapper userGroupMapper;

	@Override
	public Result insert(String accessToken, Group group) {
		if (group == null) {
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		UserInfo loginUserInfo = beanConfig.getAccessToken(accessToken);
		//效验名称是否重复
		Group build = Group.builder().name(group.getName()).build();
		build.setIsDelete(0);
		int groupCount = groupMapper.selectCount(build);
		if (groupCount != 0) {
			return fail(Tips.nameRepeat.msg);
		}
		//查看父级是否存在
		if (isEnableParent(group)) {
			return fail(Tips.parentError.msg);
		}
		group.setCreateUserId(loginUserInfo.getId());
		group.setCreateTime(new Date());
		group.setIsDelete(0);
		groupMapper.insertSelectiveCustom(group);
		return ok();
	}

	@Override
	public Result update(String accessToken, Group group) {
		if (group == null) {
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		if (group.getIsDelete() == 1){
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		UserInfo loginUserInfo = beanConfig.getAccessToken(accessToken);

		Example.Builder builder = new Example.Builder(Group.class);
		builder.where(WeekendSqls.<Group>custom().andEqualTo(Group::getIsDelete, 0));
		builder.where(WeekendSqls.<Group>custom().andEqualTo(Group::getName, group.getName()));
		builder.where(WeekendSqls.<Group>custom().andNotIn(Group::getId, Collections.singletonList(group.getId())));

		List<Group> selectCount = groupMapper.selectByExample(builder.build());
		if (selectCount.size() != 0) {
			return fail(Tips.nameRepeat.msg);
		}
		if (isEnableParent(group)) {
			return fail(Tips.parentError.msg);
		}


		group.setUpdateUserId(loginUserInfo.getId());
		group.setUpdateTime(new Date());
		groupMapper.updateByPrimaryKeySelective(group);
		return ok();
	}

	private boolean isEnableParent(Group group) {
		if (group.getParentId() != null) {
			Group buildGroup = Group.builder().id(group.getParentId()).build();
			buildGroup.setIsDelete(0);
			int buildGroupCount = groupMapper.selectCount(buildGroup);
			return buildGroupCount == 0;
		}
		return false;
	}

	@Override
	public Result deleteGetById(String accessToken, Integer id) {
		if (id == null) {
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		Group group = groupMapper.selectByPrimaryKey(id);
		if (group == null) {
			return fail(Tips.MSG_NOT.msg);
		}
		UserInfo loginUserInfo = beanConfig.getAccessToken(accessToken);

		//组权限查询
		GroupMenu groupMenu = GroupMenu.builder().groupId(id).build();
		groupMenu.setIsDelete(0);
		int groupMenuCount = groupMenuMapper.selectCount(groupMenu);
		CommonImpl.groupMenuDeleteUpdata(loginUserInfo, groupMenu, groupMenuCount, groupMenuMapper);
		//组角色查询
		GroupRole groupRole = GroupRole.builder().groupId(id).build();
		groupRole.setIsDelete(0);
		int groupRoleCount = groupRoleMapper.selectCount(groupRole);
		CommonImpl.groupRoleDeleteUpdata(loginUserInfo,groupRole,groupRoleCount,groupRoleMapper);
		//用户组查询
		UserGroup userGroup = UserGroup.builder().groupId(id).build();
		userGroup.setIsDelete(0);
		int userGroupCount = userGroupMapper.selectCount(userGroup);
		CommonImpl.userGroupDeleteUpdata(loginUserInfo,userGroup,userGroupCount,userGroupMapper);
		group.setDeleteUserId(loginUserInfo.getId());
		group.setDeleteTime(new Date());
		group.setIsDelete(1);
		groupMapper.updateByPrimaryKeySelective(group);
		return ok();
	}


	@Override
	public Result findById(String accessToken, Integer id) {
		if (id == null) {
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		Group group = groupMapper.selectByPrimaryKey(id);
		if (group == null) {
			return fail(Tips.MSG_NOT.msg);
		}
		return ok(group);

	}

	@Override
	public Result pageQuery(String accessToken, Integer page, Integer size, String sort, Group group) {
		page = null == page ? 1 : page;
		size = null == size ? 10 : size;
		PageHelper.startPage(page, size);
		Example.Builder builder = new Example.Builder(Group.class);
		builder.where(WeekendSqls.<Group>custom().andEqualTo(Group::getIsDelete, 0));
		if (group != null) {
			if (group.getId() != null) {
				builder.where(WeekendSqls.<Group>custom().andEqualTo(Group::getId, group.getId()));
			}
			if (group.getCreateTime() != null) {
				builder.where(WeekendSqls.<Group>custom().andEqualTo(Group::getCreateTime, group.getCreateTime()));
			}
			if (group.getCreateUserId() != null) {
				builder.where(WeekendSqls.<Group>custom().andEqualTo(Group::getCreateUserId, group.getCreateUserId()));
			}
			if (group.getDeleteTime() != null) {
				builder.where(WeekendSqls.<Group>custom().andEqualTo(Group::getDeleteTime, group.getDeleteTime()));
			}
			if (group.getDeleteUserId() != null) {
				builder.where(WeekendSqls.<Group>custom().andEqualTo(Group::getDeleteUserId, group.getDeleteUserId()));
			}
			if (group.getIsDelete() != null) {
				builder.where(WeekendSqls.<Group>custom().andEqualTo(Group::getIsDelete, group.getIsDelete()));
			}
			if (group.getUpdateTime() != null) {
				builder.where(WeekendSqls.<Group>custom().andEqualTo(Group::getUpdateTime, group.getUpdateTime()));
			}
			if (group.getUpdateUserId() != null) {
				builder.where(WeekendSqls.<Group>custom().andEqualTo(Group::getUpdateUserId, group.getUpdateUserId()));
			}
			if (group.getDescribes() != null && !"".equals(group.getDescribes().trim())) {
				builder.where(WeekendSqls.<Group>custom().andLike(Group::getDescribes, "%" + group.getDescribes() + "%"));
			}
			if (group.getName() != null && !"".equals(group.getName().trim())) {
				builder.where(WeekendSqls.<Group>custom().andLike(Group::getName, "%" + group.getName() + "%"));
			}
			if (group.getParentId() != null) {
				builder.where(WeekendSqls.<Group>custom().andEqualTo(Group::getParentId, group.getParentId()));
			} else {
				builder.where(WeekendSqls.<Group>custom().andIsNull(Group::getParentId));
			}
		} else {
			builder.where(WeekendSqls.<Group>custom().andIsNull(Group::getParentId));
		}
		Page<Group> all = (Page<Group>) groupMapper.selectByExample(builder.build());
		Map<String, Object> map = new HashMap<>(16);
		map.put("total", all.getTotal());
		map.put("rows", all.getResult());
		return ok(map);
	}

	@Override
	public Result getParentId(String accessToken, Integer parentId) {
		List<Group> groups = groupMapper.selectChildByParentId(parentId);
		return ok(groups);
	}

	@Override
	public Result findByOnlyAndChildren(String accessToken, Integer id) {
		List<Group> groups = groupMapper.selectSelfAndChildByParentId(id);
		return ok(groups);
	}

	@Override
	public Result setGroupByMenu(String accessToken, Integer id, String menuId) {
		UserInfo loginUser = beanConfig.getAccessToken(accessToken);
		String[] splits = StringUtils.split(menuId,",");
		if (splits == null || splits.length == 0){
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		Group group = Group.builder().id(id).build();
		group.setIsDelete(0);
		int groupCount = groupMapper.selectCount(group);
		if (groupCount == 0){
			return fail(Tips.MSG_NOT.msg);
		}
		//删除原有组对应的权限
		Example.Builder builder = new Example.Builder(RoleMenu.class);
		builder.where(WeekendSqls.<GroupMenu>custom().andEqualTo(GroupMenu::getIsDelete, 0));
		builder.where(WeekendSqls.<GroupMenu>custom().andEqualTo(GroupMenu::getGroupId, id));
		groupMenuMapper.deleteByExample(builder.build());
		//新增现有的角色权限
		for(String split : splits){
			GroupMenu build = GroupMenu.builder().groupId(id).menuId(Integer.valueOf(split)).build();
			build.setIsDelete(0);
			build.setCreateUserId(loginUser.getId());
			build.setCreateTime(new Date());
			build.setMenuType(1);
			groupMenuMapper.insert(build);
		}
		return ok();
	}

	@Override
	public Result setGroupByRole(String accessToken, Integer id, String roleIds) {
		UserInfo loginUser = beanConfig.getAccessToken(accessToken);
		String[] splits = StringUtils.split(roleIds,",");
		if (splits == null || splits.length == 0){
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		Group group = Group.builder().id(id).build();
		group.setIsDelete(0);
		int groupCount = groupMapper.selectCount(group);
		if (groupCount == 0){
			return fail(Tips.MSG_NOT.msg);
		}
		//删除原有组对应的角色
		Example.Builder builder = new Example.Builder(GroupRole.class);
		builder.where(WeekendSqls.<GroupRole>custom().andEqualTo(GroupRole::getIsDelete, 0));
		builder.where(WeekendSqls.<GroupRole>custom().andEqualTo(GroupRole::getGroupId, id));
		groupRoleMapper.deleteByExample(builder.build());
		//新增现有的角色
		for(String split : splits){
			GroupRole build = GroupRole.builder().groupId(id).roleId(Integer.valueOf(split)).build();
			build.setIsDelete(0);
			build.setCreateUserId(loginUser.getId());
			build.setCreateTime(new Date());
			groupRoleMapper.insert(build);
		}
		return ok();
	}
}

