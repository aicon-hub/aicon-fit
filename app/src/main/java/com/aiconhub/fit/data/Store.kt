package com.aiconhub.fit.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

data class Profile(
    var name: String = "",
    var heightCm: Double = 170.0,
    var weightKg: Double = 70.0,
    var units: String = "metric",
    var goal: String = "Get fit"
)

data class AppSettings(
    var reduceMotion: Boolean = false,
    var highContrast: Boolean = false,
    var haptics: Boolean = true,
    var sound: Boolean = true,
    var onboardingDone: Boolean = false
)

data class HistoryEntry(
    val name: String,
    val epochDay: Long,
    val minutes: Int,
    val exerciseCount: Int
)

class Store(private val context: Context) {
    private val file: File = File(context.filesDir, "aicon_fit_store.json")

    var profile = Profile()
    var settings = AppSettings()
    var history: MutableList<HistoryEntry> = mutableListOf()
    var planDone: MutableSet<String> = mutableSetOf()
    var activePlanId: String? = null
    var customWorkouts: MutableList<Workout> = mutableListOf()

    init {
        load()
    }

    private fun load() {
        if (!file.exists()) return
        try {
            val o = JSONObject(file.readText())
            val p = o.optJSONObject("profile")
            if (p != null) profile = Profile(
                name = p.optString("name", ""),
                heightCm = p.optDouble("heightCm", 170.0),
                weightKg = p.optDouble("weightKg", 70.0),
                units = p.optString("units", "metric"),
                goal = p.optString("goal", "Get fit")
            )
            val s = o.optJSONObject("settings")
            if (s != null) settings = AppSettings(
                reduceMotion = s.optBoolean("reduceMotion", false),
                highContrast = s.optBoolean("highContrast", false),
                haptics = s.optBoolean("haptics", true),
                sound = s.optBoolean("sound", true),
                onboardingDone = s.optBoolean("onboardingDone", false)
            )
            val h = o.optJSONArray("history")
            if (h != null) {
                for (i in 0 until h.length()) {
                    val e = h.getJSONObject(i)
                    history.add(HistoryEntry(e.getString("name"), e.getLong("epochDay"), e.getInt("minutes"), e.getInt("exerciseCount")))
                }
            }
            val pd = o.optJSONArray("planDone")
            if (pd != null) for (i in 0 until pd.length()) planDone.add(pd.getString(i))
            activePlanId = if (o.has("activePlanId")) o.getString("activePlanId") else null
            val cw = o.optJSONArray("customWorkouts")
            if (cw != null) {
                for (i in 0 until cw.length()) {
                    val w = cw.getJSONObject(i)
                    val itemsArr = w.getJSONArray("items")
                    val items = ArrayList<WorkoutItem>(itemsArr.length())
                    for (j in 0 until itemsArr.length()) {
                        val it = itemsArr.getJSONObject(j)
                        items.add(WorkoutItem(
                            exerciseId = it.getString("exerciseId"),
                            sets = it.optInt("sets", 3),
                            reps = it.optInt("reps", 0),
                            seconds = it.optInt("seconds", 0),
                            restSeconds = it.optInt("restSeconds", 45)
                        ))
                    }
                    customWorkouts.add(Workout(w.getString("id"), w.getString("name"), items, custom = true))
                }
            }
        } catch (e: Exception) {
            // Corrupt store: start fresh rather than crash.
        }
    }

    @Synchronized
    fun save() {
        try {
            val o = JSONObject()
            o.put("profile", JSONObject()
                .put("name", profile.name)
                .put("heightCm", profile.heightCm)
                .put("weightKg", profile.weightKg)
                .put("units", profile.units)
                .put("goal", profile.goal))
            o.put("settings", JSONObject()
                .put("reduceMotion", settings.reduceMotion)
                .put("highContrast", settings.highContrast)
                .put("haptics", settings.haptics)
                .put("sound", settings.sound)
                .put("onboardingDone", settings.onboardingDone))
            val h = JSONArray()
            history.forEach { e ->
                h.put(JSONObject()
                    .put("name", e.name)
                    .put("epochDay", e.epochDay)
                    .put("minutes", e.minutes)
                    .put("exerciseCount", e.exerciseCount))
            }
            o.put("history", h)
            val pd = JSONArray()
            planDone.forEach { pd.put(it) }
            o.put("planDone", pd)
            if (activePlanId != null) o.put("activePlanId", activePlanId)
            val cw = JSONArray()
            customWorkouts.forEach { w ->
                val items = JSONArray()
                w.items.forEach { it ->
                    items.put(JSONObject()
                        .put("exerciseId", it.exerciseId)
                        .put("sets", it.sets)
                        .put("reps", it.reps)
                        .put("seconds", it.seconds)
                        .put("restSeconds", it.restSeconds))
                }
                cw.put(JSONObject().put("id", w.id).put("name", w.name).put("items", items))
            }
            o.put("customWorkouts", cw)
            file.writeText(o.toString())
        } catch (e: Exception) {
            // Best effort save; never crash the app over persistence.
        }
    }

    fun addHistory(entry: HistoryEntry) {
        history.add(0, entry)
        save()
    }

    fun clearAll() {
        profile = Profile()
        settings = AppSettings()
        history = mutableListOf()
        planDone = mutableSetOf()
        activePlanId = null
        customWorkouts = mutableListOf()
        if (file.exists()) file.delete()
        save()
    }
}
