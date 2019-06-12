package com.liaoin.demo.service.user.impl;

import com.github.surpassm.common.jackson.Result;
import com.github.surpassm.common.jackson.Tips;
import com.liaoin.demo.entity.user.Department;
import com.liaoin.demo.entity.user.Menu;
import com.liaoin.demo.entity.user.UserInfo;
import com.liaoin.demo.repository.user.MenuRepository;
import com.liaoin.demo.security.BeanConfig;
import com.liaoin.demo.service.user.MenuService;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.service.Documentation;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;

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
	@Resource
	private ServiceModelToSwagger2Mapper mapper;
	@Resource
	private DocumentationCache documentationCache;

    @Override
    public Result insert(String accessToken, Menu menu) {

        if (!Optional.ofNullable(menu).isPresent()){
            return fail(Tips.PARAMETER_ERROR.msg);
        }
        UserInfo loginUser = beanConfig.getAccessToken(accessToken);
        if (menu.getParentId() != null){
			Optional<Menu> byId = menuRepository.findById(menu.getParentId());
			byId.ifPresent(menu::setParent);
		}
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
		if (menu.getParentId() != null){
			Optional<Menu> byId = menuRepository.findById(menu.getParentId());
			byId.ifPresent(menu::setParent);
		}
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
        return ok();
    }


    @Override
    public Result findById(String accessToken, Integer id) {
        if (id == null){
            return fail(Tips.PARAMETER_ERROR.msg);
        }
        beanConfig.getAccessToken(accessToken);
		Optional<Menu> optional = menuRepository.findById(id);
		if (optional.isPresent()){
			Menu menu = optional.get();
			if (menu.getParent()!= null){
				menu.setParentId(menu.getParent().getId());
			}
			return ok(menu);
		}else {
			return fail(Tips.MSG_NOT.msg);
		}
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
            }
			list.add(criteriaBuilder.isNull(root.get("parent").get("id").as(Integer.class)));
			list.add(criteriaBuilder.equal(root.get("isDelete").as(Integer.class), 0));
            return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
        }, pageable);
        Map<String, Object> map = new HashMap<>(16);
        map.put("total",all.getTotalElements());
        map.put("rows",all.getContent());
        return Result.ok(map);
    }

	@Override
	public Result resourcesUpdate(String accessToken) {
		UserInfo loginUser = beanConfig.getAccessToken(accessToken);
		String[] groupName = new String[]{"user","common","mobile"};
		for (String group:groupName ){
			Documentation documentation = documentationCache.documentationByGroup(group);
			if (documentation == null) {
				return Result.fail("导入失败");
			}
			Swagger swagger = mapper.mapDocumentation(documentation);
			Map<String, Path> paths = swagger.getPaths();
			paths.forEach((key, value) -> {
				// 链接
				String url = key + "**";
				// 名称
				if (value.getPost() != null){
					String name = value.getPost().getSummary();
					// 描述
					String description = value.getPost().getTags().get(0);
					// 权限
					Menu menu = menuRepository.findByNameAndIsDelete(description,0);
					menuInsertAndUpdata(url, name, description, menu,loginUser.getId());
				}
				if (value.getGet() != null){
					String name = value.getGet().getSummary();
					// 描述
					String description = value.getGet().getTags().get(0);
					// 权限
					Menu menu = menuRepository.findByNameAndIsDelete(description,0);
					menuInsertAndUpdata(url, name, description, menu,loginUser.getId());
				}
			});
		}
		return Result.ok();
	}



	private void menuInsertAndUpdata(String url, String name, String description, Menu menu,Integer loginUserId) {
		if (menu == null){
			//新增父级
			Menu parentMenu = Menu.builder()
					.name(description)
					.type(1)
					.build();
			parentMenu.setIsDelete(0);
			menuRepository.save(parentMenu);
			//在添加当前url为子级
			Menu menuBuild = Menu.builder()
					.menuUrl(url)
					.parent(parentMenu)
					.describes(name)
					.type(1)
					.build();
			menuBuild.setIsDelete(0);
			menuRepository.save(menuBuild);
		}else {
			Menu build = Menu.builder().menuUrl(url).build();
			build.setIsDelete(0);
			List<Menu> selectCount = menuRepository.findByMenuUrlAndIsDelete(url,0);
			if (selectCount.size() == 0){
				//去除重复数据
				Menu menuBuild = Menu.builder()
						.menuUrl(url)
						.type(1)
						.parent(menu)
						.describes(name)
						.build();
				menuBuild.setIsDelete(0);
				menuRepository.save(menuBuild);
			}
		}
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
		Page<Menu> all = menuRepository.findByParent_Id(parentId, pageable);
		Map<String, Object> map = new HashMap<>(16);
		map.put("total", all.getTotalElements());
		map.put("rows", all.getContent());
		return Result.ok(map);
	}
}

