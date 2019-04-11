package com.github.surpassm.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.surpassm.config.annotation.JsonFieldFilter;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletResponse;

/**
 * @author mc
 * Create date 2019/4/10 15:51
 * Version 1.0
 * Description
 */
public class JsonReturnHandler implements HandlerMethodReturnValueHandler {

	private final HandlerMethodReturnValueHandler delegate;
	private final ObjectMapper objectMapper;

	public JsonReturnHandler(HandlerMethodReturnValueHandler delegate,ObjectMapper objectMapper){
		this.delegate = delegate;
		this.objectMapper = objectMapper;
	}
	@Override
	public boolean supportsReturnType(MethodParameter methodParameter) {
		return methodParameter.hasMethodAnnotation(JsonFieldFilter.class);
	}

	@Override
	public void handleReturnValue(Object o, MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest) throws Exception {
		modelAndViewContainer.setRequestHandled(true);
		JsonFilterSerializer serializer = new JsonFilterSerializer();
		//如果有JsonFieldFilter注解，则过滤返回的对象returnObject
		if(methodParameter.hasMethodAnnotation(JsonFieldFilter.class)) {
			JsonFieldFilter jsonFilter = methodParameter.getMethodAnnotation(JsonFieldFilter.class);
			//调用过滤方法
			if (jsonFilter != null){
				serializer.filter(jsonFilter.type(), jsonFilter.include(), jsonFilter.exclude());
			}
		}
		HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
		if (response != null) {
			response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
			response.getWriter().write(serializer.toJson(o));
		}
	}
}
