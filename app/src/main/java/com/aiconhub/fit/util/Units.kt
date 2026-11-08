package com.aiconhub.fit.util

import kotlin.math.roundToInt

object Units {
    fun kgToLb(kg: Double): Double = kg * 2.20462

    fun formatWeight(kg: Double, units: String): String =
        if (units == "imperial") "${kgToLb(kg).roundToInt()} lb" else "${kg.roundToInt()} kg"

    fun formatHeight(cm: Double, units: String): String {
        if (units != "imperial") return "${cm.roundToInt()} cm"
        val totalIn = (cm / 2.54).roundToInt()
        val ft = totalIn / 12
        val inch = totalIn % 12
        return "$ft ft $inch in"
    }

    fun parseWeight(text: String, units: String): Double? {
        val v = text.trim().toDoubleOrNull() ?: return null
        if (v <= 0 || v > 500) return null
        return if (units == "imperial") v / 2.20462 else v
    }

    fun parseHeight(text: String, units: String): Double? {
        val t = text.trim()
        if (units == "imperial") {
            val m = Regex("""^(\d+)\s*(?:ft|')\s*(\d+)?""").find(t)
            if (m != null) {
                val ft = m.groupValues[1].toIntOrNull() ?: return null
                val inch = m.groupValues.getOrNull(2)?.toIntOrNull() ?: 0
                val cm = (ft * 12 + inch) * 2.54
                return if (cm in 60.0..250.0) cm else null
            }
            val v = t.toDoubleOrNull() ?: return null
            val cm = v * 2.54
            return if (cm in 60.0..250.0) cm else null
        }
        val v = t.toDoubleOrNull() ?: return null
        return if (v in 60.0..250.0) v else null
    }
}
