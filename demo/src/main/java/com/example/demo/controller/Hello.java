package com.example.demo.controller;

import com.github.surpassm.common.jackson.Result;
import com.github.surpassm.config.annotation.AuthorizationToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author mc
 * Create date 2019/2/16 14:44
 * Version 1.0
 * Description
 */
@CrossOrigin
@RestController
@RequestMapping("/admin/")
@Api(tags  =  "登陆API")
public class Hello {

	@Resource
	private TokenStore redisTokenStore;

	@PostMapping("hello")
	@ApiImplicitParam(name = "Authorization", value = "授权码请以(Bearer )开头", required = true, dataType = "string", paramType = "header")
	public Result save(@ApiParam(hidden = true)@AuthorizationToken String accessToken) {
		OAuth2AccessToken oAuth2AccessToken = redisTokenStore.readAccessToken(accessToken);
		if (oAuth2AccessToken == null){
			return Result.fail("请登陆");
		}
		Map<String, Object> additionalInformation = oAuth2AccessToken.getAdditionalInformation();
		if (additionalInformation.size() == 0){
			return Result.fail("请登陆");
		}
		Object userInfo = additionalInformation.get("userInfo");
		if (userInfo == null){
			return Result.fail("请登陆");
		}
		return Result.ok(userInfo);
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
		return Result.ok(oAuth2RefreshToken);
	}
}
