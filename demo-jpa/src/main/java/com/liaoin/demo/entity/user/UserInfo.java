package com.liaoin.demo.entity.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.surpassm.common.pojo.BasicEntity;
import com.github.surpassm.common.service.InsertPcSimpleView;
import com.github.surpassm.common.service.UpdatePcSimpleView;
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



	@Id
	@ApiModelProperty(value="系统标识")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition="int(11) COMMENT '系统标识'")
	@NotNull(groups = InsertPcSimpleView.class,message = "参数不能为空")
	private Integer id;

	@ApiModelProperty(value="姓名")
	@Column(columnDefinition="varchar(255) COMMENT '姓名'")
	@NotBlank(groups = {UpdatePcSimpleView.class,InsertPcSimpleView.class},message = "参数不能为为空或空串")
	private String name;

	@ApiModelProperty(value="手机号码")
	@Column(columnDefinition="varchar(255) COMMENT '手机号码'")
	@NotBlank(groups = {UpdatePcSimpleView.class,InsertPcSimpleView.class},message = "参数不能为为空或空串")
	private String mobile;

	@ApiModelProperty(value="用户名具有唯一约束")
	@Column(columnDefinition="varchar(255) COMMENT '用户名具有唯一约束'")
	@NotBlank(groups = {UpdatePcSimpleView.class,InsertPcSimpleView.class},message = "参数不能为为空或空串")
	private String username;

	@ApiModelProperty(value="密码")
	@Column(columnDefinition="varchar(255) COMMENT '密码'")
	@NotBlank(groups = {UpdatePcSimpleView.class,InsertPcSimpleView.class},message = "参数不能为为空或空串")
	private String password;

	@ApiModelProperty(value="头像URL")
	@Column(columnDefinition="varchar(255) COMMENT '头像URL'")
	@NotBlank(groups = {UpdatePcSimpleView.class,InsertPcSimpleView.class},message = "参数不能为为空或空串")
	private String headUrl;

	@ApiModelProperty("排序字段")
	@Column(columnDefinition="int(11) COMMENT '排序字段'")
	private Integer userInfoIndex ;

	@ApiModelProperty(value = "最后登陆时间",hidden = true)
	@Column(columnDefinition="datetime COMMENT '最后登陆时间'")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private LocalDateTime landingTime;


	@JsonInclude(JsonInclude.Include.NON_NULL)
	@ManyToOne
	@ApiModelProperty(value = "组织",hidden = true)
	@JsonIgnoreProperties({"region","children","parent","userInfos"})
	private Department department;
	@Transient
	@ApiModelProperty("组织ID")
	@NotBlank(groups = {UpdatePcSimpleView.class,InsertPcSimpleView.class},message = "参数不能为为空或空串")
	private Integer departmentId;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@ApiModelProperty(value = "用户组",hidden = true)
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "m_user_group", joinColumns = {
			@JoinColumn(name = "user_id", referencedColumnName = "id")},
			inverseJoinColumns = {@JoinColumn(name = "group_id", referencedColumnName = "id")})
	@JsonIgnoreProperties({"parent","children","menus","roles","userInfos"})
	private Set<Group> groups = new HashSet<>(16);

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@ApiModelProperty(value = "用户权限",hidden = true)
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "m_user_menu", joinColumns = {
			@JoinColumn(name = "user_id", referencedColumnName = "id")},
			inverseJoinColumns = {@JoinColumn(name = "menu_id", referencedColumnName = "id")})
	@JsonIgnoreProperties({"parent","children","groups","roles","userInfos"})
	private Set<Menu> menus = new HashSet<>(16);

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@ApiModelProperty(value = "用户角色",hidden = true)
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "m_user_role", joinColumns = {
			@JoinColumn(name = "user_id", referencedColumnName = "id")},
			inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
	@JsonIgnoreProperties({"groups","menus","userInfos"})
	private Set<Role> roles = new HashSet<>(16);



	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		UserInfo build = (UserInfo) o;
		return Objects.equals(id, build.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

}
