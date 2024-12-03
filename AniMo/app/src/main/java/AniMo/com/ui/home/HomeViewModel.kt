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

    class HomeViewModelFactory (private val uid: String?) : ViewModelProvider.Factory {
        override fun<T: ViewModel> create(modelClass: Class<T>) : T{ //create() creates a new instance of the modelClass, which is CommentViewModel in this case.
            if(modelClass.isAssignableFrom(HomeViewModel::class.java))
                return HomeViewModel(uid) as T
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}