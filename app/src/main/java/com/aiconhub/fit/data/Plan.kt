package com.aiconhub.fit.data

data class WorkoutItem(
    val exerciseId: String,
    val sets: Int = 3,
    val reps: Int = 0,
    val seconds: Int = 0,
    val restSeconds: Int = 45
) {
    val timed: Boolean get() = seconds > 0
    fun targetText(): String = if (timed) "$seconds sec" else "$reps reps"
}

data class Workout(
    val id: String,
    val name: String,
    val items: List<WorkoutItem>,
    val custom: Boolean = false
)

data class PlanDay(val title: String, val items: List<WorkoutItem>)

data class Plan(
    val id: String,
    val name: String,
    val description: String,
    val level: String,
    val weeks: Int,
    val daysPerWeek: Int,
    val equipment: String,
    val days: List<PlanDay>
)

object PlanRepo {
    private fun item(id: String, sets: Int = 3, reps: Int = 0, seconds: Int = 0, rest: Int = 45) =
        WorkoutItem(id, sets, reps, seconds, rest)

    private val corePlans: List<Plan> = listOf(
        Plan(
            id = "starter_full_body",
            name = "Starter Full Body",
            description = "Two weeks to learn the core moves. Three days a week, about 20 minutes a day. No equipment needed.",
            level = "Beginner",
            weeks = 2,
            daysPerWeek = 3,
            equipment = "None",
            days = listOf(
                PlanDay("Day 1: Full body A", listOf(
                    item("Bodyweight_Squat", 3, reps = 10),
                    item("Pushups", 3, reps = 8, rest = 60),
                    item("Butt_Lift_Bridge", 3, reps = 12),
                    item("Pelvic_Tilt", 3, reps = 12)
                )),
                PlanDay("Day 2: Full body B", listOf(
                    item("Standing_Calf_Raise", 3, reps = 12),
                    item("Crunches", 3, reps = 10),
                    item("Superman", 3, reps = 10),
                    item("Side_Bridge", 3, seconds = 20, rest = 60)
                )),
                PlanDay("Day 3: Full body C", listOf(
                    item("Bodyweight_Squat", 3, reps = 12),
                    item("Pushups", 3, reps = 10, rest = 60),
                    item("Leg_Lift", 3, reps = 10),
                    item("Star_Jump", 3, seconds = 30)
                )),
                PlanDay("Day 4: Full body A, round 2", listOf(
                    item("Bodyweight_Squat", 4, reps = 12),
                    item("Pushups", 4, reps = 8, rest = 60),
                    item("Butt_Lift_Bridge", 4, reps = 12),
                    item("Pelvic_Tilt", 4, reps = 12)
                )),
                PlanDay("Day 5: Full body B, round 2", listOf(
                    item("Standing_Calf_Raise", 4, reps = 12),
                    item("Crunches", 4, reps = 12),
                    item("Superman", 4, reps = 12),
                    item("Side_Bridge", 3, seconds = 30, rest = 60)
                )),
                PlanDay("Day 6: Full body C, round 2", listOf(
                    item("Bodyweight_Squat", 4, reps = 12),
                    item("Pushups", 4, reps = 10, rest = 60),
                    item("Leg_Lift", 4, reps = 12),
                    item("Star_Jump", 3, seconds = 40)
                ))
            )
        ),
        Plan(
            id = "core_foundation",
            name = "Core Foundation",
            description = "Two weeks focused on abs and lower back. Three days a week, about 15 minutes a day.",
            level = "Beginner",
            weeks = 2,
            daysPerWeek = 3,
            equipment = "None",
            days = listOf(
                PlanDay("Day 1: Core A", listOf(
                    item("Crunches", 3, reps = 15),
                    item("Bent-Knee_Hip_Raise", 3, reps = 12),
                    item("Pelvic_Tilt", 3, reps = 12)
                )),
                PlanDay("Day 2: Core B", listOf(
                    item("Cross-Body_Crunch", 3, reps = 16),
                    item("Side_Bridge", 3, seconds = 25, rest = 60),
                    item("Flutter_Kicks", 3, seconds = 30)
                )),
                PlanDay("Day 3: Core C", listOf(
                    item("Sit-Up", 3, reps = 12),
                    item("Cross-Body_Crunch", 3, reps = 16),
                    item("Leg_Lift", 3, reps = 10),
                    item("Lower_Back_Curl", 3, reps = 12)
                )),
                PlanDay("Day 4: Core A, round 2", listOf(
                    item("Crunches", 4, reps = 15),
                    item("Bent-Knee_Hip_Raise", 4, reps = 12),
                    item("Pelvic_Tilt", 3, reps = 15)
                )),
                PlanDay("Day 5: Core B, round 2", listOf(
                    item("Cross-Body_Crunch", 4, reps = 20),
                    item("Side_Bridge", 3, seconds = 35, rest = 60),
                    item("Flutter_Kicks", 4, seconds = 30)
                )),
                PlanDay("Day 6: Core C, round 2", listOf(
                    item("Sit-Up", 4, reps = 12),
                    item("Leg_Lift", 4, reps = 12),
                    item("Lower_Back_Curl", 4, reps = 12),
                    item("Flutter_Kicks", 4, seconds = 30)
                ))
            )
        ),
        Plan(
            id = "sweat_express",
            name = "Sweat Express",
            description = "Short timed sessions to get your heart rate up. Three days a week, 12 to 15 minutes. No equipment.",
            level = "Intermediate",
            weeks = 2,
            daysPerWeek = 3,
            equipment = "None",
            days = listOf(
                PlanDay("Day 1: Movers", listOf(
                    item("High_Knees", 3, seconds = 40, rest = 30),
                    item("Skier_Jacks", 3, seconds = 40, rest = 30),
                    item("Star_Jump", 3, seconds = 30, rest = 45),
                    item("Burpee", 2, seconds = 30, rest = 45)
                )),
                PlanDay("Day 2: Power", listOf(
                    item("Burpee", 3, seconds = 30, rest = 45),
                    item("Pushups", 3, reps = 10, rest = 45),
                    item("High_Knees", 3, seconds = 40, rest = 30),
                    item("Skier_Jacks", 3, seconds = 40, rest = 30)
                )),
                PlanDay("Day 3: Mix", listOf(
                    item("Star_Jump", 3, seconds = 40, rest = 30),
                    item("Bodyweight_Squat", 3, reps = 15),
                    item("High_Knees", 3, seconds = 40, rest = 30),
                    item("Sit-Up", 3, reps = 12, rest = 45)
                )),
                PlanDay("Day 4: Movers, round 2", listOf(
                    item("High_Knees", 4, seconds = 40, rest = 25),
                    item("Skier_Jacks", 4, seconds = 40, rest = 25),
                    item("Star_Jump", 4, seconds = 35, rest = 40),
                    item("Burpee", 3, seconds = 30, rest = 45)
                )),
                PlanDay("Day 5: Power, round 2", listOf(
                    item("Burpee", 4, seconds = 30, rest = 40),
                    item("Pushups", 4, reps = 10, rest = 40),
                    item("High_Knees", 4, seconds = 40, rest = 25),
                    item("Skier_Jacks", 4, seconds = 40, rest = 25)
                )),
                PlanDay("Day 6: Mix, round 2", listOf(
                    item("Star_Jump", 4, seconds = 40, rest = 25),
                    item("Bodyweight_Squat", 4, reps = 18),
                    item("High_Knees", 4, seconds = 40, rest = 25),
                    item("Burpee", 3, seconds = 35, rest = 40)
                ))
            )
        ),
        Plan(
            id = "bodyweight_plus",
            name = "Bodyweight Plus",
            description = "Harder no-equipment moves for two weeks. Three days a week, about 25 minutes. Nothing needed but floor space.",
            level = "Intermediate",
            weeks = 2,
            daysPerWeek = 3,
            equipment = "None",
            days = listOf(
                PlanDay("Day 1: Push and core", listOf(
                    item("Pushups", 3, reps = 10, rest = 60),
                    item("Push-Ups_-_Close_Triceps_Position", 3, reps = 8, rest = 60),
                    item("Sit-Up", 3, reps = 12),
                    item("Side_Bridge", 3, seconds = 30, rest = 45)
                )),
                PlanDay("Day 2: Legs and glutes", listOf(
                    item("Bodyweight_Squat", 4, reps = 12, rest = 60),
                    item("Standing_Calf_Raise", 3, reps = 15),
                    item("Butt_Lift_Bridge", 3, reps = 12),
                    item("Superman", 3, reps = 12)
                )),
                PlanDay("Day 3: Full body", listOf(
                    item("Burpee", 3, reps = 8, rest = 60),
                    item("Pushups", 3, reps = 10, rest = 60),
                    item("Leg_Lift", 3, reps = 12),
                    item("Lower_Back_Curl", 3, reps = 12)
                )),
                PlanDay("Day 4: Push and core, round 2", listOf(
                    item("Pushups", 4, reps = 10, rest = 60),
                    item("Push-Ups_-_Close_Triceps_Position", 4, reps = 8, rest = 60),
                    item("Sit-Up", 4, reps = 12),
                    item("Side_Bridge", 3, seconds = 40, rest = 45)
                )),
                PlanDay("Day 5: Legs and glutes, round 2", listOf(
                    item("Bodyweight_Squat", 4, reps = 15, rest = 60),
                    item("Standing_Calf_Raise", 4, reps = 15),
                    item("Butt_Lift_Bridge", 4, reps = 12),
                    item("Superman", 4, reps = 12)
                )),
                PlanDay("Day 6: Full body, round 2", listOf(
                    item("Burpee", 4, reps = 8, rest = 60),
                    item("Pushups", 4, reps = 10, rest = 60),
                    item("Leg_Lift", 4, reps = 12),
                    item("Cross-Body_Crunch", 4, reps = 16)
                ))
            )
        )
    )

