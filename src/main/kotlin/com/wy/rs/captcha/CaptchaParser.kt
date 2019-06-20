package com.wy.rs.captcha

/**
 * 验证码识别接口
 */
interface CaptchaParser {
    /**
     * 识别URL中的验证码
     */
    fun identifyFromUrl(url: String): String

    /**
     * 识别Base64编码中的验证码
     */
    fun identifyFromBase64(base64: String): String
}