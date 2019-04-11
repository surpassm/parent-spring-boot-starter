package com.liaoin.demo.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.surpassm.common.pojo.BasicEntity;
import com.github.surpassm.common.service.InsertPcSimpleView;
import com.github.surpassm.common.service.UpdatePcSimpleView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;

/**
 * @author mc
 * @version 1.0
 * @date 2018/9/25 9:24
 * @description
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("serial")
@ApiModel(value = "权限")
@Table(name = "t_menu")
@org.hibernate.annotations.Table(appliesTo = "t_menu", comment = "权限")
public class Menu extends BasicEntity implements Serializable {

	@Id
    @ApiModelProperty(value = "系统标识")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition="int(11) COMMENT '系统标识'")
	@NotNull(groups = UpdatePcSimpleView.class,message = "参数不能为空")
    private Integer id;


    @ApiModelProperty(value = "菜单排序")
	@Column(columnDefinition="int(11) COMMENT '菜单排序'")
    private Integer menuIndex;

    @Max(value = 1)
	@Min(value = 0)
    @ApiModelProperty(value = "权限分类（0 菜单；1 功能）",allowableValues = "0,1")
	@Column(columnDefinition="int(1) COMMENT '权限分类（0 菜单；1 功能）'")
    private Integer type;


    @ApiModelProperty(value = "名称")
	@Column(columnDefinition="varchar(255) COMMENT '名称'")
	@NotBlank(groups = {InsertPcSimpleView.class,UpdatePcSimpleView.class},message = "参数不能为为空或空串")
    private String name;

    @ApiModelProperty(value = "描述")
	@NotBlank(message = "参数不能为为空或空串")
	@Column(columnDefinition="varchar(255) COMMENT '描述'")
    private String describes;

    @ApiModelProperty(value = "路由路径 前端使用")
	@Column(columnDefinition="varchar(255) COMMENT '路由路径 前端使用'")
    private String path;

    @ApiModelProperty(value = "菜单图标名称")
	@Column(columnDefinition="varchar(255) COMMENT '菜单图标名称'")
    private String menuIcon;

    @ApiModelProperty(value = "菜单url后台权限控制")
	@Column(columnDefinition="varchar(255) COMMENT '菜单url后台权限控制'")
    private String menuUrl;

    @JsonIgnore
	@ManyToOne
	@ApiModelProperty(value = "父级Id",hidden = true)
	@JsonIgnoreProperties({"children","groups","roles"})
	private Menu parent ;
	@Transient
	@ApiModelProperty(value ="父级Id")
	private Integer parentId;
	@ApiModelProperty(value = "下级列表",hidden = true)
	@JsonIgnoreProperties({"parent","groups","roles"})
	@OneToMany(mappedBy = "parent", fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
	private Set<Menu> children = new HashSet<>(16);

	@JsonIgnore
	@ManyToMany(mappedBy = "menus")
	@ApiModelProperty(value = "组权限",hidden = true)
	@JsonIgnoreProperties({"menus","roles"})
	private Set<Group> groups = new HashSet<>(16);

	@JsonIgnore
	@ManyToMany(mappedBy = "menus")
	@ApiModelProperty(value = "角色权限",hidden = true)
	@JsonIgnoreProperties({"groups","menus"})
	private Set<Role> roles = new HashSet<>(16);

	@JsonIgnore
	@ManyToMany(mappedBy = "menus")
	@ApiModelProperty(value = "用户权限",hidden = true)
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
		Menu build = (Menu) o;
		return Objects.equals(id, build.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

}
