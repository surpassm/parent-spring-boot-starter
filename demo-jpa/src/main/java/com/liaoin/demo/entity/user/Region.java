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
 * version 1.0v
 * date 2019/1/1 11:16
 * description TODO
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("serial")
@ApiModel(description = "区域")
@Table(name = "t_region")
@org.hibernate.annotations.Table(appliesTo = "t_region", comment = "区域")
public class Region extends BasicEntity implements Serializable {


	@Id
	@ApiModelProperty(value="系统标识")
	@Column(columnDefinition="int(11) COMMENT '系统标识'")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull(groups = UpdatePcSimpleView.class,message = "参数不能为空")
	private Integer id;


    @ApiModelProperty("名称")
	@Column(columnDefinition="varchar(255) COMMENT '名称'")
	@NotBlank(groups = {InsertPcSimpleView.class,UpdatePcSimpleView.class},message = "参数不能为为空或空串")
    private String name ;


	@ApiModelProperty("排序字段")
	@Column(columnDefinition="int(11) COMMENT '排序字段'")
	private Integer departmentIndex ;

	@ManyToOne
	@ApiModelProperty("父级Id")
	@JsonIgnoreProperties({"departments","children"})
	private Region parent ;
	@ApiModelProperty(value = "下级列表",hidden = true)
	@JsonIgnoreProperties({"parent","departments"})
	@OneToMany(mappedBy = "parent", fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
	private Set<Region> children = new HashSet<>(16);


	@ApiModelProperty("部门Id")
	@JsonIgnoreProperties({"parent","children","region"})
	@OneToMany(cascade = {CascadeType.REMOVE,CascadeType.MERGE},mappedBy = "region")
	private Set<Department> departments = new HashSet<>(16);



	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Region build = (Region) o;
		return Objects.equals(id, build.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

}
