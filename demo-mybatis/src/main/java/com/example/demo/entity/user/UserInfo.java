package com.example.demo.entity.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.surpassm.common.pojo.BasicEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author mc
 * Create date 2019/1/21 11:05
 * Version 1.0
 * Description
 */
@Entity
@Getter
@Setter
@SuppressWarnings("serial")
@ApiModel(value = "用户")
@NameStyle(Style.camelhump)
@org.hibernate.annotations.Table(appliesTo = "t_user_info", comment = "用户")
@Table(name = "t_user_info", uniqueConstraints = {@UniqueConstraint(name = "username_unique", columnNames = {"username"})})
public class UserInfo extends BasicEntity implements UserDetails,CredentialsContainer {

	@Transient
	@ApiModelProperty(hidden = true)
	private  Set<GrantedAuthority> authorities;
	@Transient
	@ApiModelProperty(hidden = true)
	private  boolean accountNonExpired;
	@Transient
	@ApiModelProperty(hidden = true)
	private  boolean accountNonLocked;
	@Transient
	@ApiModelProperty(hidden = true)
	private  boolean credentialsNonExpired;
	@Transient
	@ApiModelProperty(hidden = true)
	private  boolean enabled;

	public UserInfo(){}

	public UserInfo(Integer id, String username, String password, boolean enabled,
					boolean accountNonExpired, boolean credentialsNonExpired,
					boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities){
		if (((username == null) || "".equals(username)) || (password == null)) {
			throw new IllegalArgumentException("不能将空值或空值传递给构造函数");
		}
		this.id = id;
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.accountNonExpired = accountNonExpired;
		this.credentialsNonExpired = credentialsNonExpired;
		this.accountNonLocked = accountNonLocked;
		this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
	}

	private static SortedSet<GrantedAuthority> sortAuthorities(
			Collection<? extends GrantedAuthority> authorities) {
		Assert.notNull(authorities, "无法传递空的GrantedAuthority集合");
		SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<>(
				new AuthorityComparator());

		for (GrantedAuthority grantedAuthority : authorities) {
			Assert.notNull(grantedAuthority,
					"GrantedAuthority列表不能包含任何空元素");
			sortedAuthorities.add(grantedAuthority);
		}
		return sortedAuthorities;
	}

	@Override
	public void eraseCredentials() {
		password = null;
	}

	private static class AuthorityComparator implements Comparator<GrantedAuthority>,
			Serializable {
		private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
		@Override
		public int compare(GrantedAuthority g1, GrantedAuthority g2) {
			if (g2.getAuthority() == null) {
				return -1;
			}
			if (g1.getAuthority() == null) {
				return 1;
			}
			return g1.getAuthority().compareTo(g2.getAuthority());
		}
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public interface UserInfoInsertPcSimpleView {}

	public interface UserInfoUpdatePcSimpleView {}

	@Id
	@KeySql(useGeneratedKeys = true)
	@ApiModelProperty(value="系统标识")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition="int(11) COMMENT '系统标识'")
	@NotNull(groups = UserInfoInsertPcSimpleView.class,message = "参数不能为空")
	private Integer id;

	@ApiModelProperty("所属组织Id")
	@ManyToOne(targetEntity = Department.class)
	@JoinColumn(name = "department_id", referencedColumnName = "id")
	@NotNull(groups = {UserInfoUpdatePcSimpleView.class,UserInfoInsertPcSimpleView.class},message = "参数不能为为空")
	private Integer departmentId ;

	@ApiModelProperty(value="姓名")
	@Column(columnDefinition="varchar(255) COMMENT '姓名'")
	@NotBlank(groups = {UserInfoUpdatePcSimpleView.class,UserInfoInsertPcSimpleView.class},message = "参数不能为为空或空串")
	private String name;

	@ApiModelProperty(value="手机号码")
	@Column(columnDefinition="varchar(255) COMMENT '手机号码'")
	@NotBlank(groups = {UserInfoUpdatePcSimpleView.class,UserInfoInsertPcSimpleView.class},message = "参数不能为为空或空串")
	private String mobile;

	@ApiModelProperty(value="用户名具有唯一约束")
	@Column(columnDefinition="varchar(255) COMMENT '用户名具有唯一约束'")
	@NotBlank(groups = {UserInfoUpdatePcSimpleView.class,UserInfoInsertPcSimpleView.class},message = "参数不能为为空或空串")
	private String username;

	@ApiModelProperty(value="密码")
	@Column(columnDefinition="varchar(255) COMMENT '密码'")
	@NotBlank(groups = {UserInfoUpdatePcSimpleView.class,UserInfoInsertPcSimpleView.class},message = "参数不能为为空或空串")
	private String password;

	@ApiModelProperty(value="头像URL")
	@Column(columnDefinition="varchar(255) COMMENT '头像URL'")
	@NotBlank(groups = {UserInfoUpdatePcSimpleView.class,UserInfoInsertPcSimpleView.class},message = "参数不能为为空或空串")
	private String headUrl;

	@ApiModelProperty("排序字段")
	@Column(columnDefinition="int(11) COMMENT '排序字段'")
	private Integer userInfoIndex ;


	@ApiModelProperty(value = "最后登陆时间",hidden = true)
	@Column(columnDefinition="datetime COMMENT '最后登陆时间'")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private LocalDateTime landingTime;



	@Transient
	@ApiModelProperty(value = "拥有角色",hidden = true)
	private List<Role> roles ;




}
