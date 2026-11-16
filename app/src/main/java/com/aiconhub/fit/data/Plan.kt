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
                    item("Plank", 3, seconds = 30, rest = 60)
                )),
                PlanDay("Day 2: Full body B", listOf(
                    item("Bodyweight_Walking_Lunge", 3, reps = 10),
                    item("Isometric_Chest_Squeezes", 3, reps = 10),
                    item("Superman", 3, reps = 10),
                    item("Dead_Bug", 3, reps = 8)
                )),
                PlanDay("Day 3: Full body C", listOf(
                    item("Bodyweight_Squat", 3, reps = 12),
                    item("Pushups", 3, reps = 10, rest = 60),
                    item("Leg_Lift", 3, reps = 10),
                    item("Push_Up_to_Side_Plank", 3, reps = 6, rest = 60)
                )),
                PlanDay("Day 4: Full body A, round 2", listOf(
                    item("Bodyweight_Squat", 4, reps = 12),
                    item("Pushups", 4, reps = 8, rest = 60),
                    item("Butt_Lift_Bridge", 4, reps = 12),
                    item("Plank", 4, seconds = 40, rest = 60)
                )),
                PlanDay("Day 5: Full body B, round 2", listOf(
                    item("Bodyweight_Walking_Lunge", 4, reps = 10),
                    item("Isometric_Chest_Squeezes", 4, reps = 12),
                    item("Superman", 4, reps = 12),
                    item("Dead_Bug", 4, reps = 10)
                )),
                PlanDay("Day 6: Full body C, round 2", listOf(
                    item("Freehand_Jump_Squat", 3, reps = 8),
                    item("Pushups", 4, reps = 10, rest = 60),
                    item("Leg_Lift", 4, reps = 12),
                    item("Plank", 3, seconds = 45, rest = 60)
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
                    item("Reverse_Crunch", 3, reps = 12),
                    item("Plank", 3, seconds = 30, rest = 60)
                )),
                PlanDay("Day 2: Core B", listOf(
                    item("Russian_Twist", 3, reps = 16),
                    item("Side_Bridge", 3, seconds = 25, rest = 60),
                    item("Flutter_Kicks", 3, seconds = 30)
                )),
                PlanDay("Day 3: Core C", listOf(
                    item("Sit-Up", 3, reps = 12),
                    item("Cross-Body_Crunch", 3, reps = 16),
                    item("Scissor_Kick", 3, seconds = 30),
                    item("Plank", 3, seconds = 40, rest = 60)
                )),
                PlanDay("Day 4: Core A, round 2", listOf(
                    item("Crunches", 4, reps = 15),
                    item("Reverse_Crunch", 4, reps = 12),
                    item("Tuck_Crunch", 3, reps = 12),
                    item("Plank", 3, seconds = 45, rest = 60)
                )),
                PlanDay("Day 5: Core B, round 2", listOf(
                    item("Russian_Twist", 4, reps = 20),
                    item("Side_Bridge", 3, seconds = 35, rest = 60),
                    item("Flutter_Kicks", 4, seconds = 30)
                )),
                PlanDay("Day 6: Core C, round 2", listOf(
                    item("Sit-Up", 4, reps = 12),
                    item("Cross-Body_Crunch", 4, reps = 16),
                    item("Scissor_Kick", 4, seconds = 35),
                    item("Plank", 4, seconds = 45, rest = 60)
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
                    item("Double_Leg_Butt_Kick", 3, seconds = 40, rest = 30),
                    item("Mountain_Climbers", 3, seconds = 40, rest = 30),
                    item("Freehand_Jump_Squat", 3, seconds = 30, rest = 45),
                    item("Plank", 2, seconds = 30, rest = 45)
                )),
                PlanDay("Day 2: Power", listOf(
                    item("Knee_Tuck_Jump", 3, seconds = 30, rest = 45),
                    item("Pushups", 3, reps = 10, rest = 45),
                    item("Double_Leg_Butt_Kick", 3, seconds = 40, rest = 30),
                    item("Mountain_Climbers", 3, seconds = 40, rest = 30)
                )),
                PlanDay("Day 3: Mix", listOf(
                    item("Inchworm", 3, reps = 6, rest = 45),
                    item("Freehand_Jump_Squat", 3, seconds = 40, rest = 30),
                    item("Double_Leg_Butt_Kick", 3, seconds = 40, rest = 30),
                    item("Push_Up_to_Side_Plank", 3, reps = 8, rest = 45)
                )),
                PlanDay("Day 4: Movers, round 2", listOf(
                    item("Double_Leg_Butt_Kick", 4, seconds = 40, rest = 25),
                    item("Mountain_Climbers", 4, seconds = 40, rest = 25),
                    item("Freehand_Jump_Squat", 4, seconds = 35, rest = 40),
                    item("Plank", 3, seconds = 40, rest = 45)
                )),
                PlanDay("Day 5: Power, round 2", listOf(
                    item("Knee_Tuck_Jump", 4, seconds = 30, rest = 40),
                    item("Pushups", 4, reps = 10, rest = 40),
                    item("Double_Leg_Butt_Kick", 4, seconds = 40, rest = 25),
                    item("Mountain_Climbers", 4, seconds = 40, rest = 25)
                )),
                PlanDay("Day 6: Mix, round 2", listOf(
                    item("Inchworm", 4, reps = 8, rest = 40),
                    item("Freehand_Jump_Squat", 4, seconds = 40, rest = 25),
                    item("Double_Leg_Butt_Kick", 4, seconds = 40, rest = 25),
                    item("Push_Up_to_Side_Plank", 4, reps = 10, rest = 40)
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
                    item("Clock_Push-Up", 3, reps = 8, rest = 60),
                    item("Push-Up_Wide", 3, reps = 10, rest = 60),
                    item("Tuck_Crunch", 3, reps = 12),
                    item("Side_Bridge", 3, seconds = 30, rest = 45)
                )),
                PlanDay("Day 2: Legs and glutes", listOf(
                    item("Crossover_Reverse_Lunge", 3, reps = 10, rest = 60),
                    item("Single_Leg_Glute_Bridge", 3, reps = 10),
                    item("Lateral_Bound", 3, reps = 12, rest = 60),
                    item("Glute_Kickback", 3, reps = 12)
                )),
                PlanDay("Day 3: Full body", listOf(
                    item("Inchworm", 3, reps = 8, rest = 60),
                    item("Push_Up_to_Side_Plank", 3, reps = 8, rest = 60),
                    item("Bodyweight_Walking_Lunge", 3, reps = 12),
                    item("Superman", 3, reps = 12)
                )),
                PlanDay("Day 4: Push and core, round 2", listOf(
                    item("Clock_Push-Up", 4, reps = 8, rest = 60),
                    item("Push-Up_Wide", 4, reps = 10, rest = 60),
                    item("Tuck_Crunch", 4, reps = 12),
                    item("Side_Bridge", 3, seconds = 40, rest = 45)
                )),
                PlanDay("Day 5: Legs and glutes, round 2", listOf(
                    item("Crossover_Reverse_Lunge", 4, reps = 10, rest = 60),
                    item("Single_Leg_Glute_Bridge", 4, reps = 10),
                    item("Lateral_Bound", 4, reps = 12, rest = 60),
                    item("Glute_Kickback", 4, reps = 12)
                )),
                PlanDay("Day 6: Full body, round 2", listOf(
                    item("Inchworm", 4, reps = 8, rest = 60),
                    item("Push_Up_to_Side_Plank", 4, reps = 8, rest = 60),
                    item("Bodyweight_Walking_Lunge", 4, reps = 12),
                    item("Plank", 3, seconds = 45, rest = 60)
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
                    item("90_90_Hamstring", 2, seconds = 40, rest = 30),
                    item("Inchworm", 2, reps = 6, rest = 30),
                    item("Superman", 3, reps = 10),
                    item("Toe_Touchers", 2, seconds = 40, rest = 30)
                )),
                PlanDay("Day 2: Wake up B", listOf(
                    item("Groiners", 2, seconds = 30, rest = 30),
                    item("Rear_Leg_Raises", 3, reps = 12),
                    item("Lower_Back_Curl", 3, reps = 12),
                    item("Scissor_Kick", 2, seconds = 30, rest = 30)
                )),
                PlanDay("Day 3: Wake up C", listOf(
                    item("90_90_Hamstring", 2, seconds = 40, rest = 30),
                    item("Inchworm", 2, reps = 8, rest = 30),
                    item("Side_Leg_Raises", 3, reps = 12),
                    item("Superman", 3, reps = 12),
                    item("Toe_Touchers", 2, seconds = 40, rest = 30)
                )),
                PlanDay("Day 4: Wake up A, round 2", listOf(
                    item("90_90_Hamstring", 3, seconds = 45, rest = 30),
                    item("Inchworm", 3, reps = 8, rest = 30),
                    item("Superman", 3, reps = 12),
                    item("Toe_Touchers", 3, seconds = 45, rest = 30)
                )),
                PlanDay("Day 5: Wake up B, round 2", listOf(
                    item("Groiners", 3, seconds = 35, rest = 30),
                    item("Rear_Leg_Raises", 3, reps = 14),
                    item("Lower_Back_Curl", 3, reps = 14),
                    item("Scissor_Kick", 3, seconds = 35, rest = 30)
                )),
                PlanDay("Day 6: Wake up C, round 2", listOf(
                    item("90_90_Hamstring", 3, seconds = 45, rest = 30),
                    item("Inchworm", 3, reps = 10, rest = 30),
                    item("Side_Leg_Raises", 3, reps = 14),
                    item("Superman", 3, reps = 14),
                    item("Toe_Touchers", 3, seconds = 45, rest = 30)
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
                    item("Mountain_Climbers", 2, seconds = 30, rest = 20),
                    item("Bodyweight_Squat", 2, reps = 12, rest = 20),
                    item("Pushups", 2, reps = 8, rest = 20),
                    item("Plank", 2, seconds = 30, rest = 20)
                )),
                PlanDay("Day 2: Circuit B", listOf(
                    item("Star_Jump", 2, seconds = 30, rest = 20),
                    item("Crunches", 2, reps = 12, rest = 20),
                    item("Bodyweight_Walking_Lunge", 2, reps = 10, rest = 20),
                    item("Side_Bridge", 2, seconds = 20, rest = 20)
                )),
                PlanDay("Day 3: Circuit C", listOf(
                    item("Double_Leg_Butt_Kick", 2, seconds = 30, rest = 20),
                    item("Push-Ups_-_Close_Triceps_Position", 2, reps = 10, rest = 20),
                    item("Reverse_Crunch", 2, reps = 12, rest = 20),
                    item("Plank", 2, seconds = 30, rest = 20)
                )),
                PlanDay("Day 4: Circuit A, round 2", listOf(
                    item("Mountain_Climbers", 3, seconds = 30, rest = 20),
                    item("Bodyweight_Squat", 3, reps = 12, rest = 20),
                    item("Pushups", 3, reps = 8, rest = 20),
                    item("Plank", 2, seconds = 35, rest = 20)
                )),
                PlanDay("Day 5: Circuit B, round 2", listOf(
                    item("Star_Jump", 3, seconds = 30, rest = 20),
                    item("Crunches", 3, reps = 12, rest = 20),
                    item("Bodyweight_Walking_Lunge", 3, reps = 10, rest = 20),
                    item("Side_Bridge", 2, seconds = 25, rest = 20)
                )),
                PlanDay("Day 6: Circuit C, round 2", listOf(
                    item("Double_Leg_Butt_Kick", 3, seconds = 30, rest = 20),
                    item("Push-Ups_-_Close_Triceps_Position", 3, reps = 10, rest = 20),
                    item("Reverse_Crunch", 3, reps = 12, rest = 20),
                    item("Plank", 2, seconds = 40, rest = 20)
                )),
                PlanDay("Day 7: Finisher mix", listOf(
                    item("Star_Jump", 2, seconds = 30, rest = 20),
                    item("Pushups", 2, reps = 10, rest = 20),
                    item("Mountain_Climbers", 2, seconds = 30, rest = 20),
                    item("Bodyweight_Squat", 2, reps = 14, rest = 20),
                    item("Plank", 2, seconds = 45, rest = 20)
                ))
            )
        )
    )

    val plans: List<Plan> get() = corePlans + extraPlans

    fun byId(id: String): Plan? = plans.firstOrNull { it.id == id }
}
