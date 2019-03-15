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
 * Create date 2019/3/14 18:00
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
@ApiModel(value = "角色权限")
@NameStyle(Style.camelhump)
@Table(name = "m_role_menu")
@org.hibernate.annotations.Table(appliesTo = "m_role_menu", comment = "角色权限")
public class RoleMenu extends BasicEntity implements Serializable {

	public interface RoleMenuInsertPcSimpleView {}

	public interface RoleMenuUpdatePcSimpleView {}


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@KeySql(useGeneratedKeys = true)
	@ApiModelProperty(value="系统标识")
	@Column(columnDefinition="int(11) COMMENT '系统标识'")
	@NotNull(groups = RoleMenuUpdatePcSimpleView.class,message = "参数不能为空")
	public Integer id;

	@ApiModelProperty(value="角色系统标识",example = "1")
	@Column(columnDefinition="int(11) COMMENT '角色系统标识'",nullable = false)
	@NotNull(groups = {RoleMenuInsertPcSimpleView.class,RoleMenuUpdatePcSimpleView.class},message = "参数不能为为空")
	private Integer roleId;
	@ApiModelProperty(value="权限系统标识",example = "1")
	@Column(columnDefinition="int(11) COMMENT '权限系统标识'",nullable = false)
	@NotNull(groups = {RoleMenuInsertPcSimpleView.class,RoleMenuUpdatePcSimpleView.class},message = "参数不能为为空")
	private Integer menuId;

	@ApiModelProperty(value="权限类型0=可访问、1=可授权",example = "1",allowableValues = "0,1")
	@Column(columnDefinition="int(1) COMMENT '权限类型'",nullable = false)
	@NotNull(groups = {RoleMenuInsertPcSimpleView.class,RoleMenuUpdatePcSimpleView.class},message = "参数不能为为空")
	private Integer menuType;


}
