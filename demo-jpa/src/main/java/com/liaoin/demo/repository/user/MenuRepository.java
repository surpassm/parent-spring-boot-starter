package com.liaoin.demo.repository.user;

import com.liaoin.demo.entity.user.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Set;


/**
  * @author mc
  * Create date 2019-04-10 12:49:52
  * Version 1.0
  * Description
  */
public interface MenuRepository extends JpaRepository<Menu, Integer>, JpaSpecificationExecutor<Menu> {
	/**
	 * 根据ID列表查询
	 * @param menuIds 系统标识列表
	 * @return 列表
	 */
	Set<Menu> findByIdIn(List<Integer> menuIds);

	/**
	 * 根据名称和删除状态查询详情
	 * @param description 名称
	 * @param i 0
	 * @return 详情
	 */
	Menu findByNameAndIsDelete(String description, int i);

	/**
	 * 根据菜单和删除状态查询列表
	 * @param url 菜单
	 * @param i 0
	 * @return 列表
	 */
	List<Menu> findByMenuUrlAndIsDelete(String url, int i);

	/**
	 * 根据父级ID查询列表
	 * @param parentId 父级
	 * @return 列表
	 */
	Page<Menu> findByParent_Id(Integer parentId, org.springframework.data.domain.Pageable pageable);
}