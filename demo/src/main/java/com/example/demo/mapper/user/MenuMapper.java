package com.example.demo.mapper.user;

import com.example.demo.entity.user.Menu;

/**
  * @author mc
  * Create date 2019-03-14 20:41:03
  * Version 1.0
  * Description 权限持久层
  */
public interface MenuMapper extends tk.mybatis.mapper.common.Mapper<Menu> {

    int insertSelectiveCustom(Menu menu);
}
