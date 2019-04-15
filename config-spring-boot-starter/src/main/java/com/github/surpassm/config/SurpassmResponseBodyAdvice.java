package com.github.surpassm.config;

import com.github.surpassm.common.jackson.Result;
import com.github.surpassm.config.annotation.SerializedField;
import com.github.surpassm.tool.util.Helper;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author mc
 * Create date 2019/4/15 10:33
 * Version 1.0
 * Description 自定义返回数据  实现 ResponseBodyAdvice
 */
//@Order(1)
//@ControllerAdvice(basePackages = "com.liaoin.demo.controller")
public class SurpassmResponseBodyAdvice implements ResponseBodyAdvice {
	/**
	 * 包含项
	 */
	private String[] includes = {};
	/**
	 * 排除项
	 */
	private String[] excludes = {};
	/**
	 * 是否加密
	 */
	private boolean encode = false;

	@Override
	public boolean supports(MethodParameter methodParameter, Class aClass) {
		//进入 beforeBodyWrite 方法前的业务判断
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
		//重新初始化为默认值
		includes = new String[]{};
		excludes = new String[]{};
		encode = false;
		//判断返回的对象是单个对象，还是list，或者是map
		if(o==null){
			return null;
		}
		if(Objects.requireNonNull(methodParameter.getMethod()).isAnnotationPresent(SerializedField.class)){
			//获取注解配置的包含和去除字段
			SerializedField serializedField = methodParameter.getMethodAnnotation(SerializedField.class);
			if (serializedField == null){
				return o;
			}
			includes = serializedField.includes();
			excludes = serializedField.excludes();
			//是否加密
			encode = serializedField.encode();
			if (o instanceof Result){
				Result result = (Result) o;
				Object data = result.getData();
				if (data instanceof List){
					//List
					List list = (List)data;
					data = handleList(list);
				}/*else if (data instanceof HashMap){
					Map  map= (Map) data;

				}*/else {
					//Single Object
					data = handleSingleObject(data);
				}
				result.setData(data);
			}
		}
		return o;
	}

	/**
	 * 处理返回值是单个enity对象
	 * @param o 当前对象
	 * @return 处理后对象
	 */
	private Object handleSingleObject(Object o){
		Map<String,Object> map = new HashMap<>(16);

		Field[] fields = o.getClass().getDeclaredFields();
		for (Field field:fields){
			//如果未配置表示全部的都返回
			if(includes.length==0 && excludes.length==0){
				String newVal = getNewVal(o, field);
				map.put(field.getName(), newVal);
			}else if(includes.length>0){
				//有限考虑包含字段
				if(Helper.isStringInArray(field.getName(), includes)){
					String newVal = getNewVal(o, field);
					map.put(field.getName(), newVal);
				}
			}else{
				//去除字段
				if(excludes.length>0){
					if(!Helper.isStringInArray(field.getName(), excludes)){
						String newVal = getNewVal(o, field);
						map.put(field.getName(), newVal);
					}
				}
			}

		}
		return map;
	}

	/**
	 * 处理返回值是列表
	 */
	private List handleList(List list){
		List retList = new ArrayList();
		for (Object o:list){
			Map map = (Map) handleSingleObject(o);
			retList.add(map);
		}
		return retList;
	}

	/**
	 * 获取加密后的新值
	 */
	private String getNewVal(Object o, Field field){
		String newVal = "";
		try {
			field.setAccessible(true);
			Object val = field.get(o);

			if(val!=null){
				if(encode){
					newVal = Helper.encode(val.toString());
				}else{
					newVal = val.toString();
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return newVal;
	}

}
