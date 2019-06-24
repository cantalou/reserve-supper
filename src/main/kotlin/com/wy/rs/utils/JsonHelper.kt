package com.wy.rs.utils
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

import com.fasterxml.jackson.databind.ObjectMapper

/**
 * 使用FastJson作为json数据继续框架
 */
class JsonHelper {

    companion object{
        val mapper = ObjectMapper().registerKotlinModule()
    }
}