package com.github.surpassm.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author mc
 * Create date 2019/4/15 10:22
 * Version 1.0
 * Description 自定义注解返回数据
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SerializedField {
	/**
	 * 需要返回的字段
	 */
	String[] includes() default {};
	/**
	 * 需要去除的字段
	 */
	String[] excludes() default {};
	/**
	 * 数据是否需要加密
	 */
	boolean encode() default false;
}
