package com.aiconhub.fit

import com.aiconhub.fit.util.Stats
import com.aiconhub.fit.util.Units
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class UnitsTest {
    @Test
    fun weightFormats() {
        assertEquals("70 kg", Units.formatWeight(70.0, "metric"))
        assertEquals("154 lb", Units.formatWeight(70.0, "imperial"))
    }

    @Test
    fun heightFormats() {
        assertEquals("170 cm", Units.formatHeight(170.0, "metric"))
        assertEquals("5 ft 7 in", Units.formatHeight(170.0, "imperial"))
    }

    @Test
    fun parseWeightRejectsJunk() {
        assertNull(Units.parseWeight("abc", "metric"))
        assertNull(Units.parseWeight("0", "metric"))
        assertNull(Units.parseWeight("900", "metric"))
    }

    @Test
    fun parseHeightImperial() {
        val cm = Units.parseHeight("5 ft 7", "imperial")!!
        assertEquals(170.0, cm, 2.0)
    }

    @Test
    fun streakCountsBackFromToday() {
        val days = setOf(100L, 99L, 98L, 90L)
        assertEquals(3, Stats.streak(days, 100L))
        assertEquals(3, Stats.streak(days, 101L))
        assertEquals(0, Stats.streak(days, 102L))
    }
}
