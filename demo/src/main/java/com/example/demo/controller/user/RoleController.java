package com.example.demo.controller.user;

import com.example.demo.entity.user.Role;
import com.example.demo.service.user.RoleService;
import com.github.surpassm.common.jackson.Result;
import com.github.surpassm.config.annotation.AuthorizationToken;
import io.swagger.annotations.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;


/**
  * @author mc
  * Create date 2019-03-05 15:20:29
  * Version 1.0
  * Description 控制层
  */
@CrossOrigin
@RestController
@RequestMapping("/role/")
@Api(tags  =  "4、角色Api")
public class RoleController {

    @Resource
    private RoleService roleService;

    @PostMapping("insert")
    @ApiOperation(value = "新增")
    @ApiImplicitParam(name = "Authorization", value = "授权码请以(Bearer )开头", required = true, dataType = "string", paramType = "header")
    public Result insert(@ApiParam(hidden = true)@AuthorizationToken String accessToken,
						 @Valid @RequestBody Role role, BindingResult errors) {
        if (errors.hasErrors()){
			return Result.fail(errors.getAllErrors());
		}
        return roleService.insert(accessToken,role);
    }

    @PostMapping("update")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(name = "Authorization", value = "授权码请以(Bearer )开头", required = true, dataType = "string", paramType = "header")
    public Result update(@ApiParam(hidden = true)@AuthorizationToken String accessToken,
						 @Valid @RequestBody Role role,BindingResult errors) {
        if (errors.hasErrors()){
			return Result.fail(errors.getAllErrors());
		}
        return roleService.update(accessToken,role);
    }

    @PostMapping("getById")
    @ApiOperation(value = "根据主键删除")
    @ApiImplicitParam(name = "Authorization", value = "授权码请以(Bearer )开头", required = true, dataType = "string", paramType = "header")
    public Result deleteGetById(@ApiParam(hidden = true)@AuthorizationToken String accessToken,
                                @ApiParam(value = "主键",required = true)@RequestParam(value = "id") Integer id) {
        return roleService.deleteGetById(accessToken,id);
    }

    @PostMapping("findById")
    @ApiOperation(value = "根据主键查询")
	@ApiResponses({
			@ApiResponse(code=200,message="0k",response=Role.class),
			@ApiResponse(code=400,message="失败",response=Result.class)
	})
    @ApiImplicitParam(name = "Authorization", value = "授权码请以(Bearer )开头", required = true, dataType = "string", paramType = "header")
    public Result findById(@ApiParam(hidden = true)@AuthorizationToken String accessToken,
                           @ApiParam(value = "主键",required = true)@RequestParam(value = "id") Integer id) {
        return roleService.findById(accessToken,id);
    }

    @PostMapping("pageQuery")
    @ApiOperation(value = "条件分页查询")
	@ApiResponse(code=0,message="0k",response=Role.class)
    @ApiImplicitParam(name = "Authorization", value = "授权码请以(Bearer )开头", required = true, dataType = "string", paramType = "header")
    public Result pageQuery(@ApiParam(hidden = true)@AuthorizationToken String accessToken,
                            @ApiParam(value = "第几页", required = true) @RequestParam(value = "page") Integer page,
                            @ApiParam(value = "多少条",required = true)@RequestParam(value = "size") Integer size,
                            @ApiParam(value = "排序字段")@RequestParam(value = "sort",required = false) String sort,
							@RequestBody Role role) {
        return roleService.pageQuery(accessToken,page, size, sort, role);
    }
}