    private val extraPlans: List<Plan> = listOf(
        Plan(
            id = "morning_wake_up",
            name = "Morning Wake-Up",
            description = "Gentle mobility and stretching to start the day loose. Three mornings a week, about 10 minutes. No equipment.",
            level = "Beginner",
            weeks = 2,
            daysPerWeek = 3,
            equipment = "None",
            days = listOf(
                PlanDay("Day 1: Wake up A", listOf(
                    item("Quad_Stretch", 2, seconds = 40, rest = 30),
                    item("Pelvic_Tilt", 2, reps = 12),
                    item("Superman", 2, reps = 10),
                    item("Chest_Stretch", 2, seconds = 40, rest = 30)
                )),
                PlanDay("Day 2: Wake up B", listOf(
                    item("Standing_Back_Stretch", 2, seconds = 40, rest = 30),
                    item("Butt_Lift_Bridge", 2, reps = 12),
                    item("Lower_Back_Curl", 2, reps = 12),
                    item("Quad_Stretch", 2, seconds = 40, rest = 30)
                )),
                PlanDay("Day 3: Wake up C", listOf(
                    item("Chest_Stretch", 2, seconds = 40, rest = 30),
                    item("Pelvic_Tilt", 2, reps = 12),
                    item("Butt_Lift_Bridge", 2, reps = 12),
                    item("Standing_Back_Stretch", 2, seconds = 40, rest = 30),
                    item("Star_Jump", 2, seconds = 20)
                )),
                PlanDay("Day 4: Wake up A, round 2", listOf(
                    item("Quad_Stretch", 3, seconds = 45, rest = 30),
                    item("Pelvic_Tilt", 3, reps = 12),
                    item("Superman", 3, reps = 12),
                    item("Chest_Stretch", 3, seconds = 45, rest = 30)
                )),
                PlanDay("Day 5: Wake up B, round 2", listOf(
                    item("Standing_Back_Stretch", 3, seconds = 45, rest = 30),
                    item("Butt_Lift_Bridge", 3, reps = 12),
                    item("Lower_Back_Curl", 3, reps = 14),
                    item("Quad_Stretch", 3, seconds = 45, rest = 30)
                )),
                PlanDay("Day 6: Wake up C, round 2", listOf(
                    item("Chest_Stretch", 3, seconds = 45, rest = 30),
                    item("Pelvic_Tilt", 3, reps = 12),
                    item("Butt_Lift_Bridge", 3, reps = 12),
                    item("Standing_Back_Stretch", 3, seconds = 45, rest = 30),
                    item("Star_Jump", 2, seconds = 30)
                ))
            )
        ),
        Plan(
            id = "quick_7",
            name = "Quick 7 Express",
            description = "A 7 minute daily circuit for busy days. Every day for one week, short timed sets. No equipment.",
            level = "Beginner",
            weeks = 1,
            daysPerWeek = 7,
            equipment = "None",
            days = listOf(
                PlanDay("Day 1: Circuit A", listOf(
                    item("High_Knees", 2, seconds = 30, rest = 20),
                    item("Bodyweight_Squat", 2, reps = 12, rest = 20),
                    item("Pushups", 2, reps = 8, rest = 20),
                    item("Crunches", 2, reps = 12, rest = 20)
                )),
                PlanDay("Day 2: Circuit B", listOf(
                    item("Star_Jump", 2, seconds = 30, rest = 20),
                    item("Crunches", 2, reps = 12, rest = 20),
                    item("Standing_Calf_Raise", 2, reps = 12, rest = 20),
                    item("Side_Bridge", 2, seconds = 20, rest = 20)
                )),
                PlanDay("Day 3: Circuit C", listOf(
                    item("Skier_Jacks", 2, seconds = 30, rest = 20),
                    item("Push-Ups_-_Close_Triceps_Position", 2, reps = 10, rest = 20),
                    item("Bent-Knee_Hip_Raise", 2, reps = 12, rest = 20),
                    item("Butt_Lift_Bridge", 2, reps = 12, rest = 20)
                )),
                PlanDay("Day 4: Circuit A, round 2", listOf(
                    item("High_Knees", 3, seconds = 30, rest = 20),
                    item("Bodyweight_Squat", 3, reps = 12, rest = 20),
                    item("Pushups", 3, reps = 8, rest = 20),
                    item("Crunches", 2, reps = 15, rest = 20)
                )),
                PlanDay("Day 5: Circuit B, round 2", listOf(
                    item("Star_Jump", 3, seconds = 30, rest = 20),
                    item("Crunches", 3, reps = 12, rest = 20),
                    item("Standing_Calf_Raise", 3, reps = 12, rest = 20),
                    item("Side_Bridge", 2, seconds = 25, rest = 20)
                )),
                PlanDay("Day 6: Circuit C, round 2", listOf(
                    item("Skier_Jacks", 3, seconds = 30, rest = 20),
                    item("Push-Ups_-_Close_Triceps_Position", 3, reps = 10, rest = 20),
                    item("Bent-Knee_Hip_Raise", 3, reps = 12, rest = 20),
                    item("Leg_Lift", 2, reps = 10, rest = 20)
                )),
                PlanDay("Day 7: Finisher mix", listOf(
                    item("Star_Jump", 2, seconds = 30, rest = 20),
                    item("Pushups", 2, reps = 10, rest = 20),
                    item("High_Knees", 2, seconds = 30, rest = 20),
                    item("Bodyweight_Squat", 2, reps = 14, rest = 20),
                    item("Burpee", 2, seconds = 20, rest = 20)
                ))
            )
        )
    )

    val plans: List<Plan> get() = corePlans + extraPlans

    fun byId(id: String): Plan? = plans.firstOrNull { it.id == id }
}
