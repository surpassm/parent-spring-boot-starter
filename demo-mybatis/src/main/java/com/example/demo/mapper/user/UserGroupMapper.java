package com.example.demo.mapper.user;


import com.example.demo.entity.user.UserGroup;

/**
  * @author mc
  * Create date 2019-03-15 12:57:00
  * Version 1.0
  * Description 用户组持久层
  */
public interface UserGroupMapper extends tk.mybatis.mapper.common.Mapper<UserGroup> {

    int insertSelectiveCustom(UserGroup userGroup);
}