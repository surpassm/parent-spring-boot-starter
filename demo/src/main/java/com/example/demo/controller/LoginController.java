package com.example.demo.controller;

import com.example.demo.entity.UserInfo;
import com.github.surpassm.common.jackson.Result;
import com.github.surpassm.config.BeanConfig;
import com.github.surpassm.config.annotation.AuthorizationToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
@RequestMapping("/Login/")
@Api(tags  =  "6、TokenAPI")
public class LoginController {

	@Resource
	private TokenStore redisTokenStore;
	@Resource
	private BeanConfig beanConfig;

	@PostMapping("hello")
	@ApiOperation(value = "使用token获取用户基本信息")
	@ApiImplicitParam(name = "Authorization", value = "授权码请以(Bearer )开头", required = true, dataType = "string", paramType = "header")
	public Result save(@ApiParam(hidden = true)@AuthorizationToken String accessToken) {
		return ok(beanConfig.getAccessToken(accessToken,UserInfo.class));
	}

	@PostMapping("refreshToken")
	@ApiOperation(value = "刷新token时效")
	@ApiImplicitParam(name = "Authorization", value = "授权码请以(Bearer )开头", required = true, dataType = "string", paramType = "header")
	public Result refreshToken(@ApiParam(hidden = true)@AuthorizationToken String accessToken,
							   @ApiParam(value = "刷新token")@RequestParam String refreshToken) {
		OAuth2AccessToken oAuth2AccessToken = redisTokenStore.readAccessToken(accessToken);
		if (oAuth2AccessToken == null){
			return Result.fail("请登陆");
		}
		OAuth2RefreshToken oAuth2RefreshToken = redisTokenStore.readRefreshToken(refreshToken);
		return ok(oAuth2RefreshToken);
	}
}
