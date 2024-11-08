package AniMo.com.animations

import AniMo.com.R
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import kotlin.random.Random

class PetAnimator(private val context: Context, private val petView: ImageView) {

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var petAnimation: AnimationDrawable

    // Initialize the screen dimensions
    private val screenWidth = context.resources.displayMetrics.widthPixels
    private val screenHeight = context.resources.displayMetrics.heightPixels

    // Start the pet animation
    fun startAnimation() {
        handler.post(object : Runnable {
            override fun run() {

                // Randomly change the pet's animation
                when (Random.nextInt(4)) {
                    0 -> setAnimation(R.drawable.ani_cat_idle)
                    1 -> setAnimation(R.drawable.ani_cat_sit)
                    3 -> setAnimation(R.drawable.ani_cat_happy)
                    4 -> setAnimation(R.drawable.ani_cat_anticipate)
                }

                // Restart animation every 2 seconds
                handler.postDelayed(this, 2000)
            }
        })
    }

    // Set a specific animation based on the resource ID
    private fun setAnimation(animationRes: Int) {
        if (::petAnimation.isInitialized) {
            petAnimation.stop() // Stop any currently running animation
        }
        petView.setBackgroundResource(0)
        petView.setBackgroundResource(animationRes)
        petAnimation = petView.background as AnimationDrawable
        petAnimation.start()
    }


    // Stop the animations
    fun stop() {
        handler.removeCallbacksAndMessages(null)
        if (::petAnimation.isInitialized) {
            petAnimation.stop()
        }
    }
}
