package com.liaoin.demo.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.surpassm.common.pojo.BasicEntity;
import com.github.surpassm.common.service.InsertPcSimpleView;
import com.github.surpassm.common.service.UpdatePcSimpleView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;

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
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("serial")
@ApiModel(value = "角色")
@Table(name = "t_role")
@org.hibernate.annotations.Table(appliesTo = "t_role", comment = "角色")
public class Role extends BasicEntity implements Serializable {



	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(value="系统标识")
	@Column(columnDefinition="int(11) COMMENT '系统标识'")
	@NotNull(groups = UpdatePcSimpleView.class,message = "参数不能为空")
	public Integer id;


	@ApiModelProperty(value = "security标识")
	@Column(columnDefinition="varchar(255) COMMENT 'security标识'")
	@NotBlank(groups = {InsertPcSimpleView.class,UpdatePcSimpleView.class},message = "参数不能为为空或空串")
	private String securityRoles;

	@ApiModelProperty(value = "名称")
	@Column(columnDefinition="varchar(255) COMMENT '名称'")
	@NotBlank(groups = {InsertPcSimpleView.class,UpdatePcSimpleView.class},message = "参数不能为为空或空串")
	private String name;

	@ApiModelProperty(value = "描述")
	@Column(columnDefinition="varchar(255) COMMENT '描述'")
	private String describes;

	@ApiModelProperty("排序字段")
	@Column(columnDefinition="int(11) COMMENT '排序字段'")
	private Integer roleIndex ;

	@ManyToMany(mappedBy = "roles")
	@ApiModelProperty(value = "组权限")
	@JsonIgnoreProperties({"parent","children","menus","roles"})
	private Set<Group> groups = new HashSet<>(16);

	@ApiModelProperty(value = "角色权限")
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "m_role_menu", joinColumns = {
			@JoinColumn(name = "role_id", referencedColumnName = "id")},
			inverseJoinColumns = {@JoinColumn(name = "menu_id", referencedColumnName = "id")})
	@JsonIgnoreProperties({"parent","children","groups","roles"})
	private Set<Menu> menus = new HashSet<>(16);

	@ManyToMany(mappedBy = "roles")
	@ApiModelProperty(value = "用户角色")
	@JsonIgnoreProperties({"department","groups","menus","roles"})
	private Set<UserInfo> userInfos = new HashSet<>(16);



	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Role build = (Role) o;
		return Objects.equals(id, build.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
