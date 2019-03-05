# 项目开发常用工具集

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
