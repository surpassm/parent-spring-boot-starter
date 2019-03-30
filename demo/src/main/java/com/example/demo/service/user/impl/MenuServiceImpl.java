package com.example.demo.service.user.impl;

import com.example.demo.entity.user.*;
import com.example.demo.mapper.user.GroupMenuMapper;
import com.example.demo.mapper.user.MenuMapper;
import com.example.demo.mapper.user.RoleMenuMapper;
import com.example.demo.mapper.user.UserMenuMapper;
import com.example.demo.security.BeanConfig;
import com.example.demo.service.user.MenuService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.surpassm.common.jackson.Result;
import com.github.surpassm.common.jackson.Tips;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.service.Documentation;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

import static com.example.demo.service.user.impl.CommonImpl.groupMenuDeleteUpdata;
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
@Transactional(rollbackFor = {RuntimeException.class, Exception.class})
public class MenuServiceImpl implements MenuService {
	@Resource
	private MenuMapper menuMapper;
	@Resource
	private BeanConfig beanConfig;
	@Resource
	private GroupMenuMapper groupMenuMapper;
	@Resource
	private RoleMenuMapper roleMenuMapper;
	@Resource
	private UserMenuMapper userMenuMapper;
	@Resource
	private ServiceModelToSwagger2Mapper mapper;
	@Resource
	private DocumentationCache documentationCache;


