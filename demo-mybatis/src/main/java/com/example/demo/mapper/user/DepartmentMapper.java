package com.example.demo.mapper.user;

import com.example.demo.entity.user.Department;

import java.util.List;

/**
  * @author mc
  * Create date 2019-03-14 20:41:03
  * Version 1.0
  * Description 部门持久层
  */
public interface DepartmentMapper extends tk.mybatis.mapper.common.Mapper<Department> {


	/**
	 * 根据父级Id查询
	 * @param parentId
	 * @return
	 */
	List<Department> selectChildByParentId(Integer parentId);

	/**
	 * 根据主键查询自己和所有子级
	 * @param id
	 * @return
	 */
	List<Department> selectSelfAndChildByParentId(Integer id);
}
