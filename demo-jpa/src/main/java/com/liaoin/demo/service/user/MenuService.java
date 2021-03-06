package com.liaoin.demo.service.user;

import com.github.surpassm.common.jackson.Result;
import com.liaoin.demo.entity.user.Menu;

/**
  * @author mc
  * Create date 2019-04-10 12:49:52
  * Version 1.0
  * Description 权限接口
  */
public interface MenuService {
    /**
	 * 新增
	 * @param menu 对象
	 * @return 前端返回格式
	 */
    Result insert(String accessToken, Menu menu);
    /**
	 * 修改
	 * @param menu 对象
	 * @return 前端返回格式
	 */
    Result update(String accessToken, Menu menu);
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
	 * @param menu 查询条件
	 * @return 前端返回格式
	 */
    Result pageQuery(String accessToken, Integer page, Integer size, String sort, Menu menu);

	/**
	 * 后台所有接口更新
	 * @param accessToken
	 * @return
	 */
	Result resourcesUpdate(String accessToken);

	/**
	 * 根据父级Id查询子级列表
	 * @param accessToken
	 * @param parentId
	 * @return
	 */
	Result getParentId(String accessToken, Integer page, Integer size, String sort, Integer parentId);
}
