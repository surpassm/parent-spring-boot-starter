package com.github.surpassm.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mc
 * Create date 2019/4/10 16:22
 * Version 1.0
 * Description 控制层返回前端JSON控制配置类
 */
//@Configuration
public class JsonReturnValueConfig implements InitializingBean {

	@Resource
	private RequestMappingHandlerAdapter requestMappingHandlerAdapter;
	@Resource
	private ObjectMapper objectMapper;

	@Override
	public void afterPropertiesSet() throws Exception {
		List<HandlerMethodReturnValueHandler> unmodifiableList = requestMappingHandlerAdapter.getReturnValueHandlers();
		List<HandlerMethodReturnValueHandler> list = new ArrayList<>(unmodifiableList.size());
		for (HandlerMethodReturnValueHandler returnValueHandler : unmodifiableList) {
			if (returnValueHandler instanceof RequestResponseBodyMethodProcessor) {
				list.add(new JsonReturnHandler(returnValueHandler,objectMapper));
			} else {
				list.add(returnValueHandler);
			}
		}
		requestMappingHandlerAdapter.setReturnValueHandlers(list);
	}
}
