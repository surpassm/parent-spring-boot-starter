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
 * Create date 2019/3/14 18:09
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
@ApiModel(value = "组角色")
@NameStyle(Style.camelhump)
@Table(name = "t_group_role")
@org.hibernate.annotations.Table(appliesTo = "t_group_role", comment = "组角色")
public class GroupRole extends BasicEntity implements Serializable {

	public interface GroupRoleInsertPcSimpleView {}

	public interface GroupRoleUpdatePcSimpleView {}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@KeySql(useGeneratedKeys = true)
	@ApiModelProperty(value="系统标识")
	@Column(columnDefinition="int(11) COMMENT '系统标识'")
	@NotNull(groups = GroupRoleUpdatePcSimpleView.class,message = "参数不能为空")
	public Integer id;

	@ApiModelProperty(value="组系统标识",example = "1")
	@Column(columnDefinition="int(11) COMMENT '组系统标识'",nullable = false)
	@NotNull(groups = {GroupRoleInsertPcSimpleView.class,GroupRoleUpdatePcSimpleView.class},message = "参数不能为为空")
	private Integer groupId;
	@ApiModelProperty(value="角色系统标识",example = "1")
	@Column(columnDefinition="int(11) COMMENT '角色系统标识'",nullable = false)
	@NotNull(groups = {GroupRoleInsertPcSimpleView.class,GroupRoleUpdatePcSimpleView.class},message = "参数不能为为空")
	private Integer roleId;
}
