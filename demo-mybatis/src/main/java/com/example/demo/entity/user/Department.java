package com.example.demo.entity.user;

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
@NameStyle(Style.camelhump)
@Table(name = "t_department")
@org.hibernate.annotations.Table(appliesTo = "t_department", comment = "部门")
public class Department extends BasicEntity implements Serializable {

	public interface DepartmentInsertPcSimpleView {}

	public interface DepartmentUpdatePcSimpleView {}
	@Id
	@KeySql(useGeneratedKeys = true)
	@ApiModelProperty(value = "系统标识")
	@Column(columnDefinition="int(11) COMMENT '系统标识'")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull(groups = DepartmentUpdatePcSimpleView.class,message = "参数不能为空")
	private Integer id;

	@NotBlank(groups = {DepartmentInsertPcSimpleView.class,DepartmentUpdatePcSimpleView.class},message = "参数不能为为空或空串")
	@ApiModelProperty(value = "名称",example = "研发部")
	@Column(columnDefinition="varchar(255) COMMENT '名称'")
	private String name ;

	@ApiModelProperty("所属区域Id")
	@ManyToOne(targetEntity = Region.class)
	@JoinColumn(name = "region_id", referencedColumnName = "id")
	@NotNull(groups = {DepartmentInsertPcSimpleView.class,DepartmentUpdatePcSimpleView.class},message = "参数不能为为空")
	private Integer regionId ;

	@ApiModelProperty("父级Id")
	@OneToOne(targetEntity = Department.class)
	@JoinColumn(name = "parent_id", referencedColumnName = "id")
	private Integer parentId ;

	@ApiModelProperty("排序字段")
	@Column(columnDefinition="int(11) COMMENT '排序字段'")
	private Integer departmentIndex ;

	@Transient
	@ApiModelProperty(value = "下级列表",hidden = true)
	private List<Department> children;

}
