package com.example.demo.service;

import com.github.surpassm.security.code.sms.SmsCodeSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author mc
 * Create date 2019/3/12 12:55
 * Version 1.0
 * Description
 */
@Slf4j
@Service
public class SmsCodeSenderImpl implements SmsCodeSender {

	@Override
	public void send(ServletWebRequest request, String mobile, String code) {
		log.info("向手机"+mobile+"发送短信验证码"+code);
	}
}