	@Override
	public Result insert(String accessToken, Menu menu) {
		if (menu == null) {
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		UserInfo loginUserInfo = beanConfig.getAccessToken(accessToken);

		//效验名称是否重复
		Menu build = Menu.builder().name(menu.getName()).build();
		build.setIsDelete(0);
		int groupCount = menuMapper.selectCount(build);
		if (groupCount != 0) {
			return fail(Tips.nameRepeat.msg);
		}
		//查看父级是否存在
		if (isEnableParent(menu)) {
			return fail(Tips.parentError.msg);
		}

		menu.setCreateUserId(loginUserInfo.getId());
		menu.setCreateTime(LocalDateTime.now());
		menu.setIsDelete(0);
		menuMapper.insertSelectiveCustom(menu);
		return ok();
	}

	@Override
	public Result update(String accessToken, Menu menu) {
		if (menu == null) {
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		if (menu.getIsDelete() == 1){
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		UserInfo loginUserInfo = beanConfig.getAccessToken(accessToken);

		Example.Builder builder = new Example.Builder(Menu.class);
		builder.where(WeekendSqls.<Menu>custom().andEqualTo(Menu::getIsDelete, 0));
		builder.where(WeekendSqls.<Menu>custom().andEqualTo(Menu::getName, menu.getName()));
		builder.where(WeekendSqls.<Menu>custom().andNotIn(Menu::getId, Collections.singletonList(menu.getId())));

		List<Menu> selectCount = menuMapper.selectByExample(builder.build());
		if (selectCount.size() != 0) {
			return fail(Tips.nameRepeat.msg);
		}
		if (isEnableParent(menu)) {
			return fail(Tips.parentError.msg);
		}


		menu.setUpdateUserId(loginUserInfo.getId());
		menu.setUpdateTime(LocalDateTime.now());
		menuMapper.updateByPrimaryKeySelective(menu);
		return ok();
	}

	private boolean isEnableParent(Menu menu) {
		if (menu.getParentId() != null) {
			Menu buildMenu = Menu.builder().id(menu.getParentId()).build();
			buildMenu.setIsDelete(0);
			int buildmenuMapperCount = menuMapper.selectCount(buildMenu);
			return buildmenuMapperCount == 0;
		}
		return false;
	}

	@Override
	public Result deleteGetById(String accessToken, Integer id) {
		if (id == null) {
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		UserInfo loginUserInfo = beanConfig.getAccessToken(accessToken);
		Menu menu = menuMapper.selectByPrimaryKey(id);
		if (menu == null) {
			return fail(Tips.MSG_NOT.msg);
		}
		Menu menuBuild = Menu.builder().parentId(id).build();
		menuBuild.setIsDelete(0);
		int menuCount = menuMapper.selectCount(menuBuild);
		if (menuCount != 0){
			return fail("存在下级关联数据无法删除");
		}
		
		//组权限查询
		GroupMenu groupMenu = GroupMenu.builder().menuId(id).build();
		groupMenu.setIsDelete(0);
		int groupMenuCount = groupMenuMapper.selectCount(groupMenu);
		groupMenuDeleteUpdata(loginUserInfo,groupMenu,groupMenuCount,groupMenuMapper);
		//角色权限查询
		RoleMenu roleMenu = RoleMenu.builder().menuId(id).build();
		roleMenu.setIsDelete(0);
		int roleMenuCount = roleMenuMapper.selectCount(roleMenu);
		CommonImpl.roleMenuDeleteUpdata(loginUserInfo, roleMenu, roleMenuCount, roleMenuMapper);
		//用户权限查询
		UserMenu userMenu = UserMenu.builder().menuId(id).build();
		userMenu.setIsDelete(0);
		int userMenuCount = userMenuMapper.selectCount(userMenu);
		CommonImpl.userMenuDeleteUpdata(loginUserInfo, userMenu, userMenuCount, userMenuMapper);

		menu.setDeleteUserId(loginUserInfo.getId());
		menu.setDeleteTime(LocalDateTime.now());
		menu.setIsDelete(1);
		menuMapper.updateByPrimaryKeySelective(menu);
		return ok();
	}


	@Override
	public Result findById(String accessToken, Integer id) {
		if (id == null) {
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		Menu menu = menuMapper.selectByPrimaryKey(id);
		if (menu == null) {
			return fail(Tips.MSG_NOT.msg);
		}
		return ok(menu);

	}

	@Override
	public Result pageQuery(String accessToken, Integer page, Integer size, String sort, Menu menu) {
		page = null == page ? 1 : page;
		size = null == size ? 10 : size;
		PageHelper.startPage(page, size);
		Example.Builder builder = new Example.Builder(Menu.class);
		builder.where(WeekendSqls.<Menu>custom().andEqualTo(Menu::getIsDelete, 0));
		if (menu != null) {
			if (menu.getId() != null) {
				builder.where(WeekendSqls.<Menu>custom().andEqualTo(Menu::getId, menu.getId()));
			}
			if (menu.getCreateTime() != null) {
				builder.where(WeekendSqls.<Menu>custom().andEqualTo(Menu::getCreateTime, menu.getCreateTime()));
			}
			if (menu.getCreateUserId() != null) {
				builder.where(WeekendSqls.<Menu>custom().andEqualTo(Menu::getCreateUserId, menu.getCreateUserId()));
			}
			if (menu.getDeleteTime() != null) {
				builder.where(WeekendSqls.<Menu>custom().andEqualTo(Menu::getDeleteTime, menu.getDeleteTime()));
			}
			if (menu.getDeleteUserId() != null) {
				builder.where(WeekendSqls.<Menu>custom().andEqualTo(Menu::getDeleteUserId, menu.getDeleteUserId()));
			}
			if (menu.getIsDelete() != null) {
				builder.where(WeekendSqls.<Menu>custom().andEqualTo(Menu::getIsDelete, menu.getIsDelete()));
			}
			if (menu.getUpdateTime() != null) {
				builder.where(WeekendSqls.<Menu>custom().andEqualTo(Menu::getUpdateTime, menu.getUpdateTime()));
			}
			if (menu.getUpdateUserId() != null) {
				builder.where(WeekendSqls.<Menu>custom().andEqualTo(Menu::getUpdateUserId, menu.getUpdateUserId()));
			}
			if (menu.getDescribes() != null && !"".equals(menu.getDescribes().trim())) {
				builder.where(WeekendSqls.<Menu>custom().andLike(Menu::getDescribes, "%" + menu.getDescribes() + "%"));
			}
			if (menu.getMenuIcon() != null && !"".equals(menu.getMenuIcon().trim())) {
				builder.where(WeekendSqls.<Menu>custom().andLike(Menu::getMenuIcon, "%" + menu.getMenuIcon() + "%"));
			}
			if (menu.getMenuIndex() != null) {
				builder.where(WeekendSqls.<Menu>custom().andEqualTo(Menu::getMenuIndex, menu.getMenuIndex()));
			}
			if (menu.getMenuUrl() != null && !"".equals(menu.getMenuUrl().trim())) {
				builder.where(WeekendSqls.<Menu>custom().andLike(Menu::getMenuUrl, "%" + menu.getMenuUrl() + "%"));
			}
			if (menu.getName() != null && !"".equals(menu.getName().trim())) {
				builder.where(WeekendSqls.<Menu>custom().andLike(Menu::getName, "%" + menu.getName() + "%"));
			}
			if (menu.getParentId() != null) {
				builder.where(WeekendSqls.<Menu>custom().andEqualTo(Menu::getParentId, menu.getParentId()));
			}
			if (menu.getPath() != null && !"".equals(menu.getPath().trim())) {
				builder.where(WeekendSqls.<Menu>custom().andLike(Menu::getPath, "%" + menu.getPath() + "%"));
			}
			if (menu.getType() != null) {
				builder.where(WeekendSqls.<Menu>custom().andEqualTo(Menu::getType, menu.getType()));
			} else {
				builder.where(WeekendSqls.<Menu>custom().andIsNull(Menu::getParentId));
			}
		} else {
			builder.where(WeekendSqls.<Menu>custom().andIsNull(Menu::getParentId));
		}
		Page<Menu> all = (Page<Menu>) menuMapper.selectByExample(builder.build());
		Map<String, Object> map = new HashMap<>(16);
		map.put("total", all.getTotal());
		map.put("rows", all.getResult());
		return ok(map);
	}

	@Override
	public Result getParentId(String accessToken, Integer parentId) {
		List<Menu> menus = menuMapper.selectChildByParentId(parentId);
		return ok(menus);
	}

	@Override
	public Result findByOnlyAndChildren(String accessToken, Integer id) {
		List<Menu> menus = menuMapper.selectSelfAndChildByParentId(id);
		return ok(menus);
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
					Menu build = Menu.builder().name(description).build();
					build.setIsDelete(0);
					Menu menu = menuMapper.selectOne(build);
					menuInsertAndUpdata(url, name, description, menu,loginUser.getId());
				}
				if (value.getGet() != null){
					String name = value.getGet().getSummary();
					// 描述
					String description = value.getGet().getTags().get(0);
					// 权限
					Menu build = Menu.builder().name(description).build();
					build.setIsDelete(0);
					Menu menu = menuMapper.selectOne(build);
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
			parentMenu.setCreateTime(LocalDateTime.now());
			parentMenu.setIsDelete(0);
			menuMapper.insertSelectiveCustom(parentMenu);
			//在添加当前url为子级
			Menu menuBuild = Menu.builder()
					.menuUrl(url)
					.parentId(parentMenu.getId())
					.describes(name)
					.type(1)
					.build();
			menuBuild.setCreateTime(LocalDateTime.now());
			menuBuild.setIsDelete(0);
			menuBuild.setCreateTime(LocalDateTime.now());
			menuBuild.setCreateUserId(loginUserId);
			menuMapper.insertSelectiveCustom(menuBuild);
		}else {
			Menu build = Menu.builder().menuUrl(url).build();
			build.setIsDelete(0);
			int selectCount = menuMapper.selectCount(build);
			if (selectCount == 0){
				//去除重复数据
				Menu menuBuild = Menu.builder()
						.menuUrl(url)
						.type(1)
						.parentId(menu.getId())
						.describes(name)
						.build();
				menuBuild.setCreateTime(LocalDateTime.now());
				menuBuild.setIsDelete(0);
				menuBuild.setCreateTime(LocalDateTime.now());
				menuBuild.setCreateUserId(loginUserId);
				menuMapper.insertSelectiveCustom(menuBuild);
			}
		}
	}
}

