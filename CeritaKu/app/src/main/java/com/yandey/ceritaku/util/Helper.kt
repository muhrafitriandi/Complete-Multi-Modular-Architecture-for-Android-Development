package com.yandey.ceritaku.util

import java.time.LocalTime

fun calculateTimeUntilNextMinute(): Long {
    val currentTime = LocalTime.now()
    val timeUntilNextMinute = 60 - currentTime.second
    return timeUntilNextMinute * 1000L
}