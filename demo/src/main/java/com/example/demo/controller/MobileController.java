package com.example.demo.controller;

import com.example.demo.service.MobileService;
import com.github.surpassm.common.jackson.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;

/**
 * @author mc
 * Create date 2019/3/12 10:55
 * Version 1.0
 * Description
 */
@CrossOrigin
@RestController
@RequestMapping("/mobile/")
@Api(tags = "移动端Api")
public class MobileController {

	@Resource
	private MobileService mobileService;

	@PostMapping("getPhone")
	@ApiOperation(value = "发送短信验证码")
	public Result sendPhoneMsgCode(HttpServletRequest request,
								   @ApiParam(value = "手机号码", required = true) @RequestParam(value = "phone") @NotEmpty String phone) {
		return mobileService.sendPhoneMsgCode(request, phone);
	}

}
