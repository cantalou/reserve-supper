package com.wy.rs.reserve.impl

import com.wy.rs.reserve.ReserveSupper
import com.wy.rs.service.ReserveSupperService
import com.wy.rs.utils.OkHttpHelper
import okhttp3.Request
import org.slf4j.LoggerFactory
import java.text.SimpleDateFormat
import java.util.*

abstract class AbstractReserveSupper : ReserveSupper {

    companion object {
        val logger = LoggerFactory.getLogger(AbstractReserveSupper::class.simpleName)
    }

    override fun reserve() {
        if (!isWorkDay(SimpleDateFormat("yyyyMMdd").format(Date()))) {
            logger.error("今天非工作日不进行订餐操作")
            return;
        }
        logger.error("今天正常工作日, 开始进行订餐操作")
        login("linzhiwei1@4399inc.com", "xm27377")
        reserveImpl()
    }

    abstract fun reserveImpl()

    abstract fun login(account: String, password: String): Boolean

    /**
     * 判断是否工作日
     * [dateStr] yyyyMMdd
     */
    fun isWorkDay(dateStr: String): Boolean {
        var url = "http://api.goseek.cn/Tools/holiday?date=$dateStr"
        var resp = OkHttpHelper.okHttpClient.newCall(Request.Builder().url(url).build())
            .execute()
        if (!resp.isSuccessful) {
            logger.error("请求节假日接口失败")
            return true
        }

        var queryResult = resp.body()
            ?.string()
        if (queryResult == null) {
            logger.error("请求工作日接口失败")
            return true
        }
        logger.debug("api.goseek.cn 工作日请求结果: $url  $queryResult")
        var result = queryResult.contains("\"data\":0")

        url = "http://tool.bitefu.net/jiari/?d=$dateStr"
        resp = OkHttpHelper.okHttpClient.newCall(Request.Builder().url(url).build())
            .execute()
        if (!resp.isSuccessful) {
            logger.error("请求工作日接口失败")
            return true
        }

        queryResult = resp.body()
            ?.string()
        if (queryResult == null) {
            logger.error("请求工作日接口失败")
            return true
        }
        logger.debug("tool.bitefu.net 工作日请求结果:  $url  $queryResult");
        return result && queryResult.contains("0")
    }
}