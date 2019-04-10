package com.liaoin.demo.service.user.impl;

import com.github.surpassm.common.jackson.Result;
import com.github.surpassm.common.jackson.Tips;
import com.liaoin.demo.entity.user.Role;
import com.liaoin.demo.entity.user.UserInfo;
import com.liaoin.demo.repository.user.RoleRepository;
import com.liaoin.demo.security.BeanConfig;
import com.liaoin.demo.service.user.RoleService;
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
  * Description 角色实现类
  */
@Slf4j
@Service
@Transactional(rollbackFor={RuntimeException.class, Exception.class})
public class RoleServiceImpl implements RoleService {
    @Resource
    private RoleRepository roleRepository;
    @Resource
	private BeanConfig beanConfig;

    @Override
    public Result insert(String accessToken, Role role) {

        if (!Optional.ofNullable(role).isPresent()){
            return fail(Tips.PARAMETER_ERROR.msg);
        }
        UserInfo loginUser = beanConfig.getAccessToken(accessToken);
        role.setCreateTime(LocalDateTime.now());
        role.setCreateUserId(loginUser.getId());
        role.setIsDelete(0);
        roleRepository.save(role);
        return ok();
    }

    @Override
    public Result update(String accessToken, Role role) {
        if (!Optional.ofNullable(role).isPresent()){
            return fail(Tips.PARAMETER_ERROR.msg);
        }
        UserInfo loginUser = beanConfig.getAccessToken(accessToken);
        role.setUpdateTime(LocalDateTime.now());
        role.setUpdateUserId(loginUser.getId());
        roleRepository.save(role);
        return ok();
    }


    @Override
    public Result deleteGetById(String accessToken, Integer id){
        if (!Optional.ofNullable(id).isPresent()){
            return fail(Tips.PARAMETER_ERROR.msg);
        }
        UserInfo loginUser = beanConfig.getAccessToken(accessToken);
        Optional<Role> optional = roleRepository.findById(id);
        if(!optional.isPresent()){
            return fail(Tips.MSG_NOT.msg);
        }
        Role role = optional.get();
        role.setIsDelete(1);
        role.setDeleteTime(LocalDateTime.now());
        role.setDeleteUserId(loginUser.getId());
        return ok();
    }


    @Override
    public Result findById(String accessToken, Integer id) {
        if (id == null){
            return fail(Tips.PARAMETER_ERROR.msg);
        }
        UserInfo loginUser = beanConfig.getAccessToken(accessToken);
		Optional<Role> optional = roleRepository.findById(id);
        return optional.map(Result::ok).orElseGet(() -> fail(Tips.MSG_NOT.msg));
    }

    @Override
    public Result pageQuery(String accessToken, Integer page, Integer size, String sort, Role role) {
        page = page == null ? 0 : page;
		size = size == null ? 10 : size;
    	if (page>0){page--;}
        PageRequest pageable = PageRequest.of(page, size);
        if (sort !=null && "".equals(sort.trim())) {
            pageable= PageRequest.of(page,page,new Sort(Sort.Direction.DESC,sort));
        }
        Page<Role> all = roleRepository.findAll((root, criteriaQuery, criteriaBuilder)-> {
            List<Predicate> list = new ArrayList<>();
            if (role != null) {
                if (role.getId() != null) {
                    list.add(criteriaBuilder.equal(root.get("id").as(Integer.class), role.getId()));
                }
                if (role.getCreateTime() != null) {
                    list.add(criteriaBuilder.equal(root.get("createTime").as(Date.class), role.getCreateTime()));
                }
                if (role.getCreateUserId() != null) {
                    list.add(criteriaBuilder.equal(root.get("createUserId").as(Integer.class), role.getCreateUserId()));
                }
                if (role.getDeleteTime() != null) {
                    list.add(criteriaBuilder.equal(root.get("deleteTime").as(Date.class), role.getDeleteTime()));
                }
                if (role.getDeleteUserId() != null) {
                    list.add(criteriaBuilder.equal(root.get("deleteUserId").as(Integer.class), role.getDeleteUserId()));
                }
                if (role.getIsDelete() != null) {
                    list.add(criteriaBuilder.equal(root.get("isDelete").as(Integer.class), role.getIsDelete()));
                }
                if (role.getUpdateTime() != null) {
                    list.add(criteriaBuilder.equal(root.get("updateTime").as(Date.class), role.getUpdateTime()));
                }
                if (role.getUpdateUserId() != null) {
                    list.add(criteriaBuilder.equal(root.get("updateUserId").as(Integer.class), role.getUpdateUserId()));
                }
                if ((role.getDescribes() != null) && !"".equals(role.getDescribes().trim())) {
                    list.add(criteriaBuilder.like(root.get("describes").as(String.class), "%" + role.getDescribes() + "%"));
                }
                if ((role.getName() != null) && !"".equals(role.getName().trim())) {
                    list.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + role.getName() + "%"));
                }
                if (role.getRoleIndex() != null) {
                    list.add(criteriaBuilder.equal(root.get("roleIndex").as(Integer.class), role.getRoleIndex()));
                }
                if ((role.getSecurityRoles() != null) && !"".equals(role.getSecurityRoles().trim())) {
                    list.add(criteriaBuilder.like(root.get("securityRoles").as(String.class), "%" + role.getSecurityRoles() + "%"));
                }
            }
            return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
        }, pageable);
        Map<String, Object> map = new HashMap<>(16);
        map.put("total",all.getTotalElements());
        map.put("rows",all.getContent());
        return Result.ok(map);
    }
}
