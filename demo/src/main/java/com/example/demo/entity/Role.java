package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.surpassm.common.pojo.BasicEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author mc
 * Create date 2019/1/21 13:24
 * Version 1.0
 * Description
 */
@Entity
@Getter
@Setter
@Builder
@SuppressWarnings("serial")
@ApiModel(value = "角色")
@NameStyle(Style.camelhump)
@Table(name = "f_role")
@org.hibernate.annotations.Table(appliesTo = "f_role", comment = "角色")
public class Role extends BasicEntity implements Serializable {

	public interface RoleInsertPcSimpleView {}

	public interface RoleUpdatePcSimpleView {}


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@KeySql(useGeneratedKeys = true)
	@ApiModelProperty(value="系统标识")
	@Column(columnDefinition="int(11) COMMENT '系统标识'")
	@NotNull(groups = RoleUpdatePcSimpleView.class,message = "参数不能为空")
	public Integer id;

	@NotBlank(groups = {RoleInsertPcSimpleView.class,RoleUpdatePcSimpleView.class},message = "参数不能为为空或空串")
	@ApiModelProperty(value = "security标识")
	private String securityRoles;
	@NotBlank(groups = {RoleInsertPcSimpleView.class,RoleUpdatePcSimpleView.class},message = "参数不能为为空或空串")
	@ApiModelProperty(value = "名称")
	private String name;
	@ApiModelProperty(value = "描述")
	private String describes;

	@Transient
	@NotBlank(groups = {RoleInsertPcSimpleView.class,RoleUpdatePcSimpleView.class},message = "参数不能为为空或空串")
	@ApiModelProperty("权限列表")
	private List<Menu> menuList ;

	@ApiModelProperty("排序字段")
	private Integer roleIndex ;
}
