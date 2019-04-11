package com.liaoin.demo.service.user;

import com.github.surpassm.common.jackson.Result;
import com.liaoin.demo.entity.user.Department;

/**
  * @author mc
  * Create date 2019-04-10 12:38:08
  * Version 1.0
  * Description 部门接口
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

	/**
	 * 根据区域主键查询部门详情
	 * @param accessToken
	 * @param regionId
	 * @return
	 */
	Result findByRegionId(String accessToken, Integer regionId);

	/**
	 * 根据父级Id查询子级列表
	 * @param accessToken
	 * @param parentId
	 * @return
	 */
	Result getParentId(String accessToken, Integer page, Integer size, String sort, Integer parentId);
}
