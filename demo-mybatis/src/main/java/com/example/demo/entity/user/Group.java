package com.example.demo.entity.user;

import com.github.surpassm.common.pojo.BasicEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

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
@NameStyle(Style.camelhump)
@Table(name = "t_group")
@org.hibernate.annotations.Table(appliesTo = "t_group", comment = "权限")
public class Group extends BasicEntity implements Serializable {

	public interface GroupInsertPcSimpleView {}

	public interface GroupUpdatePcSimpleView {}

	@Id
	@KeySql(useGeneratedKeys = true)
	@ApiModelProperty(value="系统标识")
	@Column(columnDefinition="int(11) COMMENT '系统标识'")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull(groups = GroupUpdatePcSimpleView.class,message = "参数不能为空")
	private Integer id;


	@ApiModelProperty(value = "名称")
	@Column(columnDefinition="varchar(255) COMMENT '名称'")
	@NotBlank(groups = {GroupInsertPcSimpleView.class,GroupUpdatePcSimpleView.class},message = "参数不能为为空或空串")
	private String name;

	@ApiModelProperty(value = "描述")
	@Column(columnDefinition="varchar(255) COMMENT '描述'")
	private String describes;

	@ApiModelProperty(value = "父级ID")
	@OneToOne(targetEntity = Group.class)
	@JoinColumn(name = "parent_id", referencedColumnName = "id")
	private Integer parentId;

	@Transient
	@ApiModelProperty(value = "下级列表",hidden = true)
	private List<Group> children;
}
