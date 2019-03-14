package com.example.demo.mapper.user;


import com.example.demo.entity.user.UserInfo;

/**
  * @author mc
  * Create date 2019-03-14 20:41:03
  * Version 1.0
  * Description 持久层
  */
public interface UserInfoMapper extends tk.mybatis.mapper.common.Mapper<UserInfo> {

    int insertSelectiveCustom(UserInfo userInfo);
}
