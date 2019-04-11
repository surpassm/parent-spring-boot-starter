package com.liaoin.demo.service.user;

import com.github.surpassm.common.jackson.Result;
import com.liaoin.demo.entity.user.Group;

/**
  * @author mc
  * Create date 2019-04-10 12:49:52
  * Version 1.0
  * Description 权限接口
  */
public interface GroupService {
    /**
	 * 新增
	 * @param group 对象
	 * @return 前端返回格式
	 */
    Result insert(String accessToken, Group group);
    /**
	 * 修改
	 * @param group 对象
	 * @return 前端返回格式
	 */
    Result update(String accessToken, Group group);
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
	 * @param group 查询条件
	 * @return 前端返回格式
	 */
    Result pageQuery(String accessToken, Integer page, Integer size, String sort, Group group);

	/**
	 * 设置组的权限
	 * @param accessToken
	 * @param id
	 * @param menuId
	 * @return
	 */
	Result setGroupByMenu(String accessToken, Integer id, String menuId);

	/**
	 * 设置组的角色
	 * @param accessToken
	 * @param id
	 * @param roleIds
	 * @return
	 */
	Result setGroupByRole(String accessToken, Integer id, String roleIds);

	/**
	 * 根据父级Id查询子级列表
	 * @param accessToken
	 * @param page
	 * @param size
	 * @param sort
	 * @param parentId
	 * @return
	 */
	Result getParentId(String accessToken, Integer page, Integer size, String sort, Integer parentId);
}
