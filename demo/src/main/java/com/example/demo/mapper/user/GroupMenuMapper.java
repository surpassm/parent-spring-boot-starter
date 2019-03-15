package com.example.demo.mapper.user;


import com.example.demo.entity.user.GroupMenu;

/**
  * @author mc
  * Create date 2019-03-15 12:57:00
  * Version 1.0
  * Description 组权限持久层
  */
public interface GroupMenuMapper extends tk.mybatis.mapper.common.Mapper<GroupMenu> {

    int insertSelectiveCustom(GroupMenu groupMenu);
}
