package com.aiconhub.fit.util

object Stats {
    fun streak(days: Set<Long>, todayEpochDay: Long): Int {
        var count = 0
        var d = todayEpochDay
        if (!days.contains(d)) {
            d -= 1
            if (!days.contains(d)) return 0
        }
        while (days.contains(d)) {
            count++
            d -= 1
        }
        return count
    }
}
