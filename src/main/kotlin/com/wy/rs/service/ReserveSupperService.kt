package com.wy.rs.service

import com.wy.rs.reserve.ReserveSupper
import com.wy.rs.reserve.ReserveSupperFactory
import com.wy.rs.reserve.impl.OkHttpReserveSupper
import org.slf4j.LoggerFactory
import javax.swing.JOptionPane


class ReserveSupperService {

    companion object {
        val logger = LoggerFactory.getLogger(ReserveSupperService::class.simpleName)
    }

    fun start(sleepDuration: Long) {
        logger.info("ReserveSupperService start..., sleepDuration $sleepDuration")
        val reserveSuper: ReserveSupper = ReserveSupperFactory.INSTANCE.getObject()
        try {
            reserveSuper.reserve();
        } catch (e: Exception) {
            logger.error("订餐失败", e)
            JOptionPane.showMessageDialog(null, e.message)
        }
    }
}