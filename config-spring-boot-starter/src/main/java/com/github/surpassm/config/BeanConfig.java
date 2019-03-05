package com.github.surpassm.config;

import com.github.surpassm.security.exception.SurpassmAuthenticationException;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author mc
 * Create date 2019/3/4 15:53
 * Version 1.0
 * Description
 */
@Configuration
public class BeanConfig {

	@Resource
	private TokenStore redisTokenStore;

	public Object getAccessToken(String accessToken){
		OAuth2AccessToken oAuth2AccessToken = redisTokenStore.readAccessToken(accessToken);
		if (oAuth2AccessToken == null){
			throw new SurpassmAuthenticationException("token已失效");
		}
		Map<String, Object> additionalInformation = oAuth2AccessToken.getAdditionalInformation();
		if (additionalInformation.size() == 0){
			throw new SurpassmAuthenticationException("请登陆");
		}
		Object object = additionalInformation.get("userInfo");
		if (object == null){
			throw new SurpassmAuthenticationException("请登陆");
		}
			return object;
	}
}
