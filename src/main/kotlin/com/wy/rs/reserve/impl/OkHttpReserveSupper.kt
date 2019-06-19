package com.wy.rs.reserve.impl

import com.wy.rs.reserve.ReserveSupper
import okhttp3.*
import java.net.http.HttpRequest
import java.util.*
import kotlin.collections.HashMap
import javax.script.Invocable
import javax.script.ScriptEngineManager
import javax.script.ScriptEngine


class OkHttpReserveSupper : ReserveSupper {

    companion object {
        val LOGIN_URL = "http://manager.4399tech.com/v2/login-jump.html"
        val ADD_PAGE_URL = "http://manager.4399tech.com/v2/www/diancan/index-add.html?action=addView"
        val SAVE_URL = "http://manager.4399tech.com/v2/www/diancan/index-add.html?action=save"
    }

    private val okHttpClient: OkHttpClient

    val cookieStore = HashMap<String, List<Cookie>>()

    init {
        val builder = OkHttpClient.Builder()
        builder.cookieJar(object : CookieJar {
            override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                cookieStore[url.host()] = cookies
                for (cookie in cookies) {
                    println("写入cookie: $cookie")
                }
            }

            override fun loadForRequest(url: HttpUrl): List<Cookie> {

                return cookieStore[url.host()] ?: Collections.emptyList()
            }
        })
        okHttpClient = builder.build()
    }


    /**
     * 执行订餐操作:
     * 1. 获取订餐页面的内容, 解析token
     * 2. 请求订餐接口
     */
    override fun reserve() {

        val request = Request.Builder().url(ADD_PAGE_URL).build()

        val resp = okHttpClient.newCall(request).execute()
        if (!resp.isSuccessful) {
            println("接口请求返回非200")
        }

        val content = resp.body()!!.string()

        val token = parseToken(content)

        //
        //
        //comment=234&captcha=234&keyString=78e7b16dd6ba97e266ae74561ab95c3a&token=201906182d8b98bb5643ca5a82f6772a1c8f26fe
    }

    /**
     * 解析验证码的KeyString参数
     */
    private fun parseKeyString(content: String): String {
        return ""
    }

    /**
     * 解析Url地址
     */
    private fun parseCaptchaUrl(content: String): String {
        return ""
    }

    /**
     * 解析Token参数
     */
    private fun parseToken(content: String): String {
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
        val paramBody = FormBody.Builder().add("login_info[email]", account).add("login_info[password]", password).build()
        val request = Request.Builder().url(LOGIN_URL).post(paramBody).build()
        val resp = okHttpClient.newCall(request).execute()
        if (!resp.isSuccessful) {
            println("接口请求返回非200")
            return false
        }
        return true
    }
}