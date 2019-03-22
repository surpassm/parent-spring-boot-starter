package com.example.demo.service.user.impl;

import com.example.demo.entity.user.*;
import com.example.demo.mapper.user.UserGroupMapper;
import com.example.demo.mapper.user.UserInfoMapper;
import com.example.demo.mapper.user.UserMenuMapper;
import com.example.demo.mapper.user.UserRoleMapper;
import com.example.demo.security.BeanConfig;
import com.example.demo.service.user.UserInfoService;
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
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.github.surpassm.common.jackson.Result.fail;
import static com.github.surpassm.common.jackson.Result.ok;


/**
 * @author mc
 * Create date 2019-03-14 20:41:03
 * Version 1.0
 * Description 实现类
 */
@Slf4j
@Service
@Transactional(rollbackFor = {RuntimeException.class, Exception.class})
public class UserInfoServiceImpl implements UserInfoService {
	@Resource
	private UserInfoMapper userInfoMapper;
	@Resource
	private BeanConfig beanConfig;
	@Resource
	private UserGroupMapper userGroupMapper;
	@Resource
	private UserMenuMapper userMenuMapper;
	@Resource
	private UserRoleMapper userRoleMapper;
	@Resource
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Resource
	private DepartmentMapper departmentMapper;
	@Resource
	private RegionMapper regionMapper;

