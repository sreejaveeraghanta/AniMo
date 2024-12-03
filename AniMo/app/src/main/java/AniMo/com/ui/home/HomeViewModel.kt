package AniMo.com.ui.home

import AniMo.com.R

//import AniMo.com.animations.PetAnimator
import AniMo.com.database.Item
import AniMo.com.ui.FindIcon
import AniMo.com.ui.inventory.InventoryViewModel
import android.content.Context

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import AniMo.com.database.Pet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.FirebaseDatabase
import java.lang.IllegalArgumentException

class HomeViewModel(val uid: String?) : ViewModel() {

    private var finder: FindIcon = FindIcon()


    //private var petAnimator: PetAnimator? = null
    private val database = FirebaseDatabase.getInstance()
    private val userReference = database.reference.child("Users")
    private var bgReference = database.getReference("Backgrounds")



    // Background resource ID
    private val _backgroundResId = MutableLiveData<Int>().apply {
        value = R.drawable.bg_purple  // Default background
    }
    val backgroundResId: LiveData<Int> = _backgroundResId

    // Method to change background
    fun setBackground(resourceId: Int) {
        _backgroundResId.value = resourceId
    }

    fun getEquippedBackground(){
        if (uid != null) {
            val allitemslist = mutableListOf<Item>() // MutableList

            // get list of items
            bgReference.get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    for (bgdata in snapshot.children) {
                        val bg = bgdata.getValue(Item::class.java)
                        if (bg != null) {
                            allitemslist.add(bg) // Add song to the mutable list
                        }
                    }
                }
            }

            userReference.child(uid).get().addOnSuccessListener { snapshot ->
                if (snapshot.exists() && snapshot.hasChild("backgroundEquipped")) {
                    val equippedBackground = snapshot.child("backgroundEquipped").getValue(String::class.java)
                    for( item in allitemslist) {
                        if (item.name == equippedBackground) {
                            val resourceId = finder.findBGimg(item.icon)
                            setBackground(resourceId)
                        }
                    }
                }
            }
        }
    }

    private val _carpetResId = MutableLiveData<Int>().apply {
        value = R.drawable.obj_carpet_0 // Default carpet
    }
    val carpetResId: LiveData<Int> = _carpetResId

    fun setCarpet(resourceId: Int) {
        _carpetResId.value = resourceId
    }

    private val viewModelScope = CoroutineScope(Dispatchers.Main + Job())

    // Pet instance
    private val pet = Pet(
        mood = "Happy",
        hunger = 50,
        level = 1,
        experiencePoints = 0,
        currentAnimation = "teen_idle_animation"
    )

    fun setHungerToZero() {
        pet.hunger = 0
    }

    // Change the pet's level (e.g., evolve from Teen to Adult)
    fun changePetLevel(newLevel: Int) {
        pet.level = newLevel
        playAnimation("teen_grow_animation", 1000L) // Switch to appropriate idle animation
    }

    // LiveData for current animation
    private val _currentAnimation = MutableLiveData<String>().apply {
        value = pet.currentAnimation
    }
    val currentAnimation: LiveData<String> = _currentAnimation

    // Handle interactions
    fun handleInteraction(interactionType: String, duration: Long) {
        when (interactionType) {
            "How do you feel?" -> {
                val animationType = pet.evaluateMoodAnimation()
                playAnimation(animationType, duration)
            }
            "Feed" -> {
                pet.feed(20) // Feed the pet and increase hunger
                playAnimation(if (pet.level == 2) "feed_animation" else "teen_feed_animation", duration)
            }
            else -> playAnimation(pet.getIdleAnimation(), 0L)
        }
    }

    private fun playAnimation(animationType: String, duration: Long) {
        _currentAnimation.value = animationType

        if (duration > 0) {
            viewModelScope.launch {
                delay(duration)
                pet.currentAnimation = pet.getIdleAnimation()
                _currentAnimation.value = pet.getIdleAnimation()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel() // Clean up coroutines
    }

    class HomeViewModelFactory (private val uid: String?) : ViewModelProvider.Factory {
        override fun<T: ViewModel> create(modelClass: Class<T>) : T{ //create() creates a new instance of the modelClass, which is CommentViewModel in this case.
            if(modelClass.isAssignableFrom(HomeViewModel::class.java))
                return HomeViewModel(uid) as T
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}