package com.github.surpassm.config;

import com.github.surpassm.security.properties.SecurityProperties;
import com.github.surpassm.tool.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author mc
 * Create date 2019/3/4 15:53
 * Version 1.0
 * Description
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
