package com.example.demo.mapper.user;

import com.example.demo.entity.user.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
  * @author mc
  * Create date 2019-03-14 20:41:03
  * Version 1.0
  * Description 权限持久层
  */
public interface MenuMapper extends tk.mybatis.mapper.common.Mapper<Menu> {

    int insertSelectiveCustom(@Param("menu")Menu menu);
	/**
	 * 根据父级Id查询
	 * @param parentId
	 * @return
	 */
	List<Menu> selectChildByParentId(@Param("parentId")Integer parentId);
	/**
	 * 根据主键查询自己和所有子级
	 * @param id
	 * @return
	 */
	List<Menu> selectSelfAndChildByParentId(@Param("id") Integer id);
}
