package com.example.demo.mapper.user;

import com.example.demo.entity.user.Region;

import java.util.List;

/**
  * @author mc
  * Create date 2019-03-14 20:41:03
  * Version 1.0
  * Description 区域持久层
  */
public interface RegionMapper extends tk.mybatis.mapper.common.Mapper<Region> {

	/**
	 * 根据父级Id查询
	 * @param parentId
	 * @return
	 */
	List<Region> selectChildByParentId(Integer parentId);
	/**
	 * 根据主键查询自己和所有子级
	 * @param id
	 * @return
	 */
	List<Region> selectSelfAndChildByParentId(Integer id);
}
