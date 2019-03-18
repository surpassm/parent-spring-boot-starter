package com.github.surpassm.security.properties.sms;

import lombok.Data;

/**
 * @author mc
 * @version 1.0
 * 短信验证码属性配置
 */

@Data
public class SmsCodeProperties {
	/**
	 * 长度
	 */
	private int length = 6;
	/**
	 * 过期时间 单位：秒
	 */
	private int expireIn = 60;
	/**
	 * 那些拦截需要验证码
	 */
	private String url;
}
