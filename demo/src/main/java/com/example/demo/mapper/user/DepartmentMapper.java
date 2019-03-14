package com.example.demo.mapper.user;

import com.example.demo.entity.user.Department;

/**
  * @author mc
  * Create date 2019-03-14 20:41:03
  * Version 1.0
  * Description 部门持久层
  */
public interface DepartmentMapper extends tk.mybatis.mapper.common.Mapper<Department> {

    int insertSelectiveCustom(Department department);
}
