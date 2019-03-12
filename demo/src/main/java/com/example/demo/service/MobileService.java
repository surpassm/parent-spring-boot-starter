package com.example.demo.service;

import com.github.surpassm.common.jackson.Result;

import javax.servlet.http.HttpServletRequest;

/**
 * @author mc
 * Create date 2019/3/12 10:56
 * Version 1.0
 * Description
 */
public interface MobileService {
	/**
	 * 发送短信验证码
	 * @param phone
	 * @return
	 */
	Result sendPhoneMsgCode(HttpServletRequest request, String phone);
}
