package com.liaoin.demo.repository.user;

import com.liaoin.demo.entity.user.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
  * @author mc
  * Create date 2019-04-10 12:38:08
  * Version 1.0
  * Description
  */
public interface DepartmentRepository extends JpaRepository<Department, Integer>, JpaSpecificationExecutor<Department> {

}