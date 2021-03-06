package com.liaoin.demo.repository.user;

import com.liaoin.demo.entity.user.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
  * @author mc
  * Create date 2019-04-10 12:49:52
  * Version 1.0
  * Description
  */
public interface RegionRepository extends JpaRepository<Region, Integer>, JpaSpecificationExecutor<Region> {
	/**
	 * 根据系统标识和删除状态查询详情
	 * @param regionId 标识
	 * @return 详情
	 */
	Optional<Region> findByIdAndIsDelete(Integer regionId,int i);
	/**
	 * 分页查询子级列表
	 * @param parentId 父级ID
	 * @param pageable 分页条件
	 * @return 分页结果
	 */
	Page<Region> findByParent_Id(Integer parentId, Pageable pageable);
}