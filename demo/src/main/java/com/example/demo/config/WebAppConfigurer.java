package com.example.demo.config;

import com.example.demo.util.FileUtils;
import com.github.surpassm.security.properties.SecurityProperties;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @description:
 * @author:         mc
 * @createDate:     2018/7/2 14:58
 * @updateRemark:
 * @version:        1.0
 */
@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {
    @Resource
    private TokenMethodArgumentResolver tokenMethodArgumentResolver;
    @Resource
	private TokenInterceptor tokenInterceptor;
    @Resource
	private SecurityProperties securityProperties;
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(tokenMethodArgumentResolver);
    }

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		InterceptorRegistration interceptorRegistration = registry.addInterceptor(tokenInterceptor);
		String[] noVerify = securityProperties.getNoVerify();
		if (noVerify != null && noVerify.length != 0){
			interceptorRegistration.excludePathPatterns(Arrays.asList(noVerify));
		}
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry){
		try {
			String path = FileUtils.rootPath();
			registry.addResourceHandler("/upload/**").addResourceLocations("file:///"+path+"/upload/");
			registry.addResourceHandler("/static/**").addResourceLocations("file:///"+path+"/static/");
		}catch (Exception e){
		}
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST", "OPTIONS", "PUT")
				.allowedHeaders("Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method","Access-Control-Request-Headers")
				.exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials")
				.allowCredentials(true).maxAge(36000);
	}
}
