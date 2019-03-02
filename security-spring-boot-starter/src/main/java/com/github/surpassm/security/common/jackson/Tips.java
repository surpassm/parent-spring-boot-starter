package com.github.surpassm.security.common.jackson;

/**
 * @author mc
 * date 2019/1/4 10:12
 * version 1.0
 * description
 */
public enum Tips {
	/**
	 *
	 */
    FAIL(400,"失败"),
    SUCCESS(200,"成功"),

    DISABLED_TOEK(401,"登陆过期,请重新登陆"),
    USER_EMAIL_HAD("该邮箱已注册"),
    USER_PASSWORD_FALSE("用户名或密码错误"),
    USER_PASSWORD_F("旧密码错误"),
    USER_PASSWORD_ERROE("密码错误"),

    MSG_NOT("信息不存在"),
    MSG_YES("信息已存在"),
    CODE_FALSE("验证码错误"),
    PARAMETER_ERROR("参数有误"),



    ;
    public Integer code;
    public String msg;


    Tips(String msg) {
        /**
         * 消息
         */
        this.msg = msg;
    }

    Tips(Integer code, String msg) {
        /**
         * 状态码
         */
        this.code = code;
        /**
         * 消息
         */
        this.msg = msg;
    }
}
