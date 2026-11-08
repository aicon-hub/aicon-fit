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

    val plans: List<Plan> = listOf(
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
                    item("Incline_Push-Up", 3, reps = 10),
                    item("Superman", 3, reps = 10),
                    item("Dead_Bug", 3, reps = 8)
                )),
                PlanDay("Day 3: Full body C", listOf(
                    item("Bodyweight_Squat", 3, reps = 12),
                    item("Pushups", 3, reps = 10, rest = 60),
                    item("Flat_Bench_Lying_Leg_Raise", 3, reps = 10),
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
                    item("Incline_Push-Up", 4, reps = 12),
                    item("Superman", 4, reps = 12),
                    item("Dead_Bug", 4, reps = 10)
                )),
                PlanDay("Day 6: Full body C, round 2", listOf(
                    item("Freehand_Jump_Squat", 3, reps = 8),
                    item("Pushups", 4, reps = 10, rest = 60),
                    item("Flat_Bench_Lying_Leg_Raise", 4, reps = 12),
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
                    item("Scissors_Jump", 3, seconds = 40, rest = 30),
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
                    item("Scissors_Jump", 4, seconds = 40, rest = 25),
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
            id = "dumbbell_basics",
            name = "Dumbbell Basics",
            description = "Two weeks of simple dumbbell strength work. Three days a week, about 25 minutes. You need one pair of dumbbells.",
            level = "Beginner",
            weeks = 2,
            daysPerWeek = 3,
            equipment = "Dumbbells",
            days = listOf(
                PlanDay("Day 1: Upper", listOf(
                    item("Dumbbell_Bench_Press", 3, reps = 10, rest = 60),
                    item("One-Arm_Dumbbell_Row", 3, reps = 10, rest = 60),
                    item("Dumbbell_Shoulder_Press", 3, reps = 10, rest = 60),
                    item("Dumbbell_Bicep_Curl", 3, reps = 12)
                )),
                PlanDay("Day 2: Lower", listOf(
                    item("Goblet_Squat", 3, reps = 10, rest = 60),
                    item("Dumbbell_Lunges", 3, reps = 10, rest = 60),
                    item("Stiff-Legged_Dumbbell_Deadlift", 3, reps = 10, rest = 60),
                    item("Standing_Dumbbell_Calf_Raise", 3, reps = 15)
                )),
                PlanDay("Day 3: Full body", listOf(
                    item("Dumbbell_Squat", 3, reps = 10, rest = 60),
                    item("Dumbbell_Bench_Press", 3, reps = 10, rest = 60),
                    item("Bent_Over_Two-Dumbbell_Row", 3, reps = 10, rest = 60),
                    item("Alternate_Hammer_Curl", 3, reps = 12),
                    item("Tricep_Dumbbell_Kickback", 3, reps = 12)
                )),
                PlanDay("Day 4: Upper, round 2", listOf(
                    item("Dumbbell_Bench_Press", 4, reps = 10, rest = 60),
                    item("One-Arm_Dumbbell_Row", 4, reps = 10, rest = 60),
                    item("Dumbbell_Shoulder_Press", 4, reps = 10, rest = 60),
                    item("Hammer_Curls", 3, reps = 12),
                    item("Dumbbell_Tricep_Extension_-Pronated_Grip", 3, reps = 12)
                )),
                PlanDay("Day 5: Lower, round 2", listOf(
                    item("Goblet_Squat", 4, reps = 10, rest = 60),
                    item("Dumbbell_Lunges", 4, reps = 10, rest = 60),
                    item("Stiff-Legged_Dumbbell_Deadlift", 4, reps = 10, rest = 60),
                    item("Standing_Dumbbell_Calf_Raise", 4, reps = 15),
                    item("Single_Leg_Glute_Bridge", 3, reps = 10)
                )),
                PlanDay("Day 6: Full body, round 2", listOf(
                    item("Dumbbell_Squat", 4, reps = 10, rest = 60),
                    item("Dumbbell_Flyes", 3, reps = 12, rest = 60),
                    item("Bent_Over_Two-Dumbbell_Row", 4, reps = 10, rest = 60),
                    item("Front_Dumbbell_Raise", 3, reps = 12),
                    item("Plank", 3, seconds = 45, rest = 60)
                ))
            )
        )
    )

    fun byId(id: String): Plan? = plans.firstOrNull { it.id == id }
}
