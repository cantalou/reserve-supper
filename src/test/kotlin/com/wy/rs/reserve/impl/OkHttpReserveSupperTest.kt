package com.wy.rs.reserve.impl

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

/**
 * comment=234&captcha=234&keyString=78e7b16dd6ba97e266ae74561ab95c3a&token=201906182d8b98bb5643ca5a82f6772a1c8f26fe
 */
internal class OkHttpReserveSupperTest {

    val okHttpReserveSupper = OkHttpReserveSupper()

    companion object {

        lateinit var indexAddPageContent: String

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            indexAddPageContent = OkHttpReserveSupperTest::class.java.classLoader.getResource("index-add.html").readText()
        }
    }

    @Test
    fun parseKeyString() {
        assertEquals("78e7b16dd6ba97e266ae74561ab95c3a", okHttpReserveSupper.parseKeyString(indexAddPageContent))
    }

    @Test
    fun parseCaptchaUrl() {
        assertEquals("http://manager.4399tech.com/v2/login-identifyingCode.html?key=78e7b16dd6ba97e266ae74561ab95c3a",
                     okHttpReserveSupper.parseCaptchaUrl(indexAddPageContent))
    }

    @Test
    fun parseToken() {
        assertEquals("201906182d8b98bb5643ca5a82f6772a1c8f26fe", okHttpReserveSupper.parseToken(indexAddPageContent))
    }
}