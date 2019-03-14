package com.example.demo.service.user.impl;

import com.example.demo.entity.user.UserInfo;
import com.example.demo.mapper.user.UserInfoMapper;
import com.example.demo.security.BeanConfig;
import com.example.demo.service.user.UserInfoService;
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
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
	private BeanConfig beanConfig;

    @Override
    public Result insert(String accessToken,UserInfo userInfo) {
        if (userInfo == null){
            return fail(Tips.PARAMETER_ERROR.msg);
        }
        UserInfo loginUserInfo = beanConfig.getAccessToken(accessToken);
        userInfo.setCreateUserId(loginUserInfo.getId());
        userInfo.setCreateTime(new Date());
        userInfo.setIsDelete(0);
        userInfoMapper.insertSelectiveCustom(userInfo);
        return ok();
    }

    @Override
    public Result update(String accessToken,UserInfo userInfo) {
        if (userInfo == null){
            return fail(Tips.PARAMETER_ERROR.msg);
        }
        UserInfo loginUserInfo = beanConfig.getAccessToken(accessToken);
        userInfo.setUpdateUserId(loginUserInfo.getId());
        userInfo.setUpdateTime(new Date());
        userInfoMapper.updateByPrimaryKeySelective(userInfo);
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
        UserInfo loginUserInfo = beanConfig.getAccessToken(accessToken);
        userInfo.setDeleteUserId(loginUserInfo.getId());
        userInfo.setDeleteTime(new Date());
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
        if (userInfo.getIsDelete() != null){
            builder.where(WeekendSqls.<UserInfo>custom().andEqualTo(UserInfo::getIsDelete,userInfo.getIsDelete()));
        }
        if (userInfo.getUpdateTime() != null){
            builder.where(WeekendSqls.<UserInfo>custom().andEqualTo(UserInfo::getUpdateTime,userInfo.getUpdateTime()));
        }
        if (userInfo.getUpdateUserId() != null){
            builder.where(WeekendSqls.<UserInfo>custom().andEqualTo(UserInfo::getUpdateUserId,userInfo.getUpdateUserId()));
        }
        if (userInfo.getDepartmentId() != null){
            builder.where(WeekendSqls.<UserInfo>custom().andEqualTo(UserInfo::getDepartmentId,userInfo.getDepartmentId()));
        }
        if (userInfo.getHeadUrl() != null && !"".equals(userInfo.getHeadUrl().trim())){
            builder.where(WeekendSqls.<UserInfo>custom().andLike(UserInfo::getHeadUrl,"%"+userInfo.getHeadUrl()+"%"));
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

