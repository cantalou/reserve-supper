package com.wy.rs.captcha.parser

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.wy.rs.captcha.CaptchaParser
import com.wy.rs.utils.OkHttpHelper.Companion.okHttpClient
import okhttp3.FormBody
import okhttp3.Request
import java.lang.RuntimeException
import java.util.*

class CJYCaptchaParser : CaptchaParser {
    override fun identifyFromUrl(url: String): String {
        val request = Request.Builder()
            .url(url)
            .build()
        val resp = okHttpClient.newCall(request)
            .execute()
        if (!resp.isSuccessful) {
            throw RuntimeException("验证码获取失败, " + resp.body()?.string())
        }
        val encoder = Base64.getEncoder()
        return identifyFromBase64(encoder.encodeToString(resp.body()!!.bytes()));
    }

    override fun identifyFromBase64(base64: String): String {
        val body = FormBody.Builder()
            .add("user", "cantalou")
            .add("pass", "wy6115213748")
            .add("softid", "900082")
            .add("codetype", "1902")
            .add("len_min", "0")
            .add("file_base64", base64)
            .build()
        val request = Request.Builder()
            .url("http://upload.chaojiying.net/Upload/Processing.php")
            .post(body)
            .build()

        val resp = okHttpClient.newCall(request)
            .execute()

        val respContent = resp.body()!!.string()
        if (!resp.isSuccessful) {
            throw RuntimeException("验证码接口返回非200, " + respContent)
        }

        val mapper = ObjectMapper().registerKotlinModule()
        val json: CaptchaResponse = mapper.readValue(respContent)
        if (json.err_no != 0) {
            throw RuntimeException("验证码接口返回错误, " + json.err_str)
        }

        return json.pic_str
    }

    class CaptchaResponse(val err_no: Int, val err_str: String, val pic_str: String, val pic_id: String, val md5: String) {}
}