package com.example.demo.controller;

import com.example.demo.entity.Menu;
import com.example.demo.service.MenuService;
import com.github.surpassm.common.jackson.Result;
import com.github.surpassm.config.annotation.AuthorizationToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
  * @author mc
  * Create date 2019-03-05 15:20:29
  * Version 1.0
  * Description 控制层
  */
@CrossOrigin
@RestController
@RequestMapping("/menu/")
@Api(tags  =  "1、权限Api")
public class MenuController {

    @Resource
    private MenuService menuService;

    @PostMapping("insert")
    @ApiOperation(value = "新增")
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
    @ApiImplicitParam(name = "Authorization", value = "授权码请以(Bearer )开头", required = true, dataType = "string", paramType = "header")
    public Result deleteGetById(@ApiParam(hidden = true)@AuthorizationToken String accessToken,
                                @ApiParam(value = "主键",required = true)@RequestParam(value = "id") Integer id) {
        return menuService.deleteGetById(accessToken,id);
    }

    @PostMapping("findById")
    @ApiOperation(value = "根据主键查询")
    @ApiImplicitParam(name = "Authorization", value = "授权码请以(Bearer )开头", required = true, dataType = "string", paramType = "header")
    public Result findById(@ApiParam(hidden = true)@AuthorizationToken String accessToken,
                           @ApiParam(value = "主键",required = true)@RequestParam(value = "id") Integer id) {
        return menuService.findById(accessToken,id);
    }

    @PostMapping("pageQuery")
    @ApiOperation(value = "条件分页查询")
    @ApiImplicitParam(name = "Authorization", value = "授权码请以(Bearer )开头", required = true, dataType = "string", paramType = "header")
    public Result pageQuery(@ApiParam(hidden = true)@AuthorizationToken String accessToken,
                            @ApiParam(value = "第几页", required = true) @RequestParam(value = "page") Integer page,
                            @ApiParam(value = "多少条",required = true)@RequestParam(value = "size") Integer size,
                            @ApiParam(value = "排序字段")@RequestParam(value = "sort",required = false) String sort,
                            Menu menu) {
        return menuService.pageQuery(accessToken,page, size, sort, menu);
    }
}
