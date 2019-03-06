package com.github.surpassm.common.jackson;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;

/**
 * 前端JSON返回格式,自定义响应格式
 *
 * @author mc
 * version 1.0
 * date 2018/10/30 12:52
 * description
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "返回类")
public class Result{
	/**
	 * 响应业务状态
	 */
	@ApiModelProperty(value = "响应业务状态")
	private Integer code;

	/**
	 * 响应消息
	 */
	@ApiModelProperty(value = "响应消息")
	private String message;

	/**
	 * 响应中的数据
	 */
	@ApiModelProperty(value = "响应中的数据")
	private Object data;

	public Result(Object data) {
		this.code = Tips.SUCCESS.code;
		this.message = Tips.SUCCESS.msg;
		this.data = data;
	}

	public Result(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public Result(Integer code, String message, Object data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public static Result ok(Object data) {
		return new Result(data);
	}

	public static Result ok() {
		return new Result(new ArrayList<>());
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



	public static Result fail(Object data) {
		return new Result(Tips.FAIL.code, Tips.FAIL.msg, data);
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}


}
