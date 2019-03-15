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
@NameStyle(Style.camelhump)
@Table(name = "t_menu")
@org.hibernate.annotations.Table(appliesTo = "t_menu", comment = "权限")
public class Menu extends BasicEntity implements Serializable {
	public interface MenuInsertPcSimpleView {}

	public interface MenuUpdatePcSimpleView {}

	@Id
	@KeySql(useGeneratedKeys = true)
    @ApiModelProperty(value = "系统标识")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition="int(11) COMMENT '系统标识'")
	@NotNull(groups = MenuUpdatePcSimpleView.class,message = "参数不能为空")
    private Integer id;

    @ApiModelProperty(value = "父级菜单ID")
    private Integer parentId;
    @ApiModelProperty(value = "菜单排序")
    private Integer menuIndex;

    @ApiModelProperty(value = "权限分类（0 菜单；1 功能）")
    private Integer type;

	@NotBlank(groups = {MenuInsertPcSimpleView.class,MenuUpdatePcSimpleView.class},message = "参数不能为为空或空串")
    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "描述")
	@NotBlank(message = "参数不能为为空或空串")
    private String describes;

    @ApiModelProperty(value = "路由路径 前端使用")
    private String path;
    @ApiModelProperty(value = "菜单图标名称")
    private String menuIcon;

    @ApiModelProperty(value = "菜单url后台权限控制")
    private String menuUrl;


	@Transient
	@ApiModelProperty(value = "下级列表",hidden = true)
	private List<Menu> children;
}
