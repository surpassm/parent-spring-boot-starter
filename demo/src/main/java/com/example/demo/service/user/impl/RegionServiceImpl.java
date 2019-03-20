package com.example.demo.service.user.impl;

import com.example.demo.entity.user.Department;
import com.example.demo.entity.user.Menu;
import com.example.demo.entity.user.Region;
import com.example.demo.entity.user.UserInfo;
import com.example.demo.mapper.user.RegionMapper;
import com.example.demo.mapper.user.UserInfoMapper;
import com.example.demo.security.BeanConfig;
import com.example.demo.service.user.RegionService;
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
import java.time.LocalDateTime;
import java.util.*;

import static com.example.demo.service.user.impl.CommonImpl.userInfoDeleteUpdata;
import static com.github.surpassm.common.jackson.Result.fail;
import static com.github.surpassm.common.jackson.Result.ok;


/**
 * @author mc
 * Create date 2019-03-14 20:41:03
 * Version 1.0
 * Description 区域实现类
 */
@Slf4j
@Service
@Transactional(rollbackFor = {RuntimeException.class, Exception.class})
public class RegionServiceImpl implements RegionService {
	@Resource
	private RegionMapper regionMapper;
	@Resource
	private BeanConfig beanConfig;
	@Resource
	private UserInfoMapper userInfoMapper;

