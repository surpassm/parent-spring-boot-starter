package com.liaoin.demo.service.user.impl;

import com.github.surpassm.common.jackson.Result;
import com.github.surpassm.common.jackson.Tips;
import com.github.surpassm.tool.util.ValidateUtil;
import com.liaoin.demo.entity.user.Department;
import com.liaoin.demo.entity.user.Region;
import com.liaoin.demo.entity.user.UserInfo;
import com.liaoin.demo.repository.user.DepartmentRepository;
import com.liaoin.demo.repository.user.RegionRepository;
import com.liaoin.demo.security.BeanConfig;
import com.liaoin.demo.service.user.DepartmentService;
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
 * Create date 2019-04-10 12:38:08
 * Version 1.0
 * Description 部门实现类
 */
@Slf4j
@Service
@Transactional(rollbackFor = {RuntimeException.class, Exception.class})
public class DepartmentServiceImpl implements DepartmentService {
	@Resource
	private DepartmentRepository departmentRepository;
	@Resource
	private BeanConfig beanConfig;
	@Resource
	private RegionRepository regionRepository;

	@Override
	public Result insert(String accessToken, Department department) {

		if (!Optional.ofNullable(department).isPresent()) {
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		UserInfo loginUser = beanConfig.getAccessToken(accessToken);
		//效验部门名称是否重复
		List<Department> departments = departmentRepository.findByNameLike(department.getName());
		if (departments.size() != 0){
			return fail(Tips.nameRepeat.msg);
		}
		//检验部门父级是否存在
		if (department.getParentId() != null && department.getParentId() != 0) {
			Optional<Department> byId = departmentRepository.findById(department.getParentId());
			byId.ifPresent(department::setParent);
		}
		//效验区域是否存在
		if (department.getRegionId() != null) {
			Optional<Region> byId = regionRepository.findById(department.getRegionId());
			byId.ifPresent(department::setRegion);
		}
		department.setCreateTime(LocalDateTime.now());
		department.setCreateUserId(loginUser.getId());
		department.setIsDelete(0);
		departmentRepository.save(department);
		return ok();
	}

	@Override
	public Result update(String accessToken, Department department) {
		if (!Optional.ofNullable(department).isPresent()) {
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		//效验部门名称是否重复
		List<Department> departments = departmentRepository.findByIdNotAndNameLike(department.getId(),department.getName());
		if (departments.size() != 0){
			return fail(Tips.nameRepeat.msg);
		}

		UserInfo loginUser = beanConfig.getAccessToken(accessToken);
		//检验部门父级是否存在
		if (department.getParentId() != null && department.getParentId() != 0) {
			Optional<Department> byId = departmentRepository.findById(department.getParentId());
			byId.ifPresent(department::setParent);
		}
		//效验区域是否存在
		if (department.getRegionId() != null) {
			Optional<Region> byId = regionRepository.findById(department.getRegionId());
			byId.ifPresent(department::setRegion);
		}
		department.setUpdateTime(LocalDateTime.now());
		department.setUpdateUserId(loginUser.getId());
		departmentRepository.save(department);
		return ok();
	}


	@Override
	public Result deleteGetById(String accessToken, Integer id) {
		if (!Optional.ofNullable(id).isPresent()) {
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		UserInfo loginUser = beanConfig.getAccessToken(accessToken);
		Optional<Department> optional = departmentRepository.findById(id);
		if (!optional.isPresent()) {
			return fail(Tips.MSG_NOT.msg);
		}
		if (optional.get().getChildren()!= null && optional.get().getChildren().size() != 0){
			return fail(Tips.childrenError.msg);
		}
		Department department = optional.get();
		department.setIsDelete(1);
		department.setDeleteTime(LocalDateTime.now());
		department.setDeleteUserId(loginUser.getId());
		return ok();
	}


	@Override
	public Result findById(String accessToken, Integer id) {
		if (id == null) {
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		Optional<Department> optional = departmentRepository.findById(id);
		return optional.map(Result::ok).orElseGet(() -> fail(Tips.MSG_NOT.msg));
	}

	@Override
	public Result pageQuery(String accessToken, Integer page, Integer size, String sort, Department department) {
		page = page == null ? 0 : page;
		size = size == null ? 10 : size;
		if (page > 0) {
			page--;
		}
		PageRequest pageable = PageRequest.of(page, size);
		if (sort != null && "".equals(sort.trim())) {
			pageable = PageRequest.of(page, page, new Sort(Sort.Direction.DESC, sort));
		}
		Page<Department> all = departmentRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
			List<Predicate> list = new ArrayList<>();
			if (department != null) {
				if (department.getId() != null) {
					list.add(criteriaBuilder.equal(root.get("id").as(Integer.class), department.getId()));
				}
				if (department.getCreateTime() != null) {
					list.add(criteriaBuilder.equal(root.get("createTime").as(Date.class), department.getCreateTime()));
				}
				if (department.getCreateUserId() != null) {
					list.add(criteriaBuilder.equal(root.get("createUserId").as(Integer.class), department.getCreateUserId()));
				}
				if (department.getDeleteTime() != null) {
					list.add(criteriaBuilder.equal(root.get("deleteTime").as(Date.class), department.getDeleteTime()));
				}
				if (department.getDeleteUserId() != null) {
					list.add(criteriaBuilder.equal(root.get("deleteUserId").as(Integer.class), department.getDeleteUserId()));
				}
				if (department.getUpdateTime() != null) {
					list.add(criteriaBuilder.equal(root.get("updateTime").as(Date.class), department.getUpdateTime()));
				}
				if (department.getUpdateUserId() != null) {
					list.add(criteriaBuilder.equal(root.get("updateUserId").as(Integer.class), department.getUpdateUserId()));
				}
				if (department.getDepartmentIndex() != null) {
					list.add(criteriaBuilder.equal(root.get("departmentIndex").as(Integer.class), department.getDepartmentIndex()));
				}
				if ((department.getName() != null) && !"".equals(department.getName().trim())) {
					list.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + department.getName() + "%"));
				}
				if (department.getRegion() != null) {
					list.add(criteriaBuilder.equal(root.get("region").get("id").as(Integer.class), department.getRegion().getId()));
				}
			}
			list.add(criteriaBuilder.isNull(root.get("parent").as(Department.class)));
			list.add(criteriaBuilder.equal(root.get("isDelete").as(Integer.class), 0));
			return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
		}, pageable);
		Map<String, Object> map = new HashMap<>(16);
		map.put("total", all.getTotalElements());
		map.put("rows", all.getContent());
		return Result.ok(map);
	}

	@Override
	public Result findByRegionId(String accessToken, Integer regionId) {
		Optional<Region> byId = regionRepository.findByIdAndIsDelete(regionId,0);
		if (!byId.isPresent()){
			return fail(Tips.MSG_NOT.msg);
		}
		List<Department> departments = departmentRepository.findByRegionAndIsDelete(byId.get(),0);
		return ok(departments);
	}
}