	@Override
	public Result insert(String accessToken, UserInfo userInfo) {
		if (userInfo == null) {
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		if (userInfo.getMobile() != null){
			if (!ValidateUtil.isMobilePhone(userInfo.getMobile())){
				return fail(Tips.phoneFormatError.msg);
			}
		}
		//效验手姓名
		if (!ValidateUtil.isRealName(userInfo.getName())){
			return fail("姓名格式错误");
		}
		if (userInfo.getUsername() == null || "".equals(userInfo.getUsername().trim())){
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		if (userInfo.getPassword() == null || "".equals(userInfo.getPassword().trim())){
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		if (!ValidateUtil.isPassword(userInfo.getPassword())){
			return fail(Tips.passwordFormatError.msg);
		}

		Department queryDepartment = Department.builder().id(userInfo.getDepartmentId()).build();
		queryDepartment.setIsDelete(0);

		Department department = departmentMapper.selectOne(queryDepartment);
		if (department == null) {
			return fail(Tips.departmentDataNull.msg);
		}

		Region queryRegion = Region.builder().id(userInfo.getRegionId()).build();
		Region region = regionMapper.selectOne(queryRegion);
		if (region == null) {
			return fail(Tips.regionDataNull.msg);
		}
		
		UserInfo user = new UserInfo();
		user.setUsername(userInfo.getUsername().trim());
		user.setIsDelete(0);
		int count = userInfoMapper.selectCount(user);
		if (count != 0){
			return fail("账号已存在");
		}
		userInfo.setPassword(bCryptPasswordEncoder.encode(userInfo.getPassword().trim()));
		UserInfo loginUserInfo = beanConfig.getAccessToken(accessToken);
		userInfo.setUsername(userInfo.getUsername().trim());
		userInfo.setCreateUserId(loginUserInfo.getId());
		userInfo.setCreateTime(LocalDateTime.now());
		userInfo.setIsDelete(0);
		userInfoMapper.insertSelectiveCustom(userInfo);
		return ok();
	}

	@Override
	public Result update(String accessToken, UserInfo userInfo) {
		if (userInfo == null) {
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		UserInfo loginUserInfo = beanConfig.getAccessToken(accessToken);
		userInfo.setUpdateUserId(loginUserInfo.getId());
		userInfo.setUpdateTime(LocalDateTime.now());
		userInfoMapper.updateByPrimaryKeySelective(userInfo);
		return ok();
	}

	@Override
	public Result deleteGetById(String accessToken, Integer id) {
		if (id == null) {
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		UserInfo userInfo = userInfoMapper.selectByPrimaryKey(id);
		if (userInfo == null) {
			return fail(Tips.MSG_NOT.msg);
		}
		UserInfo loginUserInfo = beanConfig.getAccessToken(accessToken);
		userInfo.setDeleteUserId(loginUserInfo.getId());
		userInfo.setDeleteTime(LocalDateTime.now());
		userInfo.setIsDelete(1);
		userInfoMapper.updateByPrimaryKeySelective(userInfo);
		return ok();
	}


	@Override
	public Result findById(String accessToken, Integer id) {
		if (id == null) {
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		UserInfo userInfo = userInfoMapper.selectByPrimaryKey(id);
		if (userInfo == null) {
			return fail(Tips.MSG_NOT.msg);
		}
		return ok(userInfo);

	}

	@Override
	public Result pageQuery(String accessToken, Integer page, Integer size, String sort, UserInfo userInfo) {
		page = null == page ? 1 : page;
		size = null == size ? 10 : size;
		PageHelper.startPage(page, size);
		Example.Builder builder = new Example.Builder(UserInfo.class);
		builder.where(WeekendSqls.<UserInfo>custom().andEqualTo(UserInfo::getIsDelete, 0));
		if (userInfo != null) {
			if (userInfo.getId() != null) {
				builder.where(WeekendSqls.<UserInfo>custom().andEqualTo(UserInfo::getId, userInfo.getId()));
			}
			if (userInfo.getCreateTime() != null) {
				builder.where(WeekendSqls.<UserInfo>custom().andEqualTo(UserInfo::getCreateTime, userInfo.getCreateTime()));
			}
			if (userInfo.getCreateUserId() != null) {
				builder.where(WeekendSqls.<UserInfo>custom().andEqualTo(UserInfo::getCreateUserId, userInfo.getCreateUserId()));
			}
			if (userInfo.getDeleteTime() != null) {
				builder.where(WeekendSqls.<UserInfo>custom().andEqualTo(UserInfo::getDeleteTime, userInfo.getDeleteTime()));
			}
			if (userInfo.getDeleteUserId() != null) {
				builder.where(WeekendSqls.<UserInfo>custom().andEqualTo(UserInfo::getDeleteUserId, userInfo.getDeleteUserId()));
			}
			if (userInfo.getIsDelete() != null) {
				builder.where(WeekendSqls.<UserInfo>custom().andEqualTo(UserInfo::getIsDelete, userInfo.getIsDelete()));
			}
			if (userInfo.getUpdateTime() != null) {
				builder.where(WeekendSqls.<UserInfo>custom().andEqualTo(UserInfo::getUpdateTime, userInfo.getUpdateTime()));
			}
			if (userInfo.getUpdateUserId() != null) {
				builder.where(WeekendSqls.<UserInfo>custom().andEqualTo(UserInfo::getUpdateUserId, userInfo.getUpdateUserId()));
			}
			if (userInfo.getDepartmentId() != null) {
				builder.where(WeekendSqls.<UserInfo>custom().andEqualTo(UserInfo::getDepartmentId, userInfo.getDepartmentId()));
			}
			if (userInfo.getHeadUrl() != null && !"".equals(userInfo.getHeadUrl().trim())) {
				builder.where(WeekendSqls.<UserInfo>custom().andLike(UserInfo::getHeadUrl, "%" + userInfo.getHeadUrl() + "%"));
			}
			if (userInfo.getLandingTime() != null) {
				builder.where(WeekendSqls.<UserInfo>custom().andEqualTo(UserInfo::getLandingTime, userInfo.getLandingTime()));
			}
			if (userInfo.getMobile() != null && !"".equals(userInfo.getMobile().trim())) {
				builder.where(WeekendSqls.<UserInfo>custom().andLike(UserInfo::getMobile, "%" + userInfo.getMobile() + "%"));
			}
			if (userInfo.getName() != null && !"".equals(userInfo.getName().trim())) {
				builder.where(WeekendSqls.<UserInfo>custom().andLike(UserInfo::getName, "%" + userInfo.getName() + "%"));
			}
			if (userInfo.getPassword() != null && !"".equals(userInfo.getPassword().trim())) {
				builder.where(WeekendSqls.<UserInfo>custom().andLike(UserInfo::getPassword, "%" + userInfo.getPassword() + "%"));
			}
			if (userInfo.getRegionId() != null) {
				builder.where(WeekendSqls.<UserInfo>custom().andEqualTo(UserInfo::getRegionId, userInfo.getRegionId()));
			}
			if (userInfo.getUserInfoIndex() != null) {
				builder.where(WeekendSqls.<UserInfo>custom().andEqualTo(UserInfo::getUserInfoIndex, userInfo.getUserInfoIndex()));
			}
			if (userInfo.getUsername() != null && !"".equals(userInfo.getUsername().trim())) {
				builder.where(WeekendSqls.<UserInfo>custom().andLike(UserInfo::getUsername, "%" + userInfo.getUsername() + "%"));
			}
		}
		Page<UserInfo> all = (Page<UserInfo>) userInfoMapper.selectByExample(builder.build());
		Map<String, Object> map = new HashMap<>(16);
		map.put("total", all.getTotal());
		map.put("rows", all.getResult());
		return ok(map);
	}

	/**
	 * 根据主键查询用户及角色、权限列表
	 *
	 * @param accessToken token
	 * @param id 系统标识
	 * @return 返回数据
	 */
	@Override
	public Result findRolesAndMenus(String accessToken, Integer id) {
		beanConfig.getAccessToken(accessToken);
		UserInfo userInfo = userInfoMapper.selectByUserInfoAndRolesAndMenus(id);
		if (userInfo == null){
			return fail(Tips.MSG_NOT.msg);
		}
		return ok(userInfo);
	}

	/**
	 * 设置用户、组
	 * @param accessToken token
	 * @param id 用户系统标识
	 * @param groupIds 组系统标识
	 * @return 返回数据
	 */
	@Override
	public Result setUserByGroup(String accessToken, Integer id, String groupIds) {
		UserInfo loginUser = beanConfig.getAccessToken(accessToken);
		String[] splits = StringUtils.split(groupIds,",");
		if (splits == null || splits.length == 0){
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		UserInfo userInfo = new UserInfo();
		userInfo.setId(id);
		userInfo.setIsDelete(0);
		int count = userInfoMapper.selectCount(userInfo);
		if (count == 0){
			return fail(Tips.MSG_NOT.msg);
		}
		//删除原有用户对应的组
		Example.Builder builder = new Example.Builder(UserGroup.class);
		builder.where(WeekendSqls.<UserGroup>custom().andEqualTo(UserGroup::getIsDelete, 0));
		builder.where(WeekendSqls.<UserGroup>custom().andEqualTo(UserGroup::getUserId, id));
		userGroupMapper.deleteByExample(builder.build());
		//新增现有的用户组
		for(String split: splits){
			UserGroup build = UserGroup.builder().userId(id).groupId(Integer.valueOf(split)).build();
			build.setIsDelete(0);
			build.setCreateUserId(loginUser.getId());
			build.setCreateTime(LocalDateTime.now());
			userGroupMapper.insert(build);
		}
		return ok();
	}

	/**
	 * 设置用户权限
	 * @param accessToken token
	 * @param id 用户系统标识
	 * @param menuIds 组系统标识
	 * @return 返回数据
	 */
	@Override
	public Result setUserByMenu(String accessToken, Integer id, String menuIds) {
		UserInfo loginUser = beanConfig.getAccessToken(accessToken);
		String[] splits = StringUtils.split(menuIds,",");
		if (splits == null || splits.length == 0){
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		UserInfo userInfo = new UserInfo();
		userInfo.setId(id);
		userInfo.setIsDelete(0);
		int count = userInfoMapper.selectCount(userInfo);
		if (count == 0){
			return fail(Tips.MSG_NOT.msg);
		}
		//删除原有用户对应的权限
		Example.Builder builder = new Example.Builder(UserMenu.class);
		builder.where(WeekendSqls.<UserMenu>custom().andEqualTo(UserMenu::getIsDelete, 0));
		builder.where(WeekendSqls.<UserMenu>custom().andEqualTo(UserMenu::getUserId, id));
		userMenuMapper.deleteByExample(builder.build());
		//新增现有的用户权限
		for(String split: splits){
			UserMenu build = UserMenu.builder().userId(id).menuId(Integer.valueOf(split)).build();
			build.setIsDelete(0);
			build.setCreateUserId(loginUser.getId());
			build.setCreateTime(LocalDateTime.now());
			userMenuMapper.insert(build);
		}
		return ok();
	}

	/**
	 * 设置用户、角色
	 * @param accessToken token
	 * @param id 用户系统标识
	 * @param roleIds 组系统标识
	 * @return 返回数据
	 */
	@Override
	public Result setUserByRoles(String accessToken, Integer id, String roleIds) {
		UserInfo loginUser = beanConfig.getAccessToken(accessToken);
		String[] splits = StringUtils.split(roleIds,",");
		if (splits == null || splits.length == 0){
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		UserInfo userInfo = new UserInfo();
		userInfo.setId(id);
		userInfo.setIsDelete(0);
		int count = userInfoMapper.selectCount(userInfo);
		if (count == 0){
			return fail(Tips.MSG_NOT.msg);
		}
		//删除原有用户对应的角色
		Example.Builder builder = new Example.Builder(UserRole.class);
		builder.where(WeekendSqls.<UserRole>custom().andEqualTo(UserRole::getIsDelete, 0));
		builder.where(WeekendSqls.<UserRole>custom().andEqualTo(UserRole::getUserId, id));
		userRoleMapper.deleteByExample(builder.build());
		//新增现有的用户角色
		for(String split: splits){
			UserRole build = UserRole.builder().userId(id).roleId(Integer.valueOf(split)).build();
			build.setIsDelete(0);
			build.setCreateUserId(loginUser.getId());
			build.setCreateTime(LocalDateTime.now());
			userRoleMapper.insert(build);
		}
		return ok();
	}
}

