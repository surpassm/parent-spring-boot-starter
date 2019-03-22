# 项目敏捷开发组合框架
介绍：着重demo里面写业务逻辑，方便于快速进入业务代码编写。。。
      - demo模块里面包含通用权限管理用户体系。具体用户、角色、权限、组均可按自己业务需求进行修改。登陆示例：
      - POST请求：localhost:8182/authentication/form?username=admin&password=123456 
        - head：Authorization：Basic dXNlcl8xOjEyMzQ1Ng==（user_1:123456） 的basic64加密

- 增加外键sql
```
组：
ALTER TABLE `库名`.`t_group`  
  ADD CONSTRAINT `group_parent_id` FOREIGN KEY (`parent_id`) REFERENCES `库名`.`t_group`(`id`);
区域：
ALTER TABLE `库名`.`t_region`  
  ADD CONSTRAINT `region_parent_id` FOREIGN KEY (`parent_id`) REFERENCES `库名`.`t_region`(`id`);
部门：
ALTER TABLE `库名`.`t_department`  
  ADD CONSTRAINT `department_parent_id` FOREIGN KEY (`parent_id`) REFERENCES `库名`.`t_department`(`id`);
权限：
ALTER TABLE `库名`.`t_menu`  
  ADD CONSTRAINT `menu_parent_id` FOREIGN KEY (`parent_id`) REFERENCES `库名`.`t_menu`(`id`);
  
用户：
ALTER TABLE `bridge`.`t_user_info`  
  ADD CONSTRAINT `t_user_info_region_id` FOREIGN KEY (`region_id`) REFERENCES `bridge`.`t_region`(`id`),
  ADD CONSTRAINT `t_user_info_department_id` FOREIGN KEY (`department_id`) REFERENCES `bridge`.`t_department`(`id`);


```
关系表外键sql
```

ALTER TABLE `库名`.`m_group_menu`  
  ADD CONSTRAINT `m_group_menu_group_id` FOREIGN KEY (`group_id`) REFERENCES `库名`.`t_group`(`id`),
  ADD CONSTRAINT `m_group_menu_menu_id` FOREIGN KEY (`menu_id`) REFERENCES `库名`.`t_menu`(`id`);
  
ALTER TABLE `库名`.`m_group_role`  
  ADD CONSTRAINT `m_group_role_group_id` FOREIGN KEY (`group_id`) REFERENCES `库名`.`t_group`(`id`),
  ADD CONSTRAINT `m_group_role_role_id` FOREIGN KEY (`role_id`) REFERENCES `库名`.`t_menu`(`id`);

ALTER TABLE `库名`.`m_role_menu`  
  ADD CONSTRAINT `m_role_menu_menu_id` FOREIGN KEY (`menu_id`) REFERENCES `库名`.`t_menu`(`id`),
  ADD CONSTRAINT `m_role_menu_role_id` FOREIGN KEY (`role_id`) REFERENCES `库名`.`t_role`(`id`);

ALTER TABLE `库名`.`m_user_group`  
  ADD CONSTRAINT `m_user_group_group_id` FOREIGN KEY (`group_id`) REFERENCES `库名`.`t_group`(`id`),
  ADD CONSTRAINT `m_user_group_user_id` FOREIGN KEY (`user_id`) REFERENCES `库名`.`t_user_info`(`id`);

ALTER TABLE `库名`.`m_user_menu`  
  ADD CONSTRAINT `m_user_menu_menu_id` FOREIGN KEY (`menu_id`) REFERENCES `库名`.`t_menu`(`id`),
  ADD CONSTRAINT `m_user_menu_user_id` FOREIGN KEY (`user_id`) REFERENCES `库名`.`t_user_info`(`id`);

ALTER TABLE `库名`.`m_user_role`  
  ADD CONSTRAINT `m_user_role_role_id` FOREIGN KEY (`role_id`) REFERENCES `库名`.`t_role`(`id`),
  ADD CONSTRAINT `m_user_role_user_id` FOREIGN KEY (`user_id`) REFERENCES `库名`.`t_user_info`(`id`);



```
