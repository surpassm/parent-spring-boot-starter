# 项目敏捷开发组合框架
介绍：着重demo里面写业务逻辑，方便于快速进入业务代码编写。。。
      - demo模块里面包含通用权限管理用户体系。具体用户、角色、权限、组均可按自己业务需求进行修改。登陆示例：
      - POST请求：localhost:8182/authentication/form?username=admin&password=123456 
        - head：Authorization：Basic dXNlcl8xOjEyMzQ1Ng==（user_1:123456） 的basic64加密

- 用户角色关联表sql
```
CREATE TABLE `bridge`.`m_user_role`(  
  `user_info_id` INT(11) NOT NULL COMMENT '用户系统标识',
  `role_id` INT(11) NOT NULL COMMENT '角色系统标识',
  CONSTRAINT `m_user_info_id` FOREIGN KEY (`user_info_id`) REFERENCES `bridge`.`f_user_info`(`id`),
  CONSTRAINT `m_role_id` FOREIGN KEY (`role_id`) REFERENCES `bridge`.`f_role`(`id`)
) ENGINE=INNODB CHARSET=utf8mb4 COLLATE=utf8mb4_estonian_ci
COMMENT='用户角色关联表sql';

```

- 角色权限关联表sql
````
CREATE TABLE `bridge`.`m_role_menu`(  
  `role_id` INT(11) NOT NULL COMMENT '角色系统标识',
  `menu_id` INT(11) NOT NULL COMMENT '权限系统标识',
  CONSTRAINT `m_role_menu_id` FOREIGN KEY (`role_id`) REFERENCES `bridge`.`f_role`(`id`),
  CONSTRAINT `m_menu_id` FOREIGN KEY (`menu_id`) REFERENCES `bridge`.`f_menu`(`id`)
) ENGINE=INNODB CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci
COMMENT='角色权限关联表sql';
````
