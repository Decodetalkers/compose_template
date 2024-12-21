package com.stein.mahoyinkuima.nhk

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import okhttp3.*

val isoFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

fun requestTodayNews(): Request {
    val now = LocalDateTime.now(ZoneOffset.UTC)

    // Calculate yesterday and 7 days ago
    val yesterday = now.minusDays(1)
    val sevenDaysAgo = now.minusDays(7)
    val yesterdayIso = yesterday.atOffset(ZoneOffset.UTC).format(isoFormatter)
    val sevenDaysAgoIso = sevenDaysAgo.atOffset(ZoneOffset.UTC).format(isoFormatter)
    val url =
            HttpUrl.Builder()
                    .scheme("https") // Scheme (https)
                    .host("nhk.dekiru.app") // Host
                    .addPathSegment("news") // Path segment
                    .addQueryParameter("startDate", sevenDaysAgoIso) // Query parameter
                    .addQueryParameter("endDate", yesterdayIso) // Query parameter
                    .build()

    return Request.Builder().url(url).build()
}
