package com.wy.rs.loader

interface HttpContentLoader {
    fun loadContent(url: String): String
}