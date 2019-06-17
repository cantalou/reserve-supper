package com.wy.rs.utils

class OkHttpHelper private constructor() {

    private object Holder {
        val INSTANCE = OkHttpHelper()
    }

    companion object {
        val instance: OkHttpHelper by lazy { Holder.INSTANCE }
    }
}