package com.aiconhub.fit.data

/**
 * Turns the questionnaire answers into a recommended plan and a set of
 * exercise picks. All logic is local and deterministic: level filters what is
 * shown, limitations exclude moves, goal ranks the rest.
 */
object Curator {
    private val jumpTerms = listOf("Jump", "Bound", "Skipping", "Sprint", "Plyo")
    private val kneeTerms = listOf("Lunge", "Squat", "Jump", "Step-up", "Bound", "Butt_Kick", "Split", "Knee_Tuck")
    private val wristTerms = listOf("Push", "Plank", "Inchworm", "Crawl", "Handstand", "Dips", "Bridge", "Body-Up")

    private fun matches(ex: Exercise, terms: List<String>) = terms.any { ex.id.contains(it, ignoreCase = true) }

    fun allowed(ex: Exercise, profile: Profile): Boolean {
        val lim = profile.limitations.split(",").map { it.trim() }.toSet()
        if ("no_jumping" in lim && matches(ex, jumpTerms)) return false
        if ("easy_knees" in lim && matches(ex, kneeTerms)) return false
        if ("easy_wrists" in lim && matches(ex, wristTerms)) return false
        return true
    }

    private fun levelRank(level: String) = when (level.lowercase()) {
        "beginner" -> 0
        "intermediate" -> 1
        else -> 2
    }

    fun levelOk(ex: Exercise, profile: Profile) = levelRank(ex.level) <= levelRank(profile.level)

    private fun score(ex: Exercise, profile: Profile): Int {
        var s = 0
        when (profile.goal) {
            "Build strength" -> {
                if (ex.category == "strength") s += 2
                if (ex.primaryMuscles.any { it in listOf("chest", "lats", "middle back", "quadriceps", "shoulders") }) s += 1
            }
            "Lose weight" -> {
                if (matches(ex, jumpTerms) || ex.category == "plyometrics" || ex.category == "cardio") s += 3
                if (ex.primaryMuscles.contains("abdominals")) s += 1
            }
            "Core & flexibility" -> {
                if (ex.primaryMuscles.contains("abdominals")) s += 3
                if (ex.category == "stretching") s += 2
            }
            else -> {
                if (ex.category == "strength") s += 1
                if (ex.primaryMuscles.any { it in listOf("chest", "quadriceps", "abdominals") }) s += 1
            }
        }
        if (ex.level == "beginner") s += 1
        return s
    }

    fun picks(exercises: List<Exercise>, profile: Profile, count: Int = 8): List<Exercise> =
        exercises
            .filter { allowed(it, profile) && levelOk(it, profile) }
            .sortedWith(compareByDescending<Exercise> { score(it, profile) }.thenBy { it.name })
            .take(count)

    fun recommendedPlan(profile: Profile): Plan? = PlanRepo.byId(
        when (profile.goal) {
            "Core & flexibility" -> "core_foundation"
            "Lose weight" -> "sweat_express"
            "Build strength" -> if (levelRank(profile.level) == 0) "starter_full_body" else "bodyweight_plus"
            else -> if (levelRank(profile.level) == 0) "starter_full_body" else "sweat_express"
        }
    )
}
