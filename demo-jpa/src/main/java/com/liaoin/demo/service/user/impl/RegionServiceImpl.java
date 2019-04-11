package com.liaoin.demo.service.user.impl;

import com.github.surpassm.common.jackson.Result;
import com.github.surpassm.common.jackson.Tips;
import com.liaoin.demo.entity.user.Group;
import com.liaoin.demo.entity.user.Region;
import com.liaoin.demo.entity.user.UserInfo;
import com.liaoin.demo.repository.user.RegionRepository;
import com.liaoin.demo.security.BeanConfig;
import com.liaoin.demo.service.user.RegionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.*;

import static com.github.surpassm.common.jackson.Result.fail;
import static com.github.surpassm.common.jackson.Result.ok;

/**
 * @author mc
 * Create date 2019-04-10 12:49:52
 * Version 1.0
 * Description 区域实现类
 */
@Slf4j
@Service
@Transactional(rollbackFor = {RuntimeException.class, Exception.class})
public class RegionServiceImpl implements RegionService {
	@Resource
	private RegionRepository regionRepository;
	@Resource
	private BeanConfig beanConfig;

	@Override
	public Result insert(String accessToken, Region region) {

		if (!Optional.ofNullable(region).isPresent()) {
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		UserInfo loginUser = beanConfig.getAccessToken(accessToken);
		if (getParentId(region)) {
			return fail(Tips.MSG_NOT.msg);
		}
		region.setCreateTime(LocalDateTime.now());
		region.setCreateUserId(loginUser.getId());
		region.setIsDelete(0);
		regionRepository.save(region);
		return ok();
	}

	@Override
	public Result update(String accessToken, Region region) {
		if (!Optional.ofNullable(region).isPresent()) {
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		UserInfo loginUser = beanConfig.getAccessToken(accessToken);
		if (getParentId(region)) {
			return fail(Tips.MSG_NOT.msg);
		}
		region.setUpdateTime(LocalDateTime.now());
		region.setUpdateUserId(loginUser.getId());
		regionRepository.save(region);
		return ok();
	}

	private boolean getParentId(Region region) {
		if (region.getParentId() != null && region.getParentId() != 0) {
			Optional<Region> byId = regionRepository.findById(region.getParentId());
			if (byId.isPresent()) {
				region.setParent(byId.get());
			} else {
				return true;
			}
		}
		return false;
	}


	@Override
	public Result deleteGetById(String accessToken, Integer id) {
		if (!Optional.ofNullable(id).isPresent()) {
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		UserInfo loginUser = beanConfig.getAccessToken(accessToken);
		Optional<Region> optional = regionRepository.findById(id);
		if (!optional.isPresent()) {
			return fail(Tips.MSG_NOT.msg);
		}
		Region region = optional.get();
		if (region.getChildren() != null && region.getChildren().size() != 0) {
			return fail(Tips.childrenError.msg);
		}
		region.setIsDelete(1);
		region.setDeleteTime(LocalDateTime.now());
		region.setDeleteUserId(loginUser.getId());
		return ok();
	}


	@Override
	public Result findById(String accessToken, Integer id) {
		if (id == null) {
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		beanConfig.getAccessToken(accessToken);
		Optional<Region> optional = regionRepository.findById(id);
		if (optional.isPresent()) {
			Region region = optional.get();
			if (region.getParent() != null) {
				region.setParentId(region.getParent().getId());
			}
			return ok(region);
		} else {
			return fail(Tips.MSG_NOT.msg);
		}
	}

	@Override
	public Result pageQuery(String accessToken, Integer page, Integer size, String sort, Region region) {
		page = page == null ? 0 : page;
		size = size == null ? 10 : size;
		if (page > 0) {
			page--;
		}
		PageRequest pageable = PageRequest.of(page, size);
		if (sort != null && "".equals(sort.trim())) {
			pageable = PageRequest.of(page, page, new Sort(Sort.Direction.DESC, sort));
		}
		Page<Region> all = regionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
			List<Predicate> list = new ArrayList<>();
			if (region != null) {
				if (region.getId() != null) {
					list.add(criteriaBuilder.equal(root.get("id").as(Integer.class), region.getId()));
				}
				if (region.getCreateTime() != null) {
					list.add(criteriaBuilder.equal(root.get("createTime").as(Date.class), region.getCreateTime()));
				}
				if (region.getCreateUserId() != null) {
					list.add(criteriaBuilder.equal(root.get("createUserId").as(Integer.class), region.getCreateUserId()));
				}
				if (region.getDeleteTime() != null) {
					list.add(criteriaBuilder.equal(root.get("deleteTime").as(Date.class), region.getDeleteTime()));
				}
				if (region.getDeleteUserId() != null) {
					list.add(criteriaBuilder.equal(root.get("deleteUserId").as(Integer.class), region.getDeleteUserId()));
				}
				if (region.getUpdateTime() != null) {
					list.add(criteriaBuilder.equal(root.get("updateTime").as(Date.class), region.getUpdateTime()));
				}
				if (region.getUpdateUserId() != null) {
					list.add(criteriaBuilder.equal(root.get("updateUserId").as(Integer.class), region.getUpdateUserId()));
				}
				if (region.getDepartmentIndex() != null) {
					list.add(criteriaBuilder.equal(root.get("departmentIndex").as(Integer.class), region.getDepartmentIndex()));
				}
				if ((region.getName() != null) && !"".equals(region.getName().trim())) {
					list.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + region.getName() + "%"));
				}
			}
			list.add(criteriaBuilder.isNull(root.get("parent").get("id").as(Integer.class)));
			list.add(criteriaBuilder.equal(root.get("isDelete").as(Integer.class), 0));
			return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
		}, pageable);
		Map<String, Object> map = new HashMap<>(16);
		List<Region> result = new ArrayList<>();
		for (Region r : all.getContent()) {
			if (r.getParent() != null) {
				r.setParentId(region.getParent().getId());
			}
			result.add(r);
		}
		map.put("total", all.getTotalElements());
		map.put("rows", result);
		return Result.ok(map);
	}
	@Override
	public Result getParentId(String accessToken, Integer page, Integer size, String sort, Integer parentId) {
		beanConfig.getAccessToken(accessToken);
		page = page == null ? 0 : page;
		size = size == null ? 10 : size;
		if (page > 0) {
			page--;
		}
		PageRequest pageable = PageRequest.of(page, size);
		if (sort != null && "".equals(sort.trim())) {
			pageable = PageRequest.of(page, page, new Sort(Sort.Direction.DESC, sort));
		}
		Page<Region> all = regionRepository.findByParent_Id(parentId, pageable);
		Map<String, Object> map = new HashMap<>(16);
		map.put("total", all.getTotalElements());
		map.put("rows", all.getContent());
		return Result.ok(map);
	}
}

