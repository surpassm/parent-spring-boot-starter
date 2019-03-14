package com.example.demo.service.user;

import com.example.demo.entity.user.UserInfo;
import com.github.surpassm.common.jackson.Result;

/**
  * @author mc
  * Create date 2019-03-14 20:41:03
  * Version 1.0
  * Description 接口
  */
public interface UserInfoService {
    /**
	 * 新增
	 * @param userInfo 对象
	 * @return 前端返回格式
	 */
    Result insert(String accessToken, UserInfo userInfo);
    /**
	 * 修改
	 * @param userInfo 对象
	 * @return 前端返回格式
	 */
    Result update(String accessToken, UserInfo userInfo);
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
	 * @param userInfo 查询条件
	 * @return 前端返回格式
	 */
    Result pageQuery(String accessToken, Integer page, Integer size, String sort, UserInfo userInfo);
}
