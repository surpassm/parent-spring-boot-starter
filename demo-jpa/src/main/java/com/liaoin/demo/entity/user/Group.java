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
 * Create date 2019/3/14 17:57
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
@ApiModel(value = "权限")
@Table(name = "t_group")
@org.hibernate.annotations.Table(appliesTo = "t_group", comment = "权限")
public class Group extends BasicEntity implements Serializable {


	@Id
	@ApiModelProperty(value="系统标识")
	@Column(columnDefinition="int(11) COMMENT '系统标识'")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull(groups = UpdatePcSimpleView.class,message = "参数不能为空")
	private Integer id;


	@ApiModelProperty(value = "名称")
	@Column(columnDefinition="varchar(255) COMMENT '名称'")
	@NotBlank(groups = {InsertPcSimpleView.class,UpdatePcSimpleView.class},message = "参数不能为为空或空串")
	private String name;

	@ApiModelProperty(value = "描述")
	@Column(columnDefinition="varchar(255) COMMENT '描述'")
	private String describes;

	@ManyToOne
	@ApiModelProperty("父级Id")
	@JsonIgnoreProperties({"children","menus","roles"})
	private Group parent ;
	@ApiModelProperty(value = "下级列表")
	@JsonIgnoreProperties({"parent","menus","roles"})
	@OneToMany(mappedBy = "parent", fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
	private Set<Group> children = new HashSet<>(16);


	@ApiModelProperty(value = "组权限")
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "m_group_menu", joinColumns = {
			@JoinColumn(name = "group_id", referencedColumnName = "id")},
			inverseJoinColumns = {@JoinColumn(name = "menu_id", referencedColumnName = "id")})
	@JsonIgnoreProperties({"parent","children","groups","roles"})
	private Set<Menu> menus = new HashSet<>(16);


	@ApiModelProperty(value = "组角色")
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "m_group_role", joinColumns = {
			@JoinColumn(name = "group_id", referencedColumnName = "id")},
			inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
	@JsonIgnoreProperties({"groups"})
	private Set<Role> roles = new HashSet<>(16);


	@ManyToMany(mappedBy = "groups")
	@ApiModelProperty(value = "用户组")
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
		Group build = (Group) o;
		return Objects.equals(id, build.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
