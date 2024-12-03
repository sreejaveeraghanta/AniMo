package AniMo.com.database

data class Pet(
    var xPosition: Float = 0f,
    var yPosition: Float = 0f,
    var mood: String = "Happy", // e.g., Happy, Sad, Hungry
    var hunger: Int = 50, // Hunger level (0-100)
    var level: Int = 1, // Pet's current level
    var experiencePoints: Int = 0, // Experience points for progression
    var lastUpdated: Long = System.currentTimeMillis(), // Timestamp of the last state update
    var currentAnimation: String = "idle" // Current animation state
) {
    // Method to feed the pet
    fun feed(foodAmount: Int) {
        hunger = (hunger + foodAmount).coerceAtMost(100)
    }

    // Determine the appropriate idle animation based on level
    fun getIdleAnimation(): String {
        return if (level == 2) "idle_animation" else "teen_idle_animation"
    }

    // Method to update the pet's state based on elapsed time
    fun updateState() {
        val elapsedTime = (System.currentTimeMillis() - lastUpdated) / 1000 / 60 // Minutes elapsed
        hunger = (hunger - elapsedTime.toInt()).coerceAtLeast(0)
        mood = if (hunger <= 20) "Hungry" else mood
        lastUpdated = System.currentTimeMillis()
    }

    // Evaluate mood and return the corresponding animation
    fun evaluateMoodAnimation(): String {
        return when {
            hunger > 60 -> if (level == 2) "happy_animation" else "teen_happy_animation"
            hunger in 21..60 -> if (level == 2) "hungry_animation" else "teen_hungry_animation"
            else -> if (level == 2) "mad_animation" else "teen_mad_animation"
        }
    }
}