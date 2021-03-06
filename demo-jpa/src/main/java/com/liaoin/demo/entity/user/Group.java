package com.liaoin.demo.entity.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@ApiModel(value = "组")
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

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@ApiModelProperty(value = "父级Id",hidden = true)
	@JsonIgnoreProperties({"children","menus","roles"})
	private Group parent ;
	@Transient
	@ApiModelProperty(value ="父级Id")
	private Integer parentId;
	@ApiModelProperty(value = "下级列表",hidden = true)
	@JsonIgnoreProperties({"parent","menus","roles"})
	@OneToMany(mappedBy = "parent", fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
	private Set<Group> children = new HashSet<>(16);


	@JsonIgnore
	@ApiModelProperty(value = "组权限",hidden = true)
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "m_group_menu", joinColumns = {
			@JoinColumn(name = "group_id", referencedColumnName = "id")},
			inverseJoinColumns = {@JoinColumn(name = "menu_id", referencedColumnName = "id")})
	@JsonIgnoreProperties({"parent","children","groups","roles"})
	private Set<Menu> menus = new HashSet<>(16);

	@JsonIgnore
	@ApiModelProperty(value = "组角色",hidden = true)
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "m_group_role", joinColumns = {
			@JoinColumn(name = "group_id", referencedColumnName = "id")},
			inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
	@JsonIgnoreProperties({"groups"})
	private Set<Role> roles = new HashSet<>(16);

	@JsonIgnore
	@ManyToMany(mappedBy = "groups")
	@ApiModelProperty(value = "用户组",hidden = true)
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
