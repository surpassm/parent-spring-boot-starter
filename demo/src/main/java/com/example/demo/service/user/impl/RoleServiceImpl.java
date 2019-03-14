package com.example.demo.service.user.impl;

import com.example.demo.entity.user.Role;
import com.example.demo.entity.user.UserInfo;
import com.example.demo.mapper.user.RoleMapper;
import com.example.demo.security.BeanConfig;
import com.example.demo.service.user.RoleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.surpassm.common.jackson.Result;
import com.github.surpassm.common.jackson.Tips;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import javax.annotation.Resource;
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
@Transactional(rollbackFor={RuntimeException.class, Exception.class})
public class RoleServiceImpl implements RoleService {
    @Resource
    private RoleMapper roleMapper;
    @Resource
	private BeanConfig beanConfig;

    @Override
    public Result insert(String accessToken,Role role) {
        if (role == null){
            return fail(Tips.PARAMETER_ERROR.msg);
        }
        UserInfo loginUserInfo = beanConfig.getAccessToken(accessToken);
        role.setCreateUserId(loginUserInfo.getId());
        role.setCreateTime(new Date());
        role.setIsDelete(0);
        roleMapper.insertSelectiveCustom(role);
        return ok();
    }

    @Override
    public Result update(String accessToken,Role role) {
        if (role == null){
            return fail(Tips.PARAMETER_ERROR.msg);
        }
        UserInfo loginUserInfo = beanConfig.getAccessToken(accessToken);
        role.setUpdateUserId(loginUserInfo.getId());
        role.setUpdateTime(new Date());
        roleMapper.updateByPrimaryKeySelective(role);
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
        UserInfo loginUserInfo = beanConfig.getAccessToken(accessToken);
        role.setDeleteUserId(loginUserInfo.getId());
        role.setDeleteTime(new Date());
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
        if (role.getIsDelete() != null){
            builder.where(WeekendSqls.<Role>custom().andEqualTo(Role::getIsDelete,role.getIsDelete()));
        }
        if (role.getUpdateTime() != null){
            builder.where(WeekendSqls.<Role>custom().andEqualTo(Role::getUpdateTime,role.getUpdateTime()));
        }
        if (role.getUpdateUserId() != null){
            builder.where(WeekendSqls.<Role>custom().andEqualTo(Role::getUpdateUserId,role.getUpdateUserId()));
        }
        if (role.getDescribes() != null && !"".equals(role.getDescribes().trim())){
            builder.where(WeekendSqls.<Role>custom().andLike(Role::getDescribes,"%"+role.getDescribes()+"%"));
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
        }
        Page<Role> all = (Page<Role>) roleMapper.selectByExample(builder.build());
        Map<String, Object> map = new HashMap<>(16);
        map.put("total",all.getTotal());
        map.put("rows",all.getResult());
        return ok(map);
    }
}

