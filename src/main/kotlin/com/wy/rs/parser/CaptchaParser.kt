package com.wy.rs.parser

/**
 * 验证码识别接口
 */
interface CaptchaParser {
    /**
     * 识别URL中的验证码
     */
    fun identifyFromUrl(url: String): String
}