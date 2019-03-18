package com.example.demo.controller.user;

import com.example.demo.entity.user.Role;
import com.example.demo.service.user.RoleService;
import com.github.surpassm.common.constant.Constant;
import com.github.surpassm.common.jackson.Result;
import com.github.surpassm.config.annotation.AuthorizationToken;
import io.swagger.annotations.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
  * @author mc
  * Create date 2019-03-14 20:41:03
  * Version 1.0
  * Description 控制层
  */
@CrossOrigin
@RestController
@RequestMapping("/role/")
@Api(tags  =  "角色Api")
public class RoleController {

    @Resource
    private RoleService roleService;

    @PostMapping("insert")
    @ApiOperation(value = "新增")
    @ApiResponses({
            @ApiResponse(code=Constant.SUCCESS_CODE,message=Constant.SUCCESS_MSG),
            @ApiResponse(code=Constant.FAIL_SESSION_CODE,message=Constant.FAIL_SESSION_MSG),
            @ApiResponse(code=Constant.FAIL_CODE,message=Constant.FAIL_MSG,response=Result.class)})
    @ApiImplicitParam(name = "Authorization", value = "授权码请以(Bearer )开头", required = true, dataType = "string", paramType = "header")
    public Result insert(@ApiParam(hidden = true)@AuthorizationToken String accessToken,
						 @Validated(Role.RoleInsertPcSimpleView.class)@RequestBody Role role, BindingResult errors) {
        if (errors.hasErrors()){
			return Result.fail(errors.getAllErrors());
		}
        return roleService.insert(accessToken,role);
    }

    @PostMapping("update")
    @ApiOperation(value = "修改")
    @ApiResponses({
            @ApiResponse(code=Constant.SUCCESS_CODE,message=Constant.SUCCESS_MSG),
            @ApiResponse(code=Constant.FAIL_SESSION_CODE,message=Constant.FAIL_SESSION_MSG),
            @ApiResponse(code=Constant.FAIL_CODE,message=Constant.FAIL_MSG,response=Result.class)})
    @ApiImplicitParam(name = "Authorization", value = "授权码请以(Bearer )开头", required = true, dataType = "string", paramType = "header")
    public Result update(@ApiParam(hidden = true)@AuthorizationToken String accessToken,
                         @Validated(Role.RoleUpdatePcSimpleView.class) @RequestBody Role role,BindingResult errors) {
        if (errors.hasErrors()){
			return Result.fail(errors.getAllErrors());
		}
        return roleService.update(accessToken,role);
    }

    @PostMapping("getById")
    @ApiOperation(value = "根据主键删除")
    @ApiResponses({
            @ApiResponse(code=Constant.SUCCESS_CODE,message=Constant.SUCCESS_MSG),
            @ApiResponse(code=Constant.FAIL_SESSION_CODE,message=Constant.FAIL_SESSION_MSG),
            @ApiResponse(code=Constant.FAIL_CODE,message=Constant.FAIL_MSG,response=Result.class)})
    @ApiImplicitParam(name = "Authorization", value = "授权码请以(Bearer )开头", required = true, dataType = "string", paramType = "header")
    public Result deleteGetById(@ApiParam(hidden = true)@AuthorizationToken String accessToken,
                                @ApiParam(value = "主键",required = true)@RequestParam(value = "id") Integer id) {
        return roleService.deleteGetById(accessToken,id);
    }

    @PostMapping("findById")
    @ApiOperation(value = "根据主键查询")
    @ApiResponses({
            @ApiResponse(code=Constant.FAIL_SESSION_CODE,message=Constant.FAIL_SESSION_MSG),
            @ApiResponse(code=Constant.SUCCESS_CODE,message=Constant.SUCCESS_MSG,response=Role.class),
            @ApiResponse(code=Constant.FAIL_CODE,message=Constant.FAIL_MSG,response=Result.class)})
    @ApiImplicitParam(name = "Authorization", value = "授权码请以(Bearer )开头", required = true, dataType = "string", paramType = "header")
    public Result findById(@ApiParam(hidden = true)@AuthorizationToken String accessToken,
                           @ApiParam(value = "主键",required = true)@RequestParam(value = "id") Integer id) {
        return roleService.findById(accessToken,id);
    }

    @PostMapping("pageQuery")
    @ApiOperation(value = "条件分页查询")
    @ApiResponses({@ApiResponse(code=Constant.SUCCESS_CODE,message=Constant.SUCCESS_MSG,response=Role.class),
                   @ApiResponse(code=Constant.FAIL_SESSION_CODE,message=Constant.FAIL_SESSION_MSG)})
    @ApiImplicitParam(name = "Authorization", value = "授权码请以(Bearer )开头", required = true, dataType = "string", paramType = "header")
    public Result pageQuery(@ApiParam(hidden = true)@AuthorizationToken String accessToken,
                            @ApiParam(value = "第几页", required = true) @RequestParam(value = "page") Integer page,
                            @ApiParam(value = "多少条",required = true)@RequestParam(value = "size") Integer size,
                            @ApiParam(value = "排序字段")@RequestParam(value = "sort",required = false) String sort,
							@RequestBody Role role) {
        return roleService.pageQuery(accessToken,page, size, sort, role);
    }

	@PostMapping("findMenus")
	@ApiOperation(value = "根据主键查询角色及权限列表")
	@ApiResponses({@ApiResponse(code=Constant.FAIL_SESSION_CODE,message=Constant.FAIL_SESSION_MSG),
				   @ApiResponse(code=Constant.SUCCESS_CODE,message=Constant.SUCCESS_MSG,response=Role.class),
				   @ApiResponse(code=Constant.FAIL_CODE,message=Constant.FAIL_MSG,response=Result.class)})
	@ApiImplicitParam(name = "Authorization", value = "授权码请以(Bearer )开头", required = true, dataType = "string", paramType = "header")
	public Result findMenus(@ApiParam(hidden = true)@AuthorizationToken String accessToken,
						    @ApiParam(value = "主键",required = true)@RequestParam(value = "id")@NotNull Integer id) {
		return roleService.findMenus(accessToken,id);
	}

	@PostMapping("setRoleByMenu")
	@ApiOperation(value = "设置用户权限",notes = "每次均需传全部权限ID，会把角色原有的所有权限做物理删除")
	@ApiResponses({@ApiResponse(code=Constant.FAIL_SESSION_CODE,message=Constant.FAIL_SESSION_MSG),
			@ApiResponse(code=Constant.SUCCESS_CODE,message=Constant.SUCCESS_MSG),
			@ApiResponse(code=Constant.FAIL_CODE,message=Constant.FAIL_MSG,response=Result.class)})
	@ApiImplicitParam(name = "Authorization", value = "授权码请以(Bearer )开头", required = true, dataType = "string", paramType = "header")
	public Result setRoleByMenu(@ApiParam(hidden = true)@AuthorizationToken String accessToken,
								@ApiParam(value = "角色系统标识",required = true)@RequestParam(value = "id")@NotNull Integer id,
								@ApiParam(value = "权限系统标识 多个权限请使用 ，分割",required = true)@RequestParam(value = "menuId")@NotEmpty String menuId) {
		return roleService.setRoleByMenu(accessToken,id,menuId);
	}

}
