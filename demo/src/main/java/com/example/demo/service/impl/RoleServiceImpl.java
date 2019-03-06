package com.example.demo.service.impl;

import com.example.demo.entity.Menu;
import com.example.demo.entity.Role;
import com.example.demo.entity.UserInfo;
import com.example.demo.mapper.MenuMapper;
import com.example.demo.mapper.RoleMapper;
import com.example.demo.service.RoleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.surpassm.common.jackson.Result;
import com.github.surpassm.common.jackson.Tips;
import com.github.surpassm.config.BeanConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
  * Create date 2019-03-05 15:20:29
  * Version 1.0
  * Description 实现类
  */
@Slf4j
@Service
@Transactional(rollbackFor={RuntimeException.class, Exception.class})
public class RoleServiceImpl implements RoleService {
    @Resource
    private RoleMapper roleMapper;
    @Resource
	private BeanConfig beanConfig;
	@Resource
	private MenuMapper menuMapper;

    @Override
    public Result insert(String accessToken, Role role) {
        if (role == null){
            return fail(Tips.PARAMETER_ERROR.msg);
        }
        UserInfo loginUserInfo = beanConfig.getAccessToken(accessToken, UserInfo.class);

		List<Menu> menuList = menuMapper.selectByMenuList(role.getMenuList());
		if (menuList.size() != 0){
			return fail("权限不存在");
		}
		Role build = Role.builder().name(role.getName()).build();
		build.setIsDelete(0);
		List<Role> roleList = roleMapper.select(build);
		if(roleList.size() != 0){
			return fail("角色名称已存在");
		}
		//取出所有权限的ID
		List<Integer> ids = new ArrayList<>();
		menuList.forEach(i ->ids.add(i.getId()));
		role.setCreateUserId(loginUserInfo.getId());
		role.setCreateTime(new Date());
		role.setIsDelete(0);
		roleMapper.insertSelectiveCustom(role);
		//增加对应角色的权限
		roleMapper.insertRoleMenu(role.getId(),ids);

        return ok();
    }

    @Override
    public Result update(String accessToken,Role role) {
		if (role == null){
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		UserInfo loginUserInfo = beanConfig.getAccessToken(accessToken, UserInfo.class);
		Role selectByPrimaryKey = roleMapper.selectByPrimaryKey(role.getId());
		if (selectByPrimaryKey == null){
			return fail(Tips.MSG_NOT.msg);
		}
		//删除原有用户所有角色
		roleMapper.deleteByRoleAndMenu(selectByPrimaryKey.getId());
		List<Integer> ids = new ArrayList<>();
		role.getMenuList().forEach( i -> ids.add(i.getId()));
		//新增现有角色权限
		roleMapper.insertRoleMenu(role.getId(),ids);
		BeanUtils.copyProperties(role,selectByPrimaryKey);

		selectByPrimaryKey.setUpdateTime(new Date());
		selectByPrimaryKey.setUpdateUserId(loginUserInfo.getId());
		roleMapper.updateByPrimaryKeySelective(selectByPrimaryKey);
		return ok();
    }

    @Override
    public Result deleteGetById(String accessToken,Integer id){
		if (id == null){
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		Role role = roleMapper.selectByPrimaryKey(id);
		if(role == null){
			return fail(Tips.MSG_NOT.msg);
		}
		UserInfo loginUserInfo = beanConfig.getAccessToken(accessToken, UserInfo.class);
		role.setDeleteTime(new Date());
		role.setDeleteUserId(loginUserInfo.getId());
		role.setIsDelete(1);
		roleMapper.updateByPrimaryKeySelective(role);
		return ok();
    }


    @Override
    public Result findById(String accessToken,Integer id) {
		if (id == null){
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		Role role = roleMapper.selectByPrimaryKey(id);
		if (role == null){
			return fail(Tips.MSG_NOT.msg);
		}
		//查询当前角色所有权限列表
		List<Menu> menuList = menuMapper.selectMenuList(role.getId());
		role.getMenuList().addAll(menuList);
		return ok(role);

    }

    @Override
    public Result pageQuery(String accessToken,Integer page, Integer size, String sort, Role role) {
        page = null  == page ? 1 : page;
        size = null  == size ? 10 : size;
        PageHelper.startPage(page, size);
        Example.Builder builder = new Example.Builder(Role.class);
        builder.where(WeekendSqls.<Role>custom().andEqualTo(Role::getIsDelete, 0));
        if(role != null){
        if (role.getId() != null){
            builder.where(WeekendSqls.<Role>custom().andEqualTo(Role::getId,role.getId()));
        }
        if (role.getCreateTime() != null){
            builder.where(WeekendSqls.<Role>custom().andEqualTo(Role::getCreateTime,role.getCreateTime()));
        }
        if (role.getCreateUserId() != null){
            builder.where(WeekendSqls.<Role>custom().andEqualTo(Role::getCreateUserId,role.getCreateUserId()));
        }
        if (role.getDeleteTime() != null){
            builder.where(WeekendSqls.<Role>custom().andEqualTo(Role::getDeleteTime,role.getDeleteTime()));
        }
        if (role.getDeleteUserId() != null){
            builder.where(WeekendSqls.<Role>custom().andEqualTo(Role::getDeleteUserId,role.getDeleteUserId()));
        }
        if (role.getDescribes() != null && !"".equals(role.getDescribes().trim())){
            builder.where(WeekendSqls.<Role>custom().andLike(Role::getDescribes,"%"+role.getDescribes()+"%"));
        }
        if (role.getIsDelete() != null){
            builder.where(WeekendSqls.<Role>custom().andEqualTo(Role::getIsDelete,role.getIsDelete()));
        }
        if (role.getName() != null && !"".equals(role.getName().trim())){
            builder.where(WeekendSqls.<Role>custom().andLike(Role::getName,"%"+role.getName()+"%"));
        }
        if (role.getRoleIndex() != null){
            builder.where(WeekendSqls.<Role>custom().andEqualTo(Role::getRoleIndex,role.getRoleIndex()));
        }
        if (role.getSecurityRoles() != null && !"".equals(role.getSecurityRoles().trim())){
            builder.where(WeekendSqls.<Role>custom().andLike(Role::getSecurityRoles,"%"+role.getSecurityRoles()+"%"));
        }
        if (role.getUpdateTime() != null){
            builder.where(WeekendSqls.<Role>custom().andEqualTo(Role::getUpdateTime,role.getUpdateTime()));
        }
        if (role.getUpdateUserId() != null){
            builder.where(WeekendSqls.<Role>custom().andEqualTo(Role::getUpdateUserId,role.getUpdateUserId()));
        }
        }
        Page<Role> all = (Page<Role>) roleMapper.selectByExample(builder.build());
        Map<String, Object> map = new HashMap<>(16);
        map.put("total",all.getTotal());
        map.put("rows",all.getResult());
        return ok(map);
    }
}

