package com.wy.rs.utils

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import java.util.*
import kotlin.collections.HashMap

class OkHttpHelper {

    companion object {

        val cookieStore = HashMap<String, List<Cookie>>()

        val okHttpClient: OkHttpClient by lazy {
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
            builder.build()
        }
    }
}