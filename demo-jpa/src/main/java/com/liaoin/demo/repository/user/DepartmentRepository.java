package com.liaoin.demo.repository.user;

import com.liaoin.demo.entity.user.Department;
import com.liaoin.demo.entity.user.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
  * @author mc
  * Create date 2019-04-10 12:38:08
  * Version 1.0
  * Description
  */
public interface DepartmentRepository extends JpaRepository<Department, Integer>, JpaSpecificationExecutor<Department> {
	/**
	 * 根据名称模糊查询列表
	 * @param name 名称
	 * @return 列表
	 */
	List<Department> findByNameLike(String name);

	/**
	 * 根据主键和模糊名称查询列表
	 * @param id 主键
	 * @param name 名称
	 * @return 列表
	 */
	List<Department> findByIdNotAndNameLike(Integer id, String name);

	/**
	 * 根据区域和删除状态查询
	 * @param region 区域
	 * @param i 0
	 * @return 列表
	 */
	List<Department> findByRegionAndIsDelete(Region region, int i);

	/**
	 * 分页查询子级列表
	 * @param parentId 父级ID
	 * @param pageable 分页条件
	 * @return 分页结果
	 */
	Page<Department> findByParent_Id(Integer parentId, Pageable pageable);
}