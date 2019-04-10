package com.example.demo.mapper.user;


import com.example.demo.entity.user.Role;

/**
  * @author mc
  * Create date 2019-03-14 20:41:03
  * Version 1.0
  * Description 持久层
  */
public interface RoleMapper extends tk.mybatis.mapper.common.Mapper<Role> {

    int insertSelectiveCustom(Role role);

	/**
	 * 根据系统标识查询角色列表
	 * @param id
	 * @return
	 */
	Role findByMenus(Integer id);
}
