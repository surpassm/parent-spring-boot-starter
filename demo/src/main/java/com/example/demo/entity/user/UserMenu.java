package com.example.demo.entity.user;

import com.github.surpassm.common.pojo.BasicEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author mc
 * Create date 2019/3/14 18:13
 * Version 1.0
 * Description
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("serial")
@ApiModel(value = "用户权限")
@NameStyle(Style.camelhump)
@Table(name = "t_user_menu")
@org.hibernate.annotations.Table(appliesTo = "t_user_menu", comment = "用户权限")
public class UserMenu extends BasicEntity implements Serializable {
	public interface UserMenuInsertPcSimpleView {}

	public interface UserMenuUpdatePcSimpleView {}


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@KeySql(useGeneratedKeys = true)
	@ApiModelProperty(value="系统标识")
	@Column(columnDefinition="int(11) COMMENT '系统标识'")
	@NotNull(groups = UserMenuUpdatePcSimpleView.class,message = "参数不能为空")
	public Integer id;

	@ApiModelProperty(value="用户系统标识",example = "1")
	@Column(columnDefinition="int(11) COMMENT '用户系统标识'",nullable = false)
	@NotNull(groups = {UserMenuInsertPcSimpleView.class,UserMenuUpdatePcSimpleView.class},message = "参数不能为为空")
	private Integer userId;
	@ApiModelProperty(value="权限系统标识",example = "1")
	@Column(columnDefinition="int(11) COMMENT '权限系统标识'",nullable = false)
	@NotNull(groups = {UserMenuInsertPcSimpleView.class,UserMenuUpdatePcSimpleView.class},message = "参数不能为为空")
	private Integer menuId;
	@ApiModelProperty(value="权限类型0=可访问、1=可授权",example = "1")
	@Column(columnDefinition="int(1) COMMENT '权限类型'",nullable = false)
	@NotNull(groups = {UserMenuInsertPcSimpleView.class,UserMenuUpdatePcSimpleView.class},message = "参数不能为为空")
	private Integer menuType;
}
