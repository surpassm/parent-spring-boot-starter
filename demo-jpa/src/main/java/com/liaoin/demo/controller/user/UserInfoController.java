package com.liaoin.demo.controller.user;

import com.github.surpassm.common.constant.Constant;
import com.github.surpassm.common.jackson.Result;
import com.github.surpassm.common.service.InsertPcSimpleView;
import com.github.surpassm.common.service.UpdatePcSimpleView;
import com.github.surpassm.config.annotation.AuthorizationToken;
import com.liaoin.demo.entity.user.UserInfo;
import com.liaoin.demo.service.user.UserInfoService;
import io.swagger.annotations.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
  * @author mc
  * Create date 2019-04-10 12:49:52
  * Version 1.0
  * Description 用户控制层
  */
@CrossOrigin
@RestController
@RequestMapping("/userInfo/")
@Api(tags  =  "用户API")
public class UserInfoController {

    @Resource
    private UserInfoService userInfoService;

    @PostMapping("insert")
    @ApiOperation(value = "新增")
    @ApiResponses({
            @ApiResponse(code=Constant.SUCCESS_CODE,message=Constant.SUCCESS_MSG),
            @ApiResponse(code=Constant.FAIL_SESSION_CODE,message=Constant.FAIL_SESSION_MSG),
            @ApiResponse(code=Constant.FAIL_CODE,message=Constant.FAIL_MSG,response=Result.class)})
    @ApiImplicitParam(name = "Authorization", value = "授权码请以(Bearer )开头", required = true, dataType = "string", paramType = "header")
    public Result save(@ApiParam(hidden = true)@AuthorizationToken String accessToken,
					   @Validated(InsertPcSimpleView.class)@RequestBody UserInfo userInfo, BindingResult errors) {
        if (errors.hasErrors()){
			return Result.fail(errors.getAllErrors());
		}
        return userInfoService.insert(accessToken,userInfo);
    }

    @PostMapping("update")
    @ApiOperation(value = "修改")
    @ApiResponses({
            @ApiResponse(code=Constant.SUCCESS_CODE,message=Constant.SUCCESS_MSG),
            @ApiResponse(code=Constant.FAIL_SESSION_CODE,message=Constant.FAIL_SESSION_MSG),
            @ApiResponse(code=Constant.FAIL_CODE,message=Constant.FAIL_MSG,response=Result.class)})
    @ApiImplicitParam(name = "Authorization", value = "授权码请以(Bearer )开头", required = true, dataType = "string", paramType = "header")
    public Result update(@ApiParam(hidden = true)@AuthorizationToken String accessToken,
                         @Validated(UpdatePcSimpleView.class)@RequestBody UserInfo userInfo,BindingResult errors) {
        if (errors.hasErrors()){
			return Result.fail(errors.getAllErrors());
		}
        return userInfoService.update(accessToken,userInfo);
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
        return userInfoService.deleteGetById(accessToken,id);
    }

    @PostMapping("findById")
    @ApiOperation(value = "根据主键查询")
    @ApiResponses({
            @ApiResponse(code=Constant.FAIL_SESSION_CODE,message=Constant.FAIL_SESSION_MSG),
            @ApiResponse(code=Constant.SUCCESS_CODE,message=Constant.SUCCESS_MSG,response=UserInfo.class),
            @ApiResponse(code=Constant.FAIL_CODE,message=Constant.FAIL_MSG,response=Result.class)})
    @ApiImplicitParam(name = "Authorization", value = "授权码请以(Bearer )开头", required = true, dataType = "string", paramType = "header")
    public Result findById(@ApiParam(hidden = true)@AuthorizationToken String accessToken,
                           @ApiParam(value = "主键",required = true)@RequestParam(value = "id") Integer id) {
        return userInfoService.findById(accessToken,id);
    }

    @PostMapping("pageQuery")
    @ApiOperation(value = "条件分页查询")
    @ApiResponses({@ApiResponse(code=Constant.SUCCESS_CODE,message=Constant.SUCCESS_MSG,response=UserInfo.class),
                   @ApiResponse(code=Constant.FAIL_SESSION_CODE,message=Constant.FAIL_SESSION_MSG)})
    @ApiImplicitParam(name = "Authorization", value = "授权码请以(Bearer )开头", required = true, dataType = "string", paramType = "header")
    public Result pageQuery(@ApiParam(hidden = true)@AuthorizationToken String accessToken,
                            @ApiParam(value = "第几页", required = true,example = "1") @RequestParam(value = "page") Integer page,
                            @ApiParam(value = "多少条",required = true,example = "10")@RequestParam(value = "size") Integer size,
                            @ApiParam(value = "排序字段",example = "create_time desc")@RequestParam(value = "sort",required = false) String sort,
							@RequestBody UserInfo userInfo) {
        return userInfoService.pageQuery(accessToken,page, size, sort, userInfo);
    }
}
