package com.example.demo.mapper.user;


import com.example.demo.entity.user.UserRole;

/**
  * @author mc
  * Create date 2019-03-15 12:57:00
  * Version 1.0
  * Description 持久层
  */
public interface UserRoleMapper extends tk.mybatis.mapper.common.Mapper<UserRole> {

    int insertSelectiveCustom(UserRole userRole);
}
