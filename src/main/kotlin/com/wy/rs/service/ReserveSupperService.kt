package com.wy.rs.service

import com.wy.rs.reserve.ReserveSupper
import com.wy.rs.reserve.impl.OkHttpReserveSupper
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ReserveSupperService {

    companion object {
        val logger = LoggerFactory.getLogger(ReserveSupperService::class.simpleName)
    }

    fun start(sleepDuration: Long) {
        logger.info("ReserveSupperService start..., sleepDuration $sleepDuration")
        val reserveSuper: ReserveSupper = OkHttpReserveSupper()
        try {
            reserveSuper.reserve();
        } catch (e: Exception) {
        }
    }
}