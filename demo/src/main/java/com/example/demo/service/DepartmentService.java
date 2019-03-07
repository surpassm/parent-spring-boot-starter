package com.example.demo.service;


import com.example.demo.entity.Department;
import com.github.surpassm.common.jackson.Result;

/**
  * @author mc
  * Create date 2019-03-05 15:20:29
  * Version 1.0
  * Description 接口
  */
public interface DepartmentService {
    /**
	 * 新增
	 * @param department 对象
	 * @return 前端返回格式
	 */
    Result insert(String accessToken, Department department);
    /**
	 * 修改
	 * @param department 对象
	 * @return 前端返回格式
	 */
    Result update(String accessToken, Department department);
    /**
	 * 根据主键删除
	 * @param id 标识
	 * @return 前端返回格式
	 */
    Result deleteGetById(String accessToken, Integer id);
    /**
	 * 根据主键查询
	 * @param id 标识
	 * @return 前端返回格式
	 */
    Result findById(String accessToken, Integer id);
    /**
	 * 条件分页查询
	 * @param page 当前页
	 * @param size 显示多少条
	 * @param sort 排序字段
	 * @param department 查询条件
	 * @return 前端返回格式
	 */
    Result pageQuery(String accessToken, Integer page, Integer size, String sort, Department department);
}