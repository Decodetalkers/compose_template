package com.stein.mahoyinkuima.nhk

import kotlinx.serialization.*

@Serializable
data class NhkNews(
        val newsId: String,
        val title: String,
        val titleWithRuby: String,
        val outline: String,
        val outlineWithRuby: String,
        val body: String,
        val bodyWithoutHtml: String,
        val url: String,
        val m3u8Url: String,
        val imageUrl: String,
        val publishedAtUtc: String,
)
