package com.example.demo.service.impl;

import com.example.demo.entity.Role;
import com.example.demo.entity.UserInfo;
import com.example.demo.mapper.RoleMapper;
import com.example.demo.mapper.UserInfoMapper;
import com.example.demo.service.UserInfoService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.surpassm.common.jackson.Result;
import com.github.surpassm.common.jackson.Tips;
import com.github.surpassm.config.BeanConfig;
import com.github.surpassm.tool.util.ValidateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
  * Create date 2019-03-05 10:09:50
  * Version 1.0
  * Description
  */
@Slf4j
@Service
@Transactional(rollbackFor={RuntimeException.class, Exception.class})
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
	private BeanConfig beanConfig;
    @Resource
	private RoleMapper roleMapper;
	@Resource
	private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Result insert(String accessToken, UserInfo userInfo) {
        if (userInfo == null){
            return fail(Tips.PARAMETER_ERROR.msg);
        }
		UserInfo loginUserInfo = beanConfig.getAccessToken(accessToken, UserInfo.class);
		userInfo.setCreateTime(new Date());
        userInfo.setCreateUserId(loginUserInfo.getId());
        userInfo.setIsDelete(0);
        userInfoMapper.insertSelectiveCustom(userInfo);
        return ok();
    }

    @Override
    public Result update(String accessToken,UserInfo userInfo) {
		if (userInfo == null) {
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		if (userInfo.getId() == null || "".equals(userInfo.getId())) {
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		List<Role> roleList = roleMapper.selectByPrimaryKeys(userInfo.getRoleList());
		if (roleList.size() == 0) {
			return fail("角色信息不存在");
		}
		//取出数据库中角色信息ID列表赋值给用户角色关系表,防止ID注入
		List<Integer> roleIdList = new ArrayList<>();
		roleList.forEach(i -> roleIdList.add(i.getId()));

		UserInfo loginUserInfo = beanConfig.getAccessToken(accessToken, UserInfo.class);
		UserInfo user = userInfoMapper.selectByPrimaryKey(userInfo.getId());
		String password = userInfo.getPassword();
		//删除当前用户所有角色
		userInfoMapper.deleteUserAndRole(user.getId());

		//密码效验
		if (userInfo.getPassword() != null && !"".equals(userInfo.getPassword())) {
			if (!ValidateUtil.isPassword(userInfo.getPassword())) {
				return fail(Tips.passwordFormatError.msg);
			}
			if (!password.equals(user.getPassword())) {
				//密码加密处理
				String passwordNew = bCryptPasswordEncoder.encode(password);
				userInfo.setPassword(passwordNew);
			}
		}

		userInfo.setUpdateTime(new Date());
		userInfo.setUpdateUserId(loginUserInfo.getId());

		userInfoMapper.updateByPrimaryKeySelective(userInfo);
		//新增用户角色信息
		userInfoMapper.insertUserAndRole(user.getId(), roleIdList);
		return ok();
    }

    @Override
    public Result deleteGetById(String accessToken,Integer id){
        if (id == null){
            return fail(Tips.PARAMETER_ERROR.msg);
        }
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(id);
        if(userInfo == null){
            return fail(Tips.MSG_NOT.msg);
        }
		UserInfo loginUserInfo =beanConfig.getAccessToken(accessToken, UserInfo.class);
        userInfo.setDeleteTime(new Date());
        userInfo.setDeleteUserId(loginUserInfo.getId());
        userInfo.setIsDelete(1);
        userInfoMapper.updateByPrimaryKeySelective(userInfo);
        return ok();
    }


    @Override
    public Result findById(String accessToken,Integer id) {
        if (id == null){
            return fail(Tips.PARAMETER_ERROR.msg);
        }
		UserInfo userInfo = userInfoMapper.selectByPrimaryKey(id);
        if (userInfo == null){
			return fail(Tips.MSG_NOT.msg);
		}
        return ok(userInfo);

    }

    @Override
    public Result pageQuery(String accessToken,Integer page, Integer size, String sort, UserInfo userInfo) {
        page = null  == page ? 1 : page;
        size = null  == size ? 10 : size;
        PageHelper.startPage(page, size);
        Example.Builder builder = new Example.Builder(UserInfo.class);
        builder.where(WeekendSqls.<UserInfo>custom().andEqualTo(UserInfo::getIsDelete, 0));
        if(userInfo != null){
        if (userInfo.getId() != null){
            builder.where(WeekendSqls.<UserInfo>custom().andEqualTo(UserInfo::getId,userInfo.getId()));
        }
        if (userInfo.getCreateTime() != null){
            builder.where(WeekendSqls.<UserInfo>custom().andEqualTo(UserInfo::getCreateTime,userInfo.getCreateTime()));
        }
        if (userInfo.getCreateUserId() != null){
            builder.where(WeekendSqls.<UserInfo>custom().andEqualTo(UserInfo::getCreateUserId,userInfo.getCreateUserId()));
        }
        if (userInfo.getDeleteTime() != null){
            builder.where(WeekendSqls.<UserInfo>custom().andEqualTo(UserInfo::getDeleteTime,userInfo.getDeleteTime()));
        }
        if (userInfo.getDeleteUserId() != null){
            builder.where(WeekendSqls.<UserInfo>custom().andEqualTo(UserInfo::getDeleteUserId,userInfo.getDeleteUserId()));
        }
        if (userInfo.getDepartmentId() != null){
            builder.where(WeekendSqls.<UserInfo>custom().andEqualTo(UserInfo::getDepartmentId,userInfo.getDepartmentId()));
        }
        if (userInfo.getIsDelete() != null){
            builder.where(WeekendSqls.<UserInfo>custom().andEqualTo(UserInfo::getIsDelete,userInfo.getIsDelete()));
        }
        if (userInfo.getLandingTime() != null){
            builder.where(WeekendSqls.<UserInfo>custom().andEqualTo(UserInfo::getLandingTime,userInfo.getLandingTime()));
        }
        if (userInfo.getMobile() != null && !"".equals(userInfo.getMobile().trim())){
            builder.where(WeekendSqls.<UserInfo>custom().andLike(UserInfo::getMobile,"%"+userInfo.getMobile()+"%"));
        }
        if (userInfo.getName() != null && !"".equals(userInfo.getName().trim())){
            builder.where(WeekendSqls.<UserInfo>custom().andLike(UserInfo::getName,"%"+userInfo.getName()+"%"));
        }
        if (userInfo.getPassword() != null && !"".equals(userInfo.getPassword().trim())){
            builder.where(WeekendSqls.<UserInfo>custom().andLike(UserInfo::getPassword,"%"+userInfo.getPassword()+"%"));
        }
        if (userInfo.getRegionId() != null){
            builder.where(WeekendSqls.<UserInfo>custom().andEqualTo(UserInfo::getRegionId,userInfo.getRegionId()));
        }
        if (userInfo.getUpdateTime() != null){
            builder.where(WeekendSqls.<UserInfo>custom().andEqualTo(UserInfo::getUpdateTime,userInfo.getUpdateTime()));
        }
        if (userInfo.getUpdateUserId() != null){
            builder.where(WeekendSqls.<UserInfo>custom().andEqualTo(UserInfo::getUpdateUserId,userInfo.getUpdateUserId()));
        }
        if (userInfo.getUserInfoIndex() != null){
            builder.where(WeekendSqls.<UserInfo>custom().andEqualTo(UserInfo::getUserInfoIndex,userInfo.getUserInfoIndex()));
        }
        if (userInfo.getUsername() != null && !"".equals(userInfo.getUsername().trim())){
            builder.where(WeekendSqls.<UserInfo>custom().andLike(UserInfo::getUsername,"%"+userInfo.getUsername()+"%"));
        }
        }
        Page<UserInfo> all = (Page<UserInfo>) userInfoMapper.selectByExample(builder.build());
        Map<String, Object> map = new HashMap<>(16);
        map.put("total",all.getTotal());
        map.put("rows",all.getResult());
        return ok(map);
    }
}

