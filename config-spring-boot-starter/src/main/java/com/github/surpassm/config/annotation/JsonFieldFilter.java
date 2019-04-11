package com.github.surpassm.config.annotation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author mc
 * Create date 2019/4/10 15:48
 * Version 1.0
 * Description 自定义JSON返回过滤注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@JsonIgnoreType
public @interface JsonFieldFilter {
	/**
	 * 对哪个类的属性进行过滤
	 */
	Class<?> type();
	/**
	 * 包含哪些字段，即哪些字段可以显示
	 */
	String include() default  "";
	/**
	 *不包含哪些字段，即哪些字段不可以显示
	 */
	String exclude() default "";
}
