package com.github.surpassm.security.common.jackson;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 前端JSON返回格式,自定义响应格式
 *
 * @author mc
 * version 1.0
 * date 2018/10/30 12:52
 * description
 */
public class Result implements Serializable {
	/**
	 * 定义jackson对象
	 */
	private static final ObjectMapper MAPPER = new ObjectMapper();

	/**
	 * 响应业务状态
	 */
	private Integer state;

	/**
	 * 响应消息
	 */
	private String message;

	/**
	 * 响应中的数据
	 */
	private Object data = "";

	public Result(Integer state, String message) {
		this.state = state;
		this.message = message;
	}

	public static Map buildMap(String name, Object object) {
		Map<String, Object> map = new HashMap(16);
		map.put("name", name);
		map.put("value", object);
		return map;
	}

	public static Map resultMap(String key, Object object) {
		Map<String, Object> map = new HashMap(16);
		map.put(key, object);
		return map;
	}

	public static Result build(Integer state, String message, Object data) {
		return new Result(state, message, data);
	}

	public static Result ok(Object data) {
		return new Result(data);
	}

	public static Result ok() {
		return new Result(new ArrayList<>());
	}

	public Result() {

	}

	public static Result build(Integer state, String message) {
		return new Result(state, message, null);
	}

	public static Result fail() {
		return new Result(Tips.FAIL.code, Tips.FAIL.msg);
	}

	public static Result fail(String msg) {
		return new Result(Tips.FAIL.code, msg);
	}

	public static Result fail(Integer code, String msg) {
		return new Result(code, msg);
	}

	public Result(Integer state, String message, Object data) {
		this.state = state;
		this.message = message;
		this.data = data;
	}

	public static Result fail(Object data) {
		return new Result(Tips.FAIL.code, Tips.FAIL.msg, data);
	}

	public Result(Object data) {
		this.state = 200;
		this.message = "OK";
		this.data = data;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	/**
	 * 将json结果集转化为esult对象
	 *
	 * @param jsonData json数据
	 * @param clazz    FrontResult中的object类型
	 */
	public static Result formatToPojo(String jsonData, Class<?> clazz) {
		try {
			if (clazz == null) {
				return MAPPER.readValue(jsonData, Result.class);
			}
			JsonNode jsonNode = MAPPER.readTree(jsonData);
			JsonNode data = jsonNode.get("data");
			Object obj = null;
			if (data.isObject()) {
				obj = MAPPER.readValue(data.traverse(), clazz);
			} else if (data.isTextual()) {
				obj = MAPPER.readValue(data.asText(), clazz);
			}
			return build(jsonNode.get("state").intValue(), jsonNode.get("message").asText(), obj);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 没有object对象的转化
	 */
	public static Result format(String json) {
		try {
			return MAPPER.readValue(json, Result.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Object是集合转化
	 *
	 * @param jsonData json数据
	 * @param clazz    集合中的类型
	 */
	public static Result formatToList(String jsonData, Class<?> clazz) {
		try {
			JsonNode jsonNode = MAPPER.readTree(jsonData);
			JsonNode data = jsonNode.get("data");
			Object obj = null;
			if (data.isArray() && data.size() > 0) {
				obj = MAPPER.readValue(data.traverse(),
						MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
			}
			return build(jsonNode.get("state").intValue(), jsonNode.get("message").asText(), obj);
		} catch (Exception e) {
			return null;
		}
	}

}
