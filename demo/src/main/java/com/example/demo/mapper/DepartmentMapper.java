package com.example.demo.mapper;

import com.example.demo.entity.Department;
import org.apache.ibatis.annotations.Mapper;

/**
  * @author mc
  * Create date 2019-03-05 15:20:29
  * Version 1.0
  * Description 持久层
  */
@Mapper
public interface DepartmentMapper extends tk.mybatis.mapper.common.Mapper<Department> {

    int insertSelectiveCustom(Department department);
}
