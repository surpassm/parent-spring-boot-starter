package com.liaoin.demo.service.user.impl;

import com.github.surpassm.common.jackson.Result;
import com.github.surpassm.common.jackson.Tips;
import com.liaoin.demo.entity.user.Group;
import com.liaoin.demo.entity.user.UserInfo;
import com.liaoin.demo.repository.user.GroupRepository;
import com.liaoin.demo.security.BeanConfig;
import com.liaoin.demo.service.user.GroupService;
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
  * Description 权限实现类
  */
@Slf4j
@Service
@Transactional(rollbackFor={RuntimeException.class, Exception.class})
public class GroupServiceImpl implements GroupService {
    @Resource
    private GroupRepository groupRepository;
    @Resource
	private BeanConfig beanConfig;

    @Override
    public Result insert(String accessToken, Group group) {

        if (!Optional.ofNullable(group).isPresent()){
            return fail(Tips.PARAMETER_ERROR.msg);
        }
        UserInfo loginUser = beanConfig.getAccessToken(accessToken);
        group.setCreateTime(LocalDateTime.now());
        group.setCreateUserId(loginUser.getId());
        group.setIsDelete(0);
        groupRepository.save(group);
        return ok();
    }

    @Override
    public Result update(String accessToken, Group group) {
        if (!Optional.ofNullable(group).isPresent()){
            return fail(Tips.PARAMETER_ERROR.msg);
        }
        UserInfo loginUser = beanConfig.getAccessToken(accessToken);
        group.setUpdateTime(LocalDateTime.now());
        group.setUpdateUserId(loginUser.getId());
        groupRepository.save(group);
        return ok();
    }


    @Override
    public Result deleteGetById(String accessToken, Integer id){
        if (!Optional.ofNullable(id).isPresent()){
            return fail(Tips.PARAMETER_ERROR.msg);
        }
        UserInfo loginUser = beanConfig.getAccessToken(accessToken);
        Optional<Group> optional = groupRepository.findById(id);
        if(!optional.isPresent()){
            return fail(Tips.MSG_NOT.msg);
        }
        Group group = optional.get();
        group.setIsDelete(1);
        group.setDeleteTime(LocalDateTime.now());
        group.setDeleteUserId(loginUser.getId());
        return ok();
    }


    @Override
    public Result findById(String accessToken, Integer id) {
        if (id == null){
            return fail(Tips.PARAMETER_ERROR.msg);
        }
        UserInfo loginUser = beanConfig.getAccessToken(accessToken);
		Optional<Group> optional = groupRepository.findById(id);
        return optional.map(Result::ok).orElseGet(() -> fail(Tips.MSG_NOT.msg));
    }

    @Override
    public Result pageQuery(String accessToken, Integer page, Integer size, String sort, Group group) {
        page = page == null ? 0 : page;
		size = size == null ? 10 : size;
    	if (page>0){page--;}
        PageRequest pageable = PageRequest.of(page, size);
        if (sort !=null && "".equals(sort.trim())) {
            pageable= PageRequest.of(page,page,new Sort(Sort.Direction.DESC,sort));
        }
        Page<Group> all = groupRepository.findAll((root, criteriaQuery, criteriaBuilder)-> {
            List<Predicate> list = new ArrayList<>();
            if (group != null) {
                if (group.getId() != null) {
                    list.add(criteriaBuilder.equal(root.get("id").as(Integer.class), group.getId()));
                }
                if (group.getCreateTime() != null) {
                    list.add(criteriaBuilder.equal(root.get("createTime").as(Date.class), group.getCreateTime()));
                }
                if (group.getCreateUserId() != null) {
                    list.add(criteriaBuilder.equal(root.get("createUserId").as(Integer.class), group.getCreateUserId()));
                }
                if (group.getDeleteTime() != null) {
                    list.add(criteriaBuilder.equal(root.get("deleteTime").as(Date.class), group.getDeleteTime()));
                }
                if (group.getDeleteUserId() != null) {
                    list.add(criteriaBuilder.equal(root.get("deleteUserId").as(Integer.class), group.getDeleteUserId()));
                }
                if (group.getIsDelete() != null) {
                    list.add(criteriaBuilder.equal(root.get("isDelete").as(Integer.class), group.getIsDelete()));
                }
                if (group.getUpdateTime() != null) {
                    list.add(criteriaBuilder.equal(root.get("updateTime").as(Date.class), group.getUpdateTime()));
                }
                if (group.getUpdateUserId() != null) {
                    list.add(criteriaBuilder.equal(root.get("updateUserId").as(Integer.class), group.getUpdateUserId()));
                }
                if ((group.getDescribes() != null) && !"".equals(group.getDescribes().trim())) {
                    list.add(criteriaBuilder.like(root.get("describes").as(String.class), "%" + group.getDescribes() + "%"));
                }
                if ((group.getName() != null) && !"".equals(group.getName().trim())) {
                    list.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + group.getName() + "%"));
                }
                if (group.getParent() != null) {
                    list.add(criteriaBuilder.equal(root.get("parent").get("id").as(Integer.class), group.getParent().getId()));
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
