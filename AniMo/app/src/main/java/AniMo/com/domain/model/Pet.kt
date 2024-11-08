package AniMo.com.domain.model

data class Pet(
    var xPosition: Float = 0f,
    var yPosition: Float = 0f,
    val spriteResId: Int // Resource ID of the pet’s initial image
)