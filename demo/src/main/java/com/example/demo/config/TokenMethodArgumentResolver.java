package com.example.demo.config;

import com.example.demo.annotation.AuthorizationToken;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

/**
 * @description:
 * @author: mc
 * @createDate: 2018/7/2 14:57
 * @updateRemark:
 * @version: 1.0
 */
@Component
public class TokenMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        if (methodParameter.getParameterType().isAssignableFrom(String.class) && methodParameter.hasParameterAnnotation(AuthorizationToken.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
		String token = (String) nativeWebRequest.getAttribute("Authorization", RequestAttributes.SCOPE_REQUEST);
        if (token != null) {
            return token;
        }
        throw new MissingServletRequestPartException("Authorization");
    }
}