	@Override
	public Result insert(String accessToken, Region region) {
		if (region == null) {
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		UserInfo loginUserInfo = beanConfig.getAccessToken(accessToken);
		//效验名称是否重复
		Region build = Region.builder().name(region.getName()).build();
		build.setIsDelete(0);
		int groupCount = regionMapper.selectCount(build);
		if (groupCount != 0) {
			return fail(Tips.nameRepeat.msg);
		}
		//查看父级是否存在
		if (isEnableParent(region)) {
			return fail(Tips.parentError.msg);
		}

		region.setCreateUserId(loginUserInfo.getId());
		region.setCreateTime(LocalDateTime.now());
		region.setIsDelete(0);
		regionMapper.insertSelectiveCustom(region);
		return ok();
	}

	@Override
	public Result update(String accessToken, Region region) {
		if (region == null) {
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		if (region.getIsDelete() == 1){
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		UserInfo loginUserInfo = beanConfig.getAccessToken(accessToken);

		Example.Builder builder = new Example.Builder(Region.class);
		builder.where(WeekendSqls.<Region>custom().andEqualTo(Region::getIsDelete, 0));
		builder.where(WeekendSqls.<Region>custom().andEqualTo(Region::getName, region.getName()));
		builder.where(WeekendSqls.<Region>custom().andNotIn(Region::getId, Collections.singletonList(region.getId())));

		List<Region> selectCount = regionMapper.selectByExample(builder.build());
		if (selectCount.size() != 0) {
			return fail(Tips.nameRepeat.msg);
		}
		if (isEnableParent(region)) {
			return fail(Tips.parentError.msg);
		}


		region.setUpdateUserId(loginUserInfo.getId());
		region.setUpdateTime(LocalDateTime.now());
		regionMapper.updateByPrimaryKeySelective(region);
		return ok();
	}

	private boolean isEnableParent(Region region) {
		if (region.getParentId() != null) {
			Region buildRegion = Region.builder().id(region.getParentId()).build();
			buildRegion.setIsDelete(0);
			int buildRegionCount = regionMapper.selectCount(buildRegion);
			return buildRegionCount == 0;
		}
		return false;
	}

	@Override
	public Result deleteGetById(String accessToken, Integer id) {
		if (id == null) {
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		Region region = regionMapper.selectByPrimaryKey(id);
		if (region == null) {
			return fail(Tips.MSG_NOT.msg);
		}
		UserInfo loginUserInfo = beanConfig.getAccessToken(accessToken);

		UserInfo userinfo = new UserInfo();
		userinfo.setRegionId(id);
		userinfo.setIsDelete(0);
		int userCount = userInfoMapper.selectCount(userinfo);
		//用户查询并删除
		userInfoDeleteUpdata(loginUserInfo,userinfo,userCount,userInfoMapper);

		region.setDeleteUserId(loginUserInfo.getId());
		region.setDeleteTime(LocalDateTime.now());
		region.setIsDelete(1);
		regionMapper.updateByPrimaryKeySelective(region);
		return ok();
	}


	@Override
	public Result findById(String accessToken, Integer id) {
		if (id == null) {
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		Region region = regionMapper.selectByPrimaryKey(id);
		if (region == null) {
			return fail(Tips.MSG_NOT.msg);
		}
		return ok(region);

	}

	@Override
	public Result pageQuery(String accessToken, Integer page, Integer size, String sort, Region region) {
		page = null == page ? 1 : page;
		size = null == size ? 10 : size;
		PageHelper.startPage(page, size);
		Example.Builder builder = new Example.Builder(Region.class);
		builder.where(WeekendSqls.<Region>custom().andEqualTo(Region::getIsDelete, 0));
		if (region != null) {
			if (region.getId() != null) {
				builder.where(WeekendSqls.<Region>custom().andEqualTo(Region::getId, region.getId()));
			}
			if (region.getCreateTime() != null) {
				builder.where(WeekendSqls.<Region>custom().andEqualTo(Region::getCreateTime, region.getCreateTime()));
			}
			if (region.getCreateUserId() != null) {
				builder.where(WeekendSqls.<Region>custom().andEqualTo(Region::getCreateUserId, region.getCreateUserId()));
			}
			if (region.getDeleteTime() != null) {
				builder.where(WeekendSqls.<Region>custom().andEqualTo(Region::getDeleteTime, region.getDeleteTime()));
			}
			if (region.getDeleteUserId() != null) {
				builder.where(WeekendSqls.<Region>custom().andEqualTo(Region::getDeleteUserId, region.getDeleteUserId()));
			}
			if (region.getIsDelete() != null) {
				builder.where(WeekendSqls.<Region>custom().andEqualTo(Region::getIsDelete, region.getIsDelete()));
			}
			if (region.getUpdateTime() != null) {
				builder.where(WeekendSqls.<Region>custom().andEqualTo(Region::getUpdateTime, region.getUpdateTime()));
			}
			if (region.getUpdateUserId() != null) {
				builder.where(WeekendSqls.<Region>custom().andEqualTo(Region::getUpdateUserId, region.getUpdateUserId()));
			}
			if (region.getDepartmentIndex() != null) {
				builder.where(WeekendSqls.<Region>custom().andEqualTo(Region::getDepartmentIndex, region.getDepartmentIndex()));
			}
			if (region.getName() != null && !"".equals(region.getName().trim())) {
				builder.where(WeekendSqls.<Region>custom().andLike(Region::getName, "%" + region.getName() + "%"));
			}
			if (region.getParentId() != null) {
				builder.where(WeekendSqls.<Region>custom().andEqualTo(Region::getParentId, region.getParentId()));
			} else {
				builder.where(WeekendSqls.<Region>custom().andIsNull(Region::getParentId));
			}
		} else {
			builder.where(WeekendSqls.<Region>custom().andIsNull(Region::getParentId));
		}
		Page<Region> all = (Page<Region>) regionMapper.selectByExample(builder.build());
		Map<String, Object> map = new HashMap<>(16);
		map.put("total", all.getTotal());
		map.put("rows", all.getResult());
		return ok(map);
	}

	@Override
	public Result getParentId(String accessToken, Integer parentId) {
		List<Region> regions = regionMapper.selectChildByParentId(parentId);
		return ok(regions);
	}

	@Override
	public Result findByOnlyAndChildren(String accessToken, Integer id) {
		List<Region> regions = regionMapper.selectSelfAndChildByParentId(id);
		return ok(regions);
	}
}

