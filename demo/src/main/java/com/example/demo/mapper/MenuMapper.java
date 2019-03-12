package com.example.demo.mapper;

import com.example.demo.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
  * @author mc
  * Create date 2019-03-05 15:20:29
  * Version 1.0
  * Description 持久层
  */
@Mapper
public interface MenuMapper extends tk.mybatis.mapper.common.Mapper<Menu> {

    int insertSelectiveCustom(Menu menu);

	/**
	 * 根据集合主键查询权限列表
	 * @param menuIds
	 * @return
	 */
	List<Menu> selectListIds(List<Integer> menuIds);

	/**
	 * 根据角色ID查询权限列表
	 * @param id
	 * @return
	 */
	List<Menu> selectMenuList(Integer id);

	List<Menu> selectByMenuList(List<Menu> menuList);

	int selectRoleAndMenuCount(Integer id);
}
