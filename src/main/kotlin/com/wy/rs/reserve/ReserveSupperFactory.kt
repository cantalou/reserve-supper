package com.wy.rs.reserve

import com.wy.rs.reserve.impl.OkHttpReserveSupper

class ReserveSupperFactory {

    companion object {
        val INSTANCE: ReserveSupperFactory by lazy { ReserveSupperFactory() }
    }

    fun getObject(): ReserveSupper {
        return OkHttpReserveSupper()
    }

}