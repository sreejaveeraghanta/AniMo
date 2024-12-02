package AniMo.com.ui.home

import AniMo.com.R
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    // Background resource ID
    private val _backgroundResId = MutableLiveData<Int>().apply {
        value = R.drawable.bg_purple  // Default background
    }
    val backgroundResId: LiveData<Int> = _backgroundResId

    // Method to change background
    fun setBackground(resourceId: Int) {
        _backgroundResId.value = resourceId
    }

    private val _carpetResId = MutableLiveData<Int>().apply {
        value = R.drawable.obj_carpet_0 // Default carpet
    }
    val carpetResId: LiveData<Int> = _carpetResId

    fun setCarpet(resourceId: Int) {
        _carpetResId.value = resourceId
    }

    private val _currentAnimation = MutableLiveData<String?>()
    val currentAnimation: LiveData<String?> = _currentAnimation

    // Pet interaction handler
    fun handleInteraction(interactionType: String, duration: Long) {
        when (interactionType) {
            "How do you feel?" -> playAnimation("happy_animation", duration)
            else -> playAnimation("idle_animation", 0L) // Default fallback
        }
    }

    private fun playAnimation(animationType: String, duration: Long) {
        _currentAnimation.value = animationType

        if (duration > 0) {
            // Reset to idle after the animation duration
            Handler(Looper.getMainLooper()).postDelayed({
                _currentAnimation.value = "idle_animation"
            }, duration)
        }
    }

}