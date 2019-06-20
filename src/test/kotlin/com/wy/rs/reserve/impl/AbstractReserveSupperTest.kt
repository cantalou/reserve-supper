package com.wy.rs.reserve.impl

import com.wy.rs.reserve.ReserveSupperFactory
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class AbstractReserveSupperTest {

    @Test
    fun isWorkDay() {
        val rs = ReserveSupperFactory.INSTANCE.getObject() as AbstractReserveSupper
        assert(rs.isWorkDay("20190620"))
        assert(!rs.isWorkDay("20191001"))
        assert(!rs.isWorkDay("20190622"))
    }
}