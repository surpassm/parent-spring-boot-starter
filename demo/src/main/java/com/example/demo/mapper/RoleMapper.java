package com.example.demo.mapper;

import com.example.demo.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
  * @author mc
  * Create date 2019-03-05 15:20:29
  * Version 1.0
  * Description 持久层
  */
@Mapper
public interface RoleMapper extends tk.mybatis.mapper.common.Mapper<Role> {

    int insertSelectiveCustom(Role role);

	/**
	 * 增加角色对应的权限
	 * @param id
	 * @param ids
	 */
	void insertRoleMenu(@Param("id") Integer id, @Param("ids") List<Integer> ids);

	/**
	 * 根据角色ID删除权限
	 * @param id
	 */
	void deleteByRoleAndMenu(Integer id);

	/**
	 * 根据主键列表查询角色
	 * @param roleList
	 * @return
	 */
	List<Role> selectByPrimaryKeys(@Param("roleList") List<Role> roleList);
}
