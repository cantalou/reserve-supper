package com.wy.rs.captcha

import com.wy.rs.captcha.parser.CJYCaptchaParser

/**
 * 验证码识别器工厂
 */
class CaptchaFactory private constructor() {

    private object Holder {
        val holder: CaptchaFactory = CaptchaFactory()
    }

    companion object {
        val INSTANCES: CaptchaFactory by lazy { Holder.holder }
    }

    fun createParser(): CaptchaParser {
        return CJYCaptchaParser()
    }
    
}