package com.wy.rs.reserve.impl

import com.wy.rs.captcha.CaptchaFactory
import com.wy.rs.utils.OkHttpHelper.Companion.okHttpClient
import okhttp3.*
import org.slf4j.LoggerFactory
import javax.script.Invocable
import javax.script.ScriptEngineManager


class OkHttpReserveSupper : AbstractReserveSupper() {

    companion object {
        val logger = LoggerFactory.getLogger(AbstractReserveSupper::class.simpleName)
        const val SERVER_HOST = "http://manager.4399tech.com"
        const val LOGIN_URL = "$SERVER_HOST/v2/login-jump.html"
        const val ADD_PAGE_URL = "$SERVER_HOST/v2/www/diancan/index-add.html?action=addView"
        const val SAVE_URL = "$SERVER_HOST/v2/www/diancan/index-add.html?action=save"
    }

    /**
     * 执行订餐操作:
     * 1. 获取订餐页面的内容, 解析token
     * 2. 请求订餐接口
     */
    override fun reserveImpl() {
        val content = getAddPageContent()
        val token = parseToken(content)
        val keyString = parseKeyString(content)

        val captchaUrl = parseCaptchaUrl(content)
        val captcha = CaptchaFactory.INSTANCES.createParser()
            .identifyFromUrl(captchaUrl)

        save("123", captcha, keyString, token)
    }


    /**
     * 发起订单请求
     * comment=234&captcha=234&keyString=78e7b16dd6ba97e266ae74561ab95c3a&token=201906182d8b98bb5643ca5a82f6772a1c8f26fe
     */
    private fun save(comment: String, captcha: String, keyString: String, token: String) {
        val body = FormBody.Builder()
            .add("comment", comment)
            .add("captcha", captcha)
            .add("keyString", keyString)
            .add("token", token)
            .build()
        val request = Request.Builder()
            .url(SAVE_URL)
            .post(body)
            .build()
        val resp = okHttpClient.newCall(request)
            .execute()
        println(resp.body()?.string())

    }

    /**
     * 获取订餐页面的HTML内容
     */
    private fun getAddPageContent(): String {
        val request = Request.Builder()
            .url(ADD_PAGE_URL)
            .build()

        val resp = okHttpClient.newCall(request)
            .execute()
        if (!resp.isSuccessful) {
            logger.error("接口请求返回非200, " + resp.body()?.string())
            return ""
        }
        return resp.body()!!.string()
    }

    /**
     * 解析验证码的KeyString参数
     * <img id="code_img" src="/v2/login-identifyingCode.html?key=78e7b16dd6ba97e266ae74561ab95c3a" class="yzm" title="看不清楚,换一张" alt="看不清楚,换一张"/></a>
     */
    fun parseKeyString(content: String): String {
        val captchaStartTag = "/v2/login-identifyingCode.html?key="
        val keyStart = content.indexOf(captchaStartTag) + captchaStartTag.length
        val keyEnd = content.indexOf("\"", keyStart)
        return content.substring(keyStart, keyEnd)
    }

    /**
     * 解析Url地址
     * <img id="code_img" src="/v2/login-identifyingCode.html?key=78e7b16dd6ba97e266ae74561ab95c3a" class="yzm" title="看不清楚,换一张" alt="看不清楚,换一张"/></a>
     */
    fun parseCaptchaUrl(content: String): String {
        val imgStartTag = "<img id=\"code_img\" src=\""
        val urlStart = content.indexOf(imgStartTag) + imgStartTag.length
        val urlEnd = content.indexOf("\"", urlStart)
        return SERVER_HOST + content.substring(urlStart, urlEnd)
    }

    /**
     * 解析Token参数
     */
    fun parseToken(content: String): String {
        val jsStartTag = "UIModals.form($(\"#ajax-modal\"), {'callback': callback});"
        val jsEndTag = "document.getElementById('token').value="
        val jsStart = content.indexOf(jsStartTag) + jsStartTag.length
        val jsEnd = content.indexOf(jsEndTag, jsStart)
        val jsContent = content.substring(jsStart, jsEnd)

        val jsResultStart = jsEnd + jsEndTag.length
        val jsResultEnd = content.indexOf("();", jsResultStart)
        val jsTokenMethod = content.substring(jsResultStart, jsResultEnd)

        val nashorn = ScriptEngineManager().getEngineByName("nashorn")
        nashorn.eval(jsContent)
        val inv = nashorn as Invocable
        return inv.invokeFunction(jsTokenMethod) as String
    }

    override fun login(account: String, password: String): Boolean {
        val paramBody = FormBody.Builder()
            .add("login_info[email]", account)
            .add("login_info[password]", password)
            .build()
        val request = Request.Builder()
            .url(LOGIN_URL)
            .post(paramBody)
            .build()
        val resp = okHttpClient.newCall(request)
            .execute()
        if (!resp.isSuccessful) {
            logger.error("接口请求返回非200," + resp.body()?.string())
            return false
        }
        resp.close()
        return true
    }
}