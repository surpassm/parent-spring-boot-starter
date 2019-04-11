package com.liaoin.demo.service.user.impl;

import com.github.surpassm.common.jackson.Result;
import com.github.surpassm.common.jackson.Tips;
import com.github.surpassm.tool.util.ValidateUtil;
import com.liaoin.demo.entity.user.*;
import com.liaoin.demo.repository.user.*;
import com.liaoin.demo.security.BeanConfig;
import com.liaoin.demo.service.user.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.*;

import static com.github.surpassm.common.jackson.Result.fail;
import static com.github.surpassm.common.jackson.Result.ok;

/**
  * @author mc
  * Create date 2019-04-10 12:49:52
  * Version 1.0
  * Description 用户实现类
  */
@Slf4j
@Service
@Transactional(rollbackFor={RuntimeException.class, Exception.class})
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    private UserInfoRepository userInfoRepository;
    @Resource
	private BeanConfig beanConfig;
    @Resource
	private DepartmentRepository departmentRepository;
	@Resource
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Resource
	private GroupRepository groupRepository;
	@Resource
	private MenuRepository menuRepository;
	@Resource
	private RoleRepository roleRepository;

    @Override
    public Result insert(String accessToken, UserInfo userInfo) {
        if (!Optional.ofNullable(userInfo).isPresent()){
            return fail(Tips.PARAMETER_ERROR.msg);
        }
		if (userInfo.getMobile() != null){
			if (!ValidateUtil.isMobilePhone(userInfo.getMobile())){
				return fail(Tips.phoneFormatError.msg);
			}
		}
		//效验手姓名
		if (!ValidateUtil.isRealName(userInfo.getName())){
			return fail("姓名格式错误");
		}
		if (userInfo.getUsername() == null || "".equals(userInfo.getUsername().trim())){
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		if (userInfo.getPassword() == null || "".equals(userInfo.getPassword().trim())){
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		if (!ValidateUtil.isPassword(userInfo.getPassword())){
			return fail(Tips.passwordFormatError.msg);
		}
		UserInfo loginUser = beanConfig.getAccessToken(accessToken);
		if (userInfo.getDepartmentId() != null){
			Optional<Department> byId = departmentRepository.findById(userInfo.getDepartmentId());
			byId.ifPresent(userInfo::setDepartment);
		}
		userInfo.setUsername(userInfo.getUsername().trim());
		UserInfo userInfos = userInfoRepository.findByUsername(userInfo.getUsername());
		if (userInfos != null){
			return fail("账号已存在");
		}
		userInfo.setPassword(bCryptPasswordEncoder.encode(userInfo.getPassword().trim()));
        userInfo.setCreateTime(LocalDateTime.now());
        userInfo.setCreateUserId(loginUser.getId());
        userInfo.setIsDelete(0);
        userInfoRepository.save(userInfo);
        return ok();
    }

    @Override
    public Result update(String accessToken, UserInfo userInfo) {
        if (!Optional.ofNullable(userInfo).isPresent()){
            return fail(Tips.PARAMETER_ERROR.msg);
        }
        UserInfo loginUser = beanConfig.getAccessToken(accessToken);
		if (userInfo.getMobile() != null){
			if (!ValidateUtil.isMobilePhone(userInfo.getMobile())){
				return fail(Tips.phoneFormatError.msg);
			}
		}
		//效验手姓名
		if (!ValidateUtil.isRealName(userInfo.getName())){
			return fail("姓名格式错误");
		}
		if (userInfo.getUsername() == null || "".equals(userInfo.getUsername().trim())){
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		if (userInfo.getPassword() == null || "".equals(userInfo.getPassword().trim())){
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		if (!ValidateUtil.isPassword(userInfo.getPassword())){
			return fail(Tips.passwordFormatError.msg);
		}
		if (userInfo.getDepartmentId() != null){
			Optional<Department> byId = departmentRepository.findById(userInfo.getDepartmentId());
			byId.ifPresent(userInfo::setDepartment);
		}
		Optional<UserInfo> byId = userInfoRepository.findById(userInfo.getId());
		if (!byId.isPresent()){
			return fail(Tips.MSG_NOT.msg);
		}
		String password = userInfo.getPassword();
		//密码效验
		if (userInfo.getPassword() != null && !"".equals(userInfo.getPassword())) {
			if (!ValidateUtil.isPassword(userInfo.getPassword())) {
				return fail(Tips.passwordFormatError.msg);
			}
			if (!bCryptPasswordEncoder.matches(password,byId.get().getPassword())){
				String passwordNew = bCryptPasswordEncoder.encode(password);
				userInfo.setPassword(passwordNew);
			}
		}
		userInfo.setUpdateTime(LocalDateTime.now());
        userInfo.setUpdateUserId(loginUser.getId());
        userInfoRepository.save(userInfo);
        return ok();
    }


    @Override
    public Result deleteGetById(String accessToken, Integer id){
        if (!Optional.ofNullable(id).isPresent()){
            return fail(Tips.PARAMETER_ERROR.msg);
        }
        UserInfo loginUser = beanConfig.getAccessToken(accessToken);
        Optional<UserInfo> optional = userInfoRepository.findById(id);
        if(!optional.isPresent()){
            return fail(Tips.MSG_NOT.msg);
        }
        UserInfo userInfo = optional.get();
        userInfo.setIsDelete(1);
        userInfo.setDeleteTime(LocalDateTime.now());
        userInfo.setDeleteUserId(loginUser.getId());
        return ok();
    }


    @Override
    public Result findById(String accessToken, Integer id) {
        if (id == null){
            return fail(Tips.PARAMETER_ERROR.msg);
        }
        beanConfig.getAccessToken(accessToken);
		Optional<UserInfo> optional = userInfoRepository.findById(id);
        return optional.map(Result::ok).orElseGet(() -> fail(Tips.MSG_NOT.msg));
    }

    @Override
    public Result pageQuery(String accessToken, Integer page, Integer size, String sort, UserInfo userInfo) {
        page = page == null ? 0 : page;
		size = size == null ? 10 : size;
    	if (page>0){page--;}
        PageRequest pageable = PageRequest.of(page, size);
        if (sort !=null && "".equals(sort.trim())) {
            pageable= PageRequest.of(page,page,new Sort(Sort.Direction.DESC,sort));
        }
        Page<UserInfo> all = userInfoRepository.findAll((root, criteriaQuery, criteriaBuilder)-> {
            List<Predicate> list = new ArrayList<>();
            if (userInfo != null) {
                if (userInfo.getId() != null) {
                    list.add(criteriaBuilder.equal(root.get("id").as(Integer.class), userInfo.getId()));
                }
                if (userInfo.getCreateTime() != null) {
                    list.add(criteriaBuilder.equal(root.get("createTime").as(Date.class), userInfo.getCreateTime()));
                }
                if (userInfo.getCreateUserId() != null) {
                    list.add(criteriaBuilder.equal(root.get("createUserId").as(Integer.class), userInfo.getCreateUserId()));
                }
                if (userInfo.getDeleteTime() != null) {
                    list.add(criteriaBuilder.equal(root.get("deleteTime").as(Date.class), userInfo.getDeleteTime()));
                }
                if (userInfo.getDeleteUserId() != null) {
                    list.add(criteriaBuilder.equal(root.get("deleteUserId").as(Integer.class), userInfo.getDeleteUserId()));
                }
                if (userInfo.getUpdateTime() != null) {
                    list.add(criteriaBuilder.equal(root.get("updateTime").as(Date.class), userInfo.getUpdateTime()));
                }
                if (userInfo.getUpdateUserId() != null) {
                    list.add(criteriaBuilder.equal(root.get("updateUserId").as(Integer.class), userInfo.getUpdateUserId()));
                }
                if ((userInfo.getHeadUrl() != null) && !"".equals(userInfo.getHeadUrl().trim())) {
                    list.add(criteriaBuilder.like(root.get("headUrl").as(String.class), "%" + userInfo.getHeadUrl() + "%"));
                }
                if (userInfo.getLandingTime() != null) {
                    list.add(criteriaBuilder.equal(root.get("landingTime").as(Date.class), userInfo.getLandingTime()));
                }
                if ((userInfo.getMobile() != null) && !"".equals(userInfo.getMobile().trim())) {
                    list.add(criteriaBuilder.like(root.get("mobile").as(String.class), "%" + userInfo.getMobile() + "%"));
                }
                if ((userInfo.getName() != null) && !"".equals(userInfo.getName().trim())) {
                    list.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + userInfo.getName() + "%"));
                }
                if ((userInfo.getPassword() != null) && !"".equals(userInfo.getPassword().trim())) {
                    list.add(criteriaBuilder.like(root.get("password").as(String.class), "%" + userInfo.getPassword() + "%"));
                }
                if (userInfo.getUserInfoIndex() != null) {
                    list.add(criteriaBuilder.equal(root.get("userInfoIndex").as(Integer.class), userInfo.getUserInfoIndex()));
                }
                if ((userInfo.getUsername() != null) && !"".equals(userInfo.getUsername().trim())) {
                    list.add(criteriaBuilder.like(root.get("username").as(String.class), "%" + userInfo.getUsername() + "%"));
                }
                if (userInfo.getDepartment() != null) {
                    list.add(criteriaBuilder.equal(root.get("department").get("id").as(Integer.class), userInfo.getDepartment().getId()));
                }
            }
			list.add(criteriaBuilder.equal(root.get("isDelete").as(Integer.class), 0));
            return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
        }, pageable);
        Map<String, Object> map = new HashMap<>(16);
        map.put("total",all.getTotalElements());
        map.put("rows",all.getContent());
        return Result.ok(map);
    }
	/**
	 * 根据主键查询用户及角色、权限列表
	 *
	 * @param accessToken token
	 * @param id 系统标识
	 * @return 返回数据
	 */
	@Override
	public Result findRolesAndMenus(String accessToken, Integer id) {
		beanConfig.getAccessToken(accessToken);
		Optional<UserInfo> byId = userInfoRepository.findById(id);
		return ok();
	}

	/**
	 * 设置用户、组
	 * @param accessToken token
	 * @param id 用户系统标识
	 * @param groupIds 组系统标识
	 * @return 返回数据
	 */
	@Override
	public Result setUserByGroup(String accessToken, Integer id, String groupIds) {
		beanConfig.getAccessToken(accessToken);
		String[] splits = StringUtils.split(groupIds,",");
		if (splits == null || splits.length == 0){
			return fail(Tips.PARAMETER_ERROR.msg);
		}

		Optional<UserInfo> byId = userInfoRepository.findById(id);
		if (!byId.isPresent()){
			return fail(Tips.MSG_NOT.msg);
		}
		//删除原有用户对应的组
		UserInfo userInfo = byId.get();
		if (userInfo.getGroups() != null){
			userInfo.getGroups().clear();
		}
		//新增现有的用户组
		for(String split: splits){
			Optional<Group> groupOptional = groupRepository.findById(Integer.valueOf(split));
			groupOptional.ifPresent(group -> userInfo.getGroups().add(group));
		}
		return ok();
	}

	/**
	 * 设置用户权限
	 * @param accessToken token
	 * @param id 用户系统标识
	 * @param menuIds 组系统标识
	 * @return 返回数据
	 */
	@Override
	public Result setUserByMenu(String accessToken, Integer id, String menuIds) {
		UserInfo loginUser = beanConfig.getAccessToken(accessToken);
		String[] splits = StringUtils.split(menuIds,",");
		if (splits == null || splits.length == 0){
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		Optional<UserInfo> byId = userInfoRepository.findById(id);
		if (!byId.isPresent()){
			return fail(Tips.MSG_NOT.msg);
		}
		//删除原有用户对应的权限
		UserInfo userInfo = byId.get();
		if (userInfo.getMenus() != null){
			userInfo.getMenus().clear();
		}
		userInfo.setUpdateTime(LocalDateTime.now());
		userInfo.setUpdateUserId(loginUser.getId());
		//新增现有的用户权限
		for(String split: splits){
			Optional<Menu> menuOptional = menuRepository.findById(Integer.valueOf(split));
			menuOptional.ifPresent(menu -> userInfo.getMenus().add(menu));
		}
		return ok();
	}

	/**
	 * 设置用户、角色
	 * @param accessToken token
	 * @param id 用户系统标识
	 * @param roleIds 组系统标识
	 * @return 返回数据
	 */
	@Override
	public Result setUserByRoles(String accessToken, Integer id, String roleIds) {
		beanConfig.getAccessToken(accessToken);
		String[] splits = StringUtils.split(roleIds,",");
		if (splits == null || splits.length == 0){
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		Optional<UserInfo> byId = userInfoRepository.findById(id);
		if (!byId.isPresent()){
			return fail(Tips.MSG_NOT.msg);
		}
		//删除原有用户对应的角色
		UserInfo userInfo = byId.get();
		if (userInfo.getRoles() != null){
			userInfo.getRoles().clear();
		}
		//新增现有的用户角色
		for(String split: splits){
			Optional<Role> roleOptional = roleRepository.findById(Integer.valueOf(split));
			roleOptional.ifPresent(role -> userInfo.getRoles().add(role));
		}
		return ok();
	}
}

