package com.aiconhub.fit.data

import android.content.Context
import org.json.JSONObject

data class Exercise(
    val id: String,
    val name: String,
    val level: String,
    val equipment: String,
    val primaryMuscles: List<String>,
    val secondaryMuscles: List<String>,
    val instructions: List<String>,
    val category: String,
    val anim: String,
    val video: String? = null,
    val videoCredit: String? = null
) {
    val group: String
        get() = when {
            primaryMuscles.any { it in listOf("chest") } -> "Chest"
            primaryMuscles.any { it in listOf("lats", "middle back", "lower back", "traps") } -> "Back"
            primaryMuscles.any { it in listOf("shoulders") } -> "Shoulders"
            primaryMuscles.any { it in listOf("biceps", "triceps", "forearms") } -> "Arms"
            primaryMuscles.any { it in listOf("quadriceps", "hamstrings", "calves") } -> "Legs"
            primaryMuscles.any { it in listOf("abdominals") } -> "Core"
            primaryMuscles.any { it in listOf("glutes") } -> "Glutes"
            else -> "Other"
        }
}

object ExerciseRepo {
    @Volatile private var cached: List<Exercise>? = null
    @Volatile private var byId: Map<String, Exercise>? = null

    fun load(context: Context): List<Exercise> {
        cached?.let { return it }
        val text = context.assets.open("exercises.json").bufferedReader().use { it.readText() }
        val arr = org.json.JSONArray(text)
        val list = ArrayList<Exercise>(arr.length())
        for (i in 0 until arr.length()) {
            val o: JSONObject = arr.getJSONObject(i)
            fun strList(key: String): List<String> {
                val a = o.optJSONArray(key) ?: return emptyList()
                return (0 until a.length()).map { a.getString(it) }
            }
            list.add(
                Exercise(
                    id = o.getString("id"),
                    name = o.getString("name"),
                    level = o.optString("level", "beginner"),
                    equipment = o.optString("equipment", "body only"),
                    primaryMuscles = strList("primaryMuscles"),
                    secondaryMuscles = strList("secondaryMuscles"),
                    instructions = strList("instructions"),
                    category = o.optString("category", "strength"),
                    anim = o.getString("anim"),
                    video = o.optString("video").ifEmpty { null },
                    videoCredit = o.optString("videoCredit").ifEmpty { null }
                )
            )
        }
        cached = list
        byId = list.associateBy { it.id }
        return list
    }

    fun byId(context: Context, id: String): Exercise? {
        if (byId == null) load(context)
        return byId?.get(id)
    }
}
