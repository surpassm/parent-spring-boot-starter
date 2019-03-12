package com.example.demo.service.impl;

import com.example.demo.entity.Region;
import com.example.demo.entity.UserInfo;
import com.example.demo.mapper.RegionMapper;
import com.example.demo.mapper.UserInfoMapper;
import com.example.demo.service.RegionService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.surpassm.common.jackson.Result;
import com.github.surpassm.common.jackson.Tips;
import com.example.demo.security.BeanConfig;
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
  * Create date 2019-03-05 15:20:29
  * Version 1.0
  * Description 实现类
  */
@Slf4j
@Service
@Transactional(rollbackFor={RuntimeException.class, Exception.class})
public class RegionServiceImpl implements RegionService {
    @Resource
    private RegionMapper regionMapper;
    @Resource
	private BeanConfig beanConfig;
	@Resource
	private UserInfoMapper userInfoMapper;

    @Override
    public Result insert(String accessToken, Region region) {
        if (region == null){
            return fail(Tips.PARAMETER_ERROR.msg);
        }
        UserInfo loginUserInfo = beanConfig.getAccessToken(accessToken);
        region.setCreateUserId(loginUserInfo.getId());
        region.setCreateTime(new Date());
        region.setIsDelete(0);
        regionMapper.insertSelectiveCustom(region);
        return ok();
    }

    @Override
    public Result update(String accessToken,Region region) {
        if (region == null){
            return fail(Tips.PARAMETER_ERROR.msg);
        }
        UserInfo loginUserInfo = beanConfig.getAccessToken(accessToken);
        region.setUpdateUserId(loginUserInfo.getId());
        region.setUpdateTime(new Date());
        regionMapper.updateByPrimaryKeySelective(region);
        return ok();
    }

    @Override
    public Result deleteGetById(String accessToken,Integer id){
		if (id == null){
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		Region region = regionMapper.selectByPrimaryKey(id);
		if(region == null){
			return fail(Tips.MSG_NOT.msg);
		}
		UserInfo userInfo = new UserInfo();
		userInfo.setRegionId(region.getId());
		int userInfoCount = userInfoMapper.selectCount(userInfo);
		if (userInfoCount != 0){
			return fail(Tips.AssociatedDataExistsAndCannotBeDeleted.msg);
		}
		UserInfo loginUserInfo = beanConfig.getAccessToken(accessToken);
		region.setDeleteUserId(loginUserInfo.getId());
		region.setDeleteTime(new Date());
		region.setIsDelete(1);
		regionMapper.updateByPrimaryKeySelective(region);
		return ok();
    }


    @Override
    public Result findById(String accessToken,Integer id) {
        if (id == null){
            return fail(Tips.PARAMETER_ERROR.msg);
        }
		Region region = regionMapper.selectByPrimaryKey(id);
        if (region == null){
			return fail(Tips.MSG_NOT.msg);
		}
        return ok(region);

    }

    @Override
    public Result pageQuery(String accessToken,Integer page, Integer size, String sort, Region region) {
        page = null  == page ? 1 : page;
        size = null  == size ? 10 : size;
        PageHelper.startPage(page, size);
        Example.Builder builder = new Example.Builder(Region.class);
        builder.where(WeekendSqls.<Region>custom().andEqualTo(Region::getIsDelete, 0));
        if(region != null){
        if (region.getId() != null){
            builder.where(WeekendSqls.<Region>custom().andEqualTo(Region::getId,region.getId()));
        }
        if (region.getCreateTime() != null){
            builder.where(WeekendSqls.<Region>custom().andEqualTo(Region::getCreateTime,region.getCreateTime()));
        }
        if (region.getCreateUserId() != null){
            builder.where(WeekendSqls.<Region>custom().andEqualTo(Region::getCreateUserId,region.getCreateUserId()));
        }
        if (region.getDeleteTime() != null){
            builder.where(WeekendSqls.<Region>custom().andEqualTo(Region::getDeleteTime,region.getDeleteTime()));
        }
        if (region.getDeleteUserId() != null){
            builder.where(WeekendSqls.<Region>custom().andEqualTo(Region::getDeleteUserId,region.getDeleteUserId()));
        }
        if (region.getDepartmentIndex() != null){
            builder.where(WeekendSqls.<Region>custom().andEqualTo(Region::getDepartmentIndex,region.getDepartmentIndex()));
        }
        if (region.getIsDelete() != null){
            builder.where(WeekendSqls.<Region>custom().andEqualTo(Region::getIsDelete,region.getIsDelete()));
        }
        if (region.getName() != null && !"".equals(region.getName().trim())){
            builder.where(WeekendSqls.<Region>custom().andLike(Region::getName,"%"+region.getName()+"%"));
        }
        if (region.getParentId() != null){
            builder.where(WeekendSqls.<Region>custom().andEqualTo(Region::getParentId,region.getParentId()));
        }
        if (region.getUpdateTime() != null){
            builder.where(WeekendSqls.<Region>custom().andEqualTo(Region::getUpdateTime,region.getUpdateTime()));
        }
        if (region.getUpdateUserId() != null){
            builder.where(WeekendSqls.<Region>custom().andEqualTo(Region::getUpdateUserId,region.getUpdateUserId()));
        }
        }
        Page<Region> all = (Page<Region>) regionMapper.selectByExample(builder.build());
        Map<String, Object> map = new HashMap<>(16);
        map.put("total",all.getTotal());
        map.put("rows",all.getResult());
        return ok(map);
    }
}

