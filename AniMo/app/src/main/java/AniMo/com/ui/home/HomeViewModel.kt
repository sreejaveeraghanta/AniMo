package AniMo.com.ui.home

import AniMo.com.R
import AniMo.com.animations.PetAnimator
import android.content.Context
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private var petAnimator: PetAnimator? = null

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

    // Initialize PetAnimator and start wandering
    fun initializePetAnimator(context: Context, petView: ImageView) {
        petAnimator = PetAnimator(context, petView).apply {
            startAnimation()
        }
    }

    // Stop the animator to release resources when no longer needed
    fun stopPetAnimator() {
        petAnimator?.stop()
    }
}