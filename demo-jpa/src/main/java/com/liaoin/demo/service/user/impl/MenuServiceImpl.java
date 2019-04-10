package com.liaoin.demo.service.user.impl;

import com.github.surpassm.common.jackson.Result;
import com.github.surpassm.common.jackson.Tips;
import com.liaoin.demo.entity.user.Menu;
import com.liaoin.demo.entity.user.UserInfo;
import com.liaoin.demo.repository.user.MenuRepository;
import com.liaoin.demo.security.BeanConfig;
import com.liaoin.demo.service.user.MenuService;
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
public class MenuServiceImpl implements MenuService {
    @Resource
    private MenuRepository menuRepository;
    @Resource
	private BeanConfig beanConfig;

    @Override
    public Result insert(String accessToken, Menu menu) {

        if (!Optional.ofNullable(menu).isPresent()){
            return fail(Tips.PARAMETER_ERROR.msg);
        }
        UserInfo loginUser = beanConfig.getAccessToken(accessToken);
        menu.setCreateTime(LocalDateTime.now());
        menu.setCreateUserId(loginUser.getId());
        menu.setIsDelete(0);
        menuRepository.save(menu);
        return ok();
    }

    @Override
    public Result update(String accessToken, Menu menu) {
        if (!Optional.ofNullable(menu).isPresent()){
            return fail(Tips.PARAMETER_ERROR.msg);
        }
        UserInfo loginUser = beanConfig.getAccessToken(accessToken);
        menu.setUpdateTime(LocalDateTime.now());
        menu.setUpdateUserId(loginUser.getId());
        menuRepository.save(menu);
        return ok();
    }


    @Override
    public Result deleteGetById(String accessToken, Integer id){
        if (!Optional.ofNullable(id).isPresent()){
            return fail(Tips.PARAMETER_ERROR.msg);
        }
        UserInfo loginUser = beanConfig.getAccessToken(accessToken);
        Optional<Menu> optional = menuRepository.findById(id);
        if(!optional.isPresent()){
            return fail(Tips.MSG_NOT.msg);
        }
        Menu menu = optional.get();
        menu.setIsDelete(1);
        menu.setDeleteTime(LocalDateTime.now());
        menu.setDeleteUserId(loginUser.getId());
        return ok();
    }


    @Override
    public Result findById(String accessToken, Integer id) {
        if (id == null){
            return fail(Tips.PARAMETER_ERROR.msg);
        }
        UserInfo loginUser = beanConfig.getAccessToken(accessToken);
		Optional<Menu> optional = menuRepository.findById(id);
        return optional.map(Result::ok).orElseGet(() -> fail(Tips.MSG_NOT.msg));
    }

    @Override
    public Result pageQuery(String accessToken, Integer page, Integer size, String sort, Menu menu) {
        page = page == null ? 0 : page;
		size = size == null ? 10 : size;
    	if (page>0){page--;}
        PageRequest pageable = PageRequest.of(page, size);
        if (sort !=null && "".equals(sort.trim())) {
            pageable= PageRequest.of(page,page,new Sort(Sort.Direction.DESC,sort));
        }
        Page<Menu> all = menuRepository.findAll((root, criteriaQuery, criteriaBuilder)-> {
            List<Predicate> list = new ArrayList<>();
            if (menu != null) {
                if (menu.getId() != null) {
                    list.add(criteriaBuilder.equal(root.get("id").as(Integer.class), menu.getId()));
                }
                if (menu.getCreateTime() != null) {
                    list.add(criteriaBuilder.equal(root.get("createTime").as(Date.class), menu.getCreateTime()));
                }
                if (menu.getCreateUserId() != null) {
                    list.add(criteriaBuilder.equal(root.get("createUserId").as(Integer.class), menu.getCreateUserId()));
                }
                if (menu.getDeleteTime() != null) {
                    list.add(criteriaBuilder.equal(root.get("deleteTime").as(Date.class), menu.getDeleteTime()));
                }
                if (menu.getDeleteUserId() != null) {
                    list.add(criteriaBuilder.equal(root.get("deleteUserId").as(Integer.class), menu.getDeleteUserId()));
                }
                if (menu.getIsDelete() != null) {
                    list.add(criteriaBuilder.equal(root.get("isDelete").as(Integer.class), menu.getIsDelete()));
                }
                if (menu.getUpdateTime() != null) {
                    list.add(criteriaBuilder.equal(root.get("updateTime").as(Date.class), menu.getUpdateTime()));
                }
                if (menu.getUpdateUserId() != null) {
                    list.add(criteriaBuilder.equal(root.get("updateUserId").as(Integer.class), menu.getUpdateUserId()));
                }
                if ((menu.getDescribes() != null) && !"".equals(menu.getDescribes().trim())) {
                    list.add(criteriaBuilder.like(root.get("describes").as(String.class), "%" + menu.getDescribes() + "%"));
                }
                if ((menu.getMenuIcon() != null) && !"".equals(menu.getMenuIcon().trim())) {
                    list.add(criteriaBuilder.like(root.get("menuIcon").as(String.class), "%" + menu.getMenuIcon() + "%"));
                }
                if (menu.getMenuIndex() != null) {
                    list.add(criteriaBuilder.equal(root.get("menuIndex").as(Integer.class), menu.getMenuIndex()));
                }
                if ((menu.getMenuUrl() != null) && !"".equals(menu.getMenuUrl().trim())) {
                    list.add(criteriaBuilder.like(root.get("menuUrl").as(String.class), "%" + menu.getMenuUrl() + "%"));
                }
                if ((menu.getName() != null) && !"".equals(menu.getName().trim())) {
                    list.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + menu.getName() + "%"));
                }
                if ((menu.getPath() != null) && !"".equals(menu.getPath().trim())) {
                    list.add(criteriaBuilder.like(root.get("path").as(String.class), "%" + menu.getPath() + "%"));
                }
                if (menu.getType() != null) {
                    list.add(criteriaBuilder.equal(root.get("type").as(Integer.class), menu.getType()));
                }
                if (menu.getParent() != null) {
                    list.add(criteriaBuilder.equal(root.get("parent").get("id").as(Integer.class), menu.getParent().getId()));
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

