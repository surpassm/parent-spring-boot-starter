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
@NameStyle(Style.camelhump)
@Table(name = "t_region")
@org.hibernate.annotations.Table(appliesTo = "t_region", comment = "区域")
public class Region extends BasicEntity implements Serializable {

	public interface RegionInsertPcSimpleView {}

	public interface RegionUpdatePcSimpleView {}

	@Id
	@KeySql(useGeneratedKeys = true)
	@ApiModelProperty(value="系统标识")
	@Column(columnDefinition="int(11) COMMENT '系统标识'")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull(groups = RegionUpdatePcSimpleView.class,message = "参数不能为空")
	private Integer id;

	@NotBlank(groups = {RegionInsertPcSimpleView.class,RegionUpdatePcSimpleView.class},message = "参数不能为为空或空串")
    @ApiModelProperty("名称")
    private String name ;

	@ApiModelProperty("父级Id")
	private Integer parentId ;

	@ApiModelProperty("排序字段")
	private Integer departmentIndex ;




}
