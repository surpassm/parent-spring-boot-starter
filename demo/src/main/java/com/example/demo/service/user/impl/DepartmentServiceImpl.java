package com.example.demo.service.user.impl;

import com.example.demo.entity.user.Department;
import com.example.demo.entity.user.UserInfo;
import com.example.demo.mapper.user.DepartmentMapper;
import com.example.demo.mapper.user.UserInfoMapper;
import com.example.demo.service.user.DepartmentService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.surpassm.common.jackson.Tips;
import com.example.demo.security.BeanConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.github.surpassm.common.jackson.Result;

import static com.github.surpassm.common.jackson.Result.fail;
import static com.github.surpassm.common.jackson.Result.ok;

/**
  * @author mc
  * Create date 2019-03-05 15:20:29
  * Version 1.0
  * Description 实现类
  */
@Slf4j
@Service
@Transactional(rollbackFor={RuntimeException.class, Exception.class})
public class DepartmentServiceImpl implements DepartmentService {
    @Resource
    private DepartmentMapper departmentMapper;
    @Resource
	private BeanConfig beanConfig;
	@Resource
	private UserInfoMapper userInfoMapper;

    @Override
    public Result insert(String accessToken, Department department) {
        if (department == null){
            return fail(Tips.PARAMETER_ERROR.msg);
        }
		UserInfo loginUserInfo = beanConfig.getAccessToken(accessToken);
		department.setCreateUserId(loginUserInfo.getId());
        department.setCreateTime(new Date());
        department.setIsDelete(0);
        departmentMapper.insertSelectiveCustom(department);
        return ok();
    }

    @Override
    public Result update(String accessToken,Department department) {
        if (department == null){
            return fail(Tips.PARAMETER_ERROR.msg);
        }
        UserInfo loginUserInfo = beanConfig.getAccessToken(accessToken);
        department.setUpdateUserId(loginUserInfo.getId());
        department.setUpdateTime(new Date());
        departmentMapper.updateByPrimaryKeySelective(department);
        return ok();
    }

    @Override
    public Result deleteGetById(String accessToken,Integer id){
		if (id == null){
			return fail(Tips.PARAMETER_ERROR.msg);
		}
		Department department = departmentMapper.selectByPrimaryKey(id);
		if(department == null){
			return fail(Tips.MSG_NOT.msg);
		}
		UserInfo userInfo = new UserInfo();
		userInfo.setDepartmentId(department.getId());
		int userInfoCount = userInfoMapper.selectCount(userInfo);
		if (userInfoCount != 0){
			return fail(Tips.AssociatedDataExistsAndCannotBeDeleted.msg);
		}
		int departmentCount = departmentMapper.selectCount(Department.builder().parentId(department.getId()).build());
		if (departmentCount != 0){
			return fail(Tips.AssociatedDataExistsAndCannotBeDeleted.msg);
		}
		UserInfo loginUserInfo = beanConfig.getAccessToken(accessToken);
		department.setDeleteUserId(loginUserInfo.getId());
		department.setDeleteTime(new Date());
		department.setIsDelete(1);
		departmentMapper.updateByPrimaryKeySelective(department);
		return ok();
    }


    @Override
    public Result findById(String accessToken,Integer id) {
        if (id == null){
            return fail(Tips.PARAMETER_ERROR.msg);
        }
		Department department = departmentMapper.selectByPrimaryKey(id);
        if (department == null){
			return fail(Tips.MSG_NOT.msg);
		}
        return ok(department);

    }

    @Override
    public Result pageQuery(String accessToken,Integer page, Integer size, String sort, Department department) {
        page = null  == page ? 1 : page;
        size = null  == size ? 10 : size;
        PageHelper.startPage(page, size);
        Example.Builder builder = new Example.Builder(Department.class);
        builder.where(WeekendSqls.<Department>custom().andEqualTo(Department::getIsDelete, 0));
        if(department != null){
        if (department.getId() != null){
            builder.where(WeekendSqls.<Department>custom().andEqualTo(Department::getId,department.getId()));
        }
        if (department.getCreateTime() != null){
            builder.where(WeekendSqls.<Department>custom().andEqualTo(Department::getCreateTime,department.getCreateTime()));
        }
        if (department.getCreateUserId() != null){
            builder.where(WeekendSqls.<Department>custom().andEqualTo(Department::getCreateUserId,department.getCreateUserId()));
        }
        if (department.getDeleteTime() != null){
            builder.where(WeekendSqls.<Department>custom().andEqualTo(Department::getDeleteTime,department.getDeleteTime()));
        }
        if (department.getDeleteUserId() != null){
            builder.where(WeekendSqls.<Department>custom().andEqualTo(Department::getDeleteUserId,department.getDeleteUserId()));
        }
        if (department.getDepartmentIndex() != null){
            builder.where(WeekendSqls.<Department>custom().andEqualTo(Department::getDepartmentIndex,department.getDepartmentIndex()));
        }
        if (department.getIsDelete() != null){
            builder.where(WeekendSqls.<Department>custom().andEqualTo(Department::getIsDelete,department.getIsDelete()));
        }
        if (department.getName() != null && !"".equals(department.getName().trim())){
            builder.where(WeekendSqls.<Department>custom().andLike(Department::getName,"%"+department.getName()+"%"));
        }
        if (department.getParentId() != null){
            builder.where(WeekendSqls.<Department>custom().andEqualTo(Department::getParentId,department.getParentId()));
        }
        if (department.getUpdateTime() != null){
            builder.where(WeekendSqls.<Department>custom().andEqualTo(Department::getUpdateTime,department.getUpdateTime()));
        }
        if (department.getUpdateUserId() != null){
            builder.where(WeekendSqls.<Department>custom().andEqualTo(Department::getUpdateUserId,department.getUpdateUserId()));
        }
        }
        Page<Department> all = (Page<Department>) departmentMapper.selectByExample(builder.build());
        Map<String, Object> map = new HashMap<>(16);
        map.put("total",all.getTotal());
        map.put("rows",all.getResult());
        return ok(map);
    }
}

