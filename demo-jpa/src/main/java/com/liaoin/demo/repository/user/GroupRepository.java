package com.liaoin.demo.repository.user;

import com.liaoin.demo.entity.user.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
  * @author mc
  * Create date 2019-04-10 12:49:52
  * Version 1.0
  * Description
  */
public interface GroupRepository extends JpaRepository<Group, Integer>, JpaSpecificationExecutor<Group> {
	/**
	 * 根据名称模糊查询列表
	 * @param name 名称
	 * @return 列表
	 */
	List<Group> findByNameLike(String name);

	/**
	 * 根据不包含ID和名称模糊查询列表
	 * @param id 系统标识
	 * @param name 名称
	 * @return 列表
	 */
	List<Group> findByIdNotAndNameLike(Integer id, String name);
}