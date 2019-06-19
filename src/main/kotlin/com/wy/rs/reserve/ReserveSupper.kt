package com.wy.rs.reserve

/**
 * Http内容解析接口
 */
interface ReserveSupper {

    /**
     * 执行订餐操作
     */
    fun reserve()

    /**
     * 登录并保存Cookie
     */
    fun login(account: String, password: String): Boolean
}