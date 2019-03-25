package com.example.demo.service.common.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.surpassm.common.jackson.Result;
import com.github.surpassm.common.jackson.Tips;
import com.github.surpassm.common.pojo.File;
import com.github.surpassm.common.pojo.ValidateCode;
import com.github.surpassm.security.code.sms.SmsCodeSender;
import com.github.surpassm.security.exception.SurpassmAuthenticationException;
import com.github.surpassm.security.properties.SecurityProperties;
import com.github.surpassm.tool.util.JsonUtils;
import com.github.surpassm.tool.util.OrderUtil;
import com.github.surpassm.tool.util.ValidateUtil;
import com.liaoin.bridge.entity.*;
import com.liaoin.bridge.mapper.*;
import com.liaoin.bridge.security.BeanConfig;
import com.liaoin.bridge.service.MobileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.github.surpassm.common.jackson.Result.fail;
import static com.github.surpassm.common.jackson.Result.ok;

/**
 * @author mc
 * Create date 2019/3/12 10:56
 * Version 1.1
 * Description
 */
@Slf4j
@Service
@Transactional(rollbackFor={RuntimeException.class, Exception.class})
public class MobileServiceImpl implements MobileService {

	@Resource
	private BeanConfig beanConfig;
	@Resource
	private StringRedisTemplate stringRedisTemplate;
	@Resource
	private SecurityProperties securityProperties;
	@Resource
	private SmsCodeSender smsCodeSender;
	@Resource
	private UserInfoMapper userInfoMapper;
	@Resource
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Resource
	private BridgeMapper bridgeMapper;
	@Resource
	private RiskSourcesMapper riskSourcesMapper;
	@Resource
	private RiskSourcesFileMapper riskSourcesFileMapper;
	@Resource
	private RiskHandleProcessMapper riskHandleProcessMapper;
	@Resource
	private ObjectMapper objectMapper;

	@Override
	public Result sendPhoneMsgCode(HttpServletRequest request, String phone) {
		if (!ValidateUtil.isMobilePhone(phone)){
			return fail(Tips.phoneFormatError.msg);
		}
		Boolean isData = stringRedisTemplate.hasKey(phone);
		if (isData !=null && isData){
			return fail("请稍后再试");
		}

		//生成6位短信码并设定过期时间
		String code = RandomStringUtils.randomNumeric(securityProperties.getSms().getLength());
		ValidateCode validateCode = new ValidateCode(code, securityProperties.getSms().getExpireIn());
		stringRedisTemplate.opsForValue().set(phone,code,securityProperties.getSms().getExpireIn(),TimeUnit.SECONDS);

		String key;
		try {
			key= getKey(new ServletWebRequest(request));
		}catch (SurpassmAuthenticationException e){
			return fail("设备ID参数不能为空");
		}
		//防短信轰炸
		String stringRedisValue = stringRedisTemplate.opsForValue().get(key + ":" + phone);
		if (stringRedisValue != null){
			ValidateCode validate = JsonUtils.jsonToPojo(stringRedisValue,ValidateCode.class,objectMapper);
			if (validate != null){
				if (validate.getSendLimit() < securityProperties.getSms().getLimit()){
					int limit = validate.getSendLimit() + 1;
					validate.setSendLimit(limit);
					stringRedisTemplate.opsForValue().set(key + ":" + phone,Objects.requireNonNull(JsonUtils.objectToJson(validate, objectMapper)),securityProperties.getSms().getLimitDuration(),TimeUnit.MINUTES);
				}else {
					return fail("超出使用限制，暂时冻结使用");
				}
			}else {
				return fail("服务异常,无法使用");
			}
		}else {
			stringRedisTemplate.opsForValue().set(key + ":" + phone,Objects.requireNonNull(JsonUtils.objectToJson(validateCode, objectMapper)),securityProperties.getSms().getLimitDuration(),TimeUnit.MINUTES);
		}
		//发送具体业务逻辑
		smsCodeSender.send(new ServletWebRequest(request),phone,code);
		return ok(validateCode);
	}

	private String getKey(WebRequest request) throws SurpassmAuthenticationException {
		String deviceId = request.getHeader(securityProperties.getSms().getLimitKey());
		if (StringUtils.isBlank(deviceId)){

			throw new SurpassmAuthenticationException("设备ID参数不能为空");
		}
		return securityProperties.getSms().getLimitKey();
	}
}
