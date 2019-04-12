package com.liaoin.demo.entity.user;

import com.fasterxml.jackson.annotation.*;
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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author mc
 * Create date 2019/1/21 13:39
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
@ApiModel(value = "部门")
@Table(name = "t_department")
@org.hibernate.annotations.Table(appliesTo = "t_department", comment = "部门")
public class Department extends BasicEntity implements Serializable {

	@Id
	@ApiModelProperty(value = "系统标识")
	@Column(columnDefinition = "int(11) COMMENT '系统标识'")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull(groups = UpdatePcSimpleView.class, message = "参数不能为空")
	private Integer id;

	@NotBlank(groups = {InsertPcSimpleView.class, UpdatePcSimpleView.class}, message = "参数不能为为空或空串")
	@ApiModelProperty(value = "名称", example = "研发部")
	@Column(columnDefinition = "varchar(255) COMMENT '名称'")
	private String name;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@ApiModelProperty(value = "所属区域Id", hidden = true)
	private Region region;

	@Transient
	@ApiModelProperty(value = "所属区域Id")
	@NotNull(groups = {InsertPcSimpleView.class, UpdatePcSimpleView.class}, message = "参数不能为为空")
	private Integer regionId;


	@ApiModelProperty("排序字段")
	@Column(columnDefinition = "int(11) COMMENT '排序字段'")
	private Integer departmentIndex;


	@JsonIgnore
	@ApiModelProperty(value = "下级列表", hidden = true)
	@OneToMany(mappedBy = "parent", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties({"region", "parent"})
	private Set<Department> children = new HashSet<>(16);

	@JsonIgnore
	@ApiModelProperty(value = "父级", hidden = true)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn
	@JsonIgnoreProperties({"region", "children"})
	private Department parent;
	@Transient
	@ApiModelProperty(value = "父级Id")
	private Integer parentId;


	@JsonIgnore
	@OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "department")
	@ApiModelProperty(value = "用户列表", hidden = true)
	@JsonIgnoreProperties({"department", "groups", "menus", "roles"})
	private Set<UserInfo> userInfos = new HashSet<>(16);


	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Department department = (Department) o;
		return Objects.equals(id, department.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
