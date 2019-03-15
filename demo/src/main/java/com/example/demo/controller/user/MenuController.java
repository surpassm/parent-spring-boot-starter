package com.example.demo.controller.user;

import com.example.demo.entity.user.Group;
import com.example.demo.entity.user.Menu;
import com.example.demo.service.user.MenuService;
import com.github.surpassm.common.constant.Constant;
import com.github.surpassm.common.jackson.Result;
import com.github.surpassm.config.annotation.AuthorizationToken;
import io.swagger.annotations.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
  * @author mc
  * Create date 2019-03-14 20:41:03
  * Version 1.0
  * Description 权限控制层
  */
@CrossOrigin
@RestController
@RequestMapping("/menu/")
@Api(tags  =  "权限Api")
public class MenuController {

    @Resource
    private MenuService menuService;

    @PostMapping("insert")
    @ApiOperation(value = "新增")
    @ApiResponses({
            @ApiResponse(code=Constant.SUCCESS_CODE,message=Constant.SUCCESS_MSG),
            @ApiResponse(code=Constant.FAIL_SESSION_CODE,message=Constant.FAIL_SESSION_MSG),
            @ApiResponse(code=Constant.FAIL_CODE,message=Constant.FAIL_MSG,response=Result.class)})
    @ApiImplicitParam(name = "Authorization", value = "授权码请以(Bearer )开头", required = true, dataType = "string", paramType = "header")
    public Result insert(@ApiParam(hidden = true)@AuthorizationToken String accessToken,
						 @Validated(Menu.MenuInsertPcSimpleView.class) Menu menu, BindingResult errors) {
        if (errors.hasErrors()){
			return Result.fail(errors.getAllErrors());
		}
        return menuService.insert(accessToken,menu);
    }

    @PostMapping("update")
    @ApiOperation(value = "修改")
    @ApiResponses({
            @ApiResponse(code=Constant.SUCCESS_CODE,message=Constant.SUCCESS_MSG),
            @ApiResponse(code=Constant.FAIL_SESSION_CODE,message=Constant.FAIL_SESSION_MSG),
            @ApiResponse(code=Constant.FAIL_CODE,message=Constant.FAIL_MSG,response=Result.class)})
    @ApiImplicitParam(name = "Authorization", value = "授权码请以(Bearer )开头", required = true, dataType = "string", paramType = "header")
    public Result update(@ApiParam(hidden = true)@AuthorizationToken String accessToken,
                         @Validated(Menu.MenuUpdatePcSimpleView.class) Menu menu,BindingResult errors) {
        if (errors.hasErrors()){
			return Result.fail(errors.getAllErrors());
		}
        return menuService.update(accessToken,menu);
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
        return menuService.deleteGetById(accessToken,id);
    }

    @PostMapping("findById")
    @ApiOperation(value = "根据主键查询")
    @ApiResponses({
            @ApiResponse(code=Constant.FAIL_SESSION_CODE,message=Constant.FAIL_SESSION_MSG),
            @ApiResponse(code=Constant.SUCCESS_CODE,message=Constant.SUCCESS_MSG,response=Menu.class),
            @ApiResponse(code=Constant.FAIL_CODE,message=Constant.FAIL_MSG,response=Result.class)})
    @ApiImplicitParam(name = "Authorization", value = "授权码请以(Bearer )开头", required = true, dataType = "string", paramType = "header")
    public Result findById(@ApiParam(hidden = true)@AuthorizationToken String accessToken,
                           @ApiParam(value = "主键",required = true)@RequestParam(value = "id") Integer id) {
        return menuService.findById(accessToken,id);
    }

    @PostMapping("pageQuery")
    @ApiOperation(value = "条件分页查询")
    @ApiResponses({@ApiResponse(code=Constant.SUCCESS_CODE,message=Constant.SUCCESS_MSG,response=Menu.class),
                   @ApiResponse(code=Constant.FAIL_SESSION_CODE,message=Constant.FAIL_SESSION_MSG)})
    @ApiImplicitParam(name = "Authorization", value = "授权码请以(Bearer )开头", required = true, dataType = "string", paramType = "header")
    public Result pageQuery(@ApiParam(hidden = true)@AuthorizationToken String accessToken,
                            @ApiParam(value = "第几页", required = true) @RequestParam(value = "page") Integer page,
                            @ApiParam(value = "多少条",required = true)@RequestParam(value = "size") Integer size,
                            @ApiParam(value = "排序字段")@RequestParam(value = "sort",required = false) String sort,
                            Menu menu) {
        return menuService.pageQuery(accessToken,page, size, sort, menu);
    }

	@PostMapping("findChildren")
	@ApiOperation(value = "根据父级Id查询所有子级")
	@ApiResponses({
			@ApiResponse(code=Constant.FAIL_SESSION_CODE,message=Constant.FAIL_SESSION_MSG),
			@ApiResponse(code=Constant.SUCCESS_CODE,message=Constant.SUCCESS_MSG,response=Menu.class),
			@ApiResponse(code=Constant.FAIL_CODE,message=Constant.FAIL_MSG,response=Result.class)})
	@ApiImplicitParam(name = "Authorization", value = "授权码请以(Bearer )开头", required = true, dataType = "string", paramType = "header")
	public Result getParentId(@ApiParam(hidden = true)@AuthorizationToken String accessToken,
							  @ApiParam(value = "主键",required = true)@RequestParam(value = "parentId")@NotNull Integer parentId) {
		return menuService.getParentId(accessToken,parentId);
	}

	@PostMapping("findByOnlyAndChildren")
	@ApiOperation(value = "根据主键查询自己和所有子级")
	@ApiResponses({
			@ApiResponse(code=Constant.FAIL_SESSION_CODE,message=Constant.FAIL_SESSION_MSG),
			@ApiResponse(code=Constant.SUCCESS_CODE,message=Constant.SUCCESS_MSG,response=Menu.class),
			@ApiResponse(code=Constant.FAIL_CODE,message=Constant.FAIL_MSG,response=Result.class)})
	@ApiImplicitParam(name = "Authorization", value = "授权码请以(Bearer )开头", required = true, dataType = "string", paramType = "header")
	public Result findByOnlyAndChildren(@ApiParam(hidden = true)@AuthorizationToken String accessToken,
										@ApiParam(value = "主键",required = true)@RequestParam(value = "id")@NotNull Integer id) {
		return menuService.findByOnlyAndChildren(accessToken,id);
	}
}
