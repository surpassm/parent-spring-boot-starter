package com.example.demo.controller.user;

import com.github.surpassm.common.jackson.Result;
import com.example.demo.security.BeanConfig;
import com.github.surpassm.config.annotation.AuthorizationToken;
import com.github.surpassm.security.handler.SurpassmAuthenticationSuccessHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.io.IOException;

import static com.github.surpassm.common.jackson.Result.fail;
import static com.github.surpassm.common.jackson.Result.ok;


/**
 * @author mc
 * Create date 2019/2/16 14:44
 * Version 1.0
 * Description
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/login/")
@Api(tags  =  "TokenAPI")
public class LoginController {

	@Resource
	private BeanConfig beanConfig;
	@Resource
	private SurpassmAuthenticationSuccessHandler surpassmAuthenticationSuccessHandler;

	@PostMapping("hello")
	@ApiOperation(value = "使用token获取用户基本信息")
	@ApiImplicitParam(name = "Authorization", value = "授权码请以(Bearer )开头", required = true, dataType = "string", paramType = "header")
	public Result save(@ApiParam(hidden = true)@AuthorizationToken String accessToken) {
		return ok(beanConfig.getAccessToken(accessToken));
	}

	@PostMapping("refreshToken")
	@ApiOperation(value = "刷新token时效")
	public Result refreshToken(@ApiParam(value = "刷新token")@RequestParam String refreshToken,
							   @ApiParam(value = "head 应用账号密码Basic64位加密")@RequestParam String head) {
		try {
			OAuth2AccessToken refresh = surpassmAuthenticationSuccessHandler.refresh(refreshToken, head);
			return ok(refresh);
		} catch (IOException e) {
			e.printStackTrace();
			return fail();
		}

	}
}
