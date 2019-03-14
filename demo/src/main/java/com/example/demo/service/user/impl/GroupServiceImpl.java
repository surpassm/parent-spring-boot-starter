package com.example.demo.service.user.impl;

import com.example.demo.entity.user.Group;
import com.example.demo.entity.user.UserInfo;
import com.example.demo.mapper.user.GroupMapper;
import com.example.demo.security.BeanConfig;
import com.example.demo.service.user.GroupService;
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
  * Description 权限实现类
  */
@Slf4j
@Service
@Transactional(rollbackFor={RuntimeException.class, Exception.class})
public class GroupServiceImpl implements GroupService {
    @Resource
    private GroupMapper groupMapper;
    @Resource
	private BeanConfig beanConfig;

    @Override
    public Result insert(String accessToken,Group group) {
        if (group == null){
            return fail(Tips.PARAMETER_ERROR.msg);
        }
        UserInfo loginUserInfo = beanConfig.getAccessToken(accessToken);
        group.setCreateUserId(loginUserInfo.getId());
        group.setCreateTime(new Date());
        group.setIsDelete(0);
        groupMapper.insertSelectiveCustom(group);
        return ok();
    }

    @Override
    public Result update(String accessToken,Group group) {
        if (group == null){
            return fail(Tips.PARAMETER_ERROR.msg);
        }
        UserInfo loginUserInfo = beanConfig.getAccessToken(accessToken);
        group.setUpdateUserId(loginUserInfo.getId());
        group.setUpdateTime(new Date());
        groupMapper.updateByPrimaryKeySelective(group);
        return ok();
    }

    @Override
    public Result deleteGetById(String accessToken,Integer id){
        if (id == null){
            return fail(Tips.PARAMETER_ERROR.msg);
        }
        Group group = groupMapper.selectByPrimaryKey(id);
        if(group == null){
            return fail(Tips.MSG_NOT.msg);
        }
        UserInfo loginUserInfo = beanConfig.getAccessToken(accessToken);
        group.setDeleteUserId(loginUserInfo.getId());
        group.setDeleteTime(new Date());
        group.setIsDelete(1);
        groupMapper.updateByPrimaryKeySelective(group);
        return ok();
    }


    @Override
    public Result findById(String accessToken,Integer id) {
        if (id == null){
            return fail(Tips.PARAMETER_ERROR.msg);
        }
		Group group = groupMapper.selectByPrimaryKey(id);
        if (group == null){
			return fail(Tips.MSG_NOT.msg);
		}
        return ok(group);

    }

    @Override
    public Result pageQuery(String accessToken,Integer page, Integer size, String sort, Group group) {
        page = null  == page ? 1 : page;
        size = null  == size ? 10 : size;
        PageHelper.startPage(page, size);
        Example.Builder builder = new Example.Builder(Group.class);
        builder.where(WeekendSqls.<Group>custom().andEqualTo(Group::getIsDelete, 0));
        if(group != null){
        if (group.getId() != null){
            builder.where(WeekendSqls.<Group>custom().andEqualTo(Group::getId,group.getId()));
        }
        if (group.getCreateTime() != null){
            builder.where(WeekendSqls.<Group>custom().andEqualTo(Group::getCreateTime,group.getCreateTime()));
        }
        if (group.getCreateUserId() != null){
            builder.where(WeekendSqls.<Group>custom().andEqualTo(Group::getCreateUserId,group.getCreateUserId()));
        }
        if (group.getDeleteTime() != null){
            builder.where(WeekendSqls.<Group>custom().andEqualTo(Group::getDeleteTime,group.getDeleteTime()));
        }
        if (group.getDeleteUserId() != null){
            builder.where(WeekendSqls.<Group>custom().andEqualTo(Group::getDeleteUserId,group.getDeleteUserId()));
        }
        if (group.getIsDelete() != null){
            builder.where(WeekendSqls.<Group>custom().andEqualTo(Group::getIsDelete,group.getIsDelete()));
        }
        if (group.getUpdateTime() != null){
            builder.where(WeekendSqls.<Group>custom().andEqualTo(Group::getUpdateTime,group.getUpdateTime()));
        }
        if (group.getUpdateUserId() != null){
            builder.where(WeekendSqls.<Group>custom().andEqualTo(Group::getUpdateUserId,group.getUpdateUserId()));
        }
        if (group.getDescribes() != null && !"".equals(group.getDescribes().trim())){
            builder.where(WeekendSqls.<Group>custom().andLike(Group::getDescribes,"%"+group.getDescribes()+"%"));
        }
        if (group.getName() != null && !"".equals(group.getName().trim())){
            builder.where(WeekendSqls.<Group>custom().andLike(Group::getName,"%"+group.getName()+"%"));
        }
        if (group.getParentId() != null){
            builder.where(WeekendSqls.<Group>custom().andEqualTo(Group::getParentId,group.getParentId()));
        }
        }
        Page<Group> all = (Page<Group>) groupMapper.selectByExample(builder.build());
        Map<String, Object> map = new HashMap<>(16);
        map.put("total",all.getTotal());
        map.put("rows",all.getResult());
        return ok(map);
    }
}

