package com.wy.rs.parser

/**
 * Http内容解析接口
 */
interface HttpContentParser {
    /**
     *
     */
    fun loadContent(url: String): String
}