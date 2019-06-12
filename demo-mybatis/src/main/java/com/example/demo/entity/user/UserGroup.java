package com.example.demo.entity.user;

import com.github.surpassm.common.pojo.BasicEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author mc
 * Create date 2019/3/14 18:15
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
@ApiModel(value = "用户组")
@NameStyle(Style.camelhump)
@Table(name = "m_user_group")
@org.hibernate.annotations.Table(appliesTo = "m_user_group", comment = "用户组")
public class UserGroup extends BasicEntity implements Serializable {
	public interface UserGroupInsertPcSimpleView {}

	public interface UserGroupUpdatePcSimpleView {}


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@KeySql(useGeneratedKeys = true)
	@ApiModelProperty(value="系统标识")
	@Column(columnDefinition="int(11) COMMENT '系统标识'")
	@NotNull(groups = UserGroupUpdatePcSimpleView.class,message = "参数不能为空")
	public Integer id;

	@ApiModelProperty(value="用户系统标识",example = "1")
	@ManyToOne(targetEntity = UserInfo.class)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	@NotNull(groups = {UserGroupInsertPcSimpleView.class,UserGroupUpdatePcSimpleView.class},message = "参数不能为为空")
	private Integer userId;
	@ApiModelProperty(value="组系统标识",example = "1")
	@ManyToOne(targetEntity = Group.class)
	@JoinColumn(name = "group_id", referencedColumnName = "id")
	@NotNull(groups = {UserGroupInsertPcSimpleView.class,UserGroupUpdatePcSimpleView.class},message = "参数不能为为空")
	private Integer groupId;
}
