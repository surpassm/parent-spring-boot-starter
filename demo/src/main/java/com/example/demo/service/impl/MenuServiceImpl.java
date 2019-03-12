package com.example.demo.service.impl;

import com.example.demo.entity.Menu;
import com.example.demo.entity.UserInfo;
import com.example.demo.mapper.MenuMapper;
import com.example.demo.service.MenuService;
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
public class MenuServiceImpl implements MenuService {
    @Resource
    private MenuMapper menuMapper;
    @Resource
	private BeanConfig beanConfig;

    @Override
    public Result insert(String accessToken, Menu menu) {
        if (menu == null){
            return fail(Tips.PARAMETER_ERROR.msg);
        }
        UserInfo loginUserInfo = beanConfig.getAccessToken(accessToken);
        menu.setCreateUserId(loginUserInfo.getId());
        menu.setCreateTime(new Date());
        menu.setIsDelete(0);
        menuMapper.insertSelectiveCustom(menu);
        return ok();
    }

    @Override
    public Result update(String accessToken,Menu menu) {
        if (menu == null){
            return fail(Tips.PARAMETER_ERROR.msg);
        }
        UserInfo loginUserInfo = beanConfig.getAccessToken(accessToken);
        menu.setUpdateUserId(loginUserInfo.getId());
        menu.setUpdateTime(new Date());
        menuMapper.updateByPrimaryKeySelective(menu);
        return ok();
    }

    @Override
    public Result deleteGetById(String accessToken, Integer id){
		if (id == null){
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		Menu menu = menuMapper.selectByPrimaryKey(id);
		if(menu == null){
			return fail(Tips.MSG_NOT.msg);
		}
		int menuCount = menuMapper.selectCount(Menu.builder().parentId(id).build());
		if (menuCount != 0){
			return fail(Tips.AssociatedDataExistsAndCannotBeDeleted.msg);
		}
		int roleAndMenuCount = menuMapper.selectRoleAndMenuCount(menu.getId());
		if (roleAndMenuCount != 0){
			return fail(Tips.AssociatedDataExistsAndCannotBeDeleted.msg);
		}

		UserInfo loginUserInfo = beanConfig.getAccessToken(accessToken);
		menu.setDeleteUserId(loginUserInfo.getId());
		menu.setDeleteTime(new Date());
		menu.setIsDelete(1);
		menuMapper.updateByPrimaryKeySelective(menu);
		return ok();
    }


    @Override
    public Result findById(String accessToken,Integer id) {
        if (id == null){
            return fail(Tips.PARAMETER_ERROR.msg);
        }
		Menu menu = menuMapper.selectByPrimaryKey(id);
        if (menu == null){
			return fail(Tips.MSG_NOT.msg);
		}
        return ok(menu);

    }

    @Override
    public Result pageQuery(String accessToken,Integer page, Integer size, String sort, Menu menu) {
        page = null  == page ? 1 : page;
        size = null  == size ? 10 : size;
        PageHelper.startPage(page, size);
        Example.Builder builder = new Example.Builder(Menu.class);
        builder.where(WeekendSqls.<Menu>custom().andEqualTo(Menu::getIsDelete, 0));
        if(menu != null){
        if (menu.getId() != null){
            builder.where(WeekendSqls.<Menu>custom().andEqualTo(Menu::getId,menu.getId()));
        }
        if (menu.getCreateTime() != null){
            builder.where(WeekendSqls.<Menu>custom().andEqualTo(Menu::getCreateTime,menu.getCreateTime()));
        }
        if (menu.getCreateUserId() != null){
            builder.where(WeekendSqls.<Menu>custom().andEqualTo(Menu::getCreateUserId,menu.getCreateUserId()));
        }
        if (menu.getDeleteTime() != null){
            builder.where(WeekendSqls.<Menu>custom().andEqualTo(Menu::getDeleteTime,menu.getDeleteTime()));
        }
        if (menu.getDeleteUserId() != null){
            builder.where(WeekendSqls.<Menu>custom().andEqualTo(Menu::getDeleteUserId,menu.getDeleteUserId()));
        }
        if (menu.getDescribes() != null && !"".equals(menu.getDescribes().trim())){
            builder.where(WeekendSqls.<Menu>custom().andLike(Menu::getDescribes,"%"+menu.getDescribes()+"%"));
        }
        if (menu.getIsDelete() != null){
            builder.where(WeekendSqls.<Menu>custom().andEqualTo(Menu::getIsDelete,menu.getIsDelete()));
        }
        if (menu.getMenuIcon() != null && !"".equals(menu.getMenuIcon().trim())){
            builder.where(WeekendSqls.<Menu>custom().andLike(Menu::getMenuIcon,"%"+menu.getMenuIcon()+"%"));
        }
        if (menu.getMenuIndex() != null){
            builder.where(WeekendSqls.<Menu>custom().andEqualTo(Menu::getMenuIndex,menu.getMenuIndex()));
        }
        if (menu.getMenuUrl() != null && !"".equals(menu.getMenuUrl().trim())){
            builder.where(WeekendSqls.<Menu>custom().andLike(Menu::getMenuUrl,"%"+menu.getMenuUrl()+"%"));
        }
        if (menu.getName() != null && !"".equals(menu.getName().trim())){
            builder.where(WeekendSqls.<Menu>custom().andLike(Menu::getName,"%"+menu.getName()+"%"));
        }
        if (menu.getParentId() != null){
            builder.where(WeekendSqls.<Menu>custom().andEqualTo(Menu::getParentId,menu.getParentId()));
        }
        if (menu.getPath() != null && !"".equals(menu.getPath().trim())){
            builder.where(WeekendSqls.<Menu>custom().andLike(Menu::getPath,"%"+menu.getPath()+"%"));
        }
        if (menu.getType() != null){
            builder.where(WeekendSqls.<Menu>custom().andEqualTo(Menu::getType,menu.getType()));
        }
        if (menu.getUpdateTime() != null){
            builder.where(WeekendSqls.<Menu>custom().andEqualTo(Menu::getUpdateTime,menu.getUpdateTime()));
        }
        if (menu.getUpdateUserId() != null){
            builder.where(WeekendSqls.<Menu>custom().andEqualTo(Menu::getUpdateUserId,menu.getUpdateUserId()));
        }
        }
        Page<Menu> all = (Page<Menu>) menuMapper.selectByExample(builder.build());
        Map<String, Object> map = new HashMap<>(16);
        map.put("total",all.getTotal());
        map.put("rows",all.getResult());
        return ok(map);
    }
}

