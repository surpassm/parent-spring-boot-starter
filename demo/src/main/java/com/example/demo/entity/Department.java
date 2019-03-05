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
@SuppressWarnings("serial")
@ApiModel(value = "部门")
@NameStyle(Style.camelhump)
@Table(name = "f_department")
@org.hibernate.annotations.Table(appliesTo = "f_department", comment = "部门")
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
	@ApiModelProperty("名称")
	private String name ;

	@ApiModelProperty("父级Id")
	private Integer parentId ;

	@ApiModelProperty("排序字段")
	private Integer departmentIndex ;

}
