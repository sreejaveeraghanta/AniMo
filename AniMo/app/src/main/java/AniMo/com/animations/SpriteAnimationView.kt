package AniMo.com.animations

import AniMo.com.R
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SpriteAnimationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var spriteSheet: Bitmap? = null
    private var frameWidth: Int = 0
    private var frameHeight: Int = 0
    private var currentFrame: Int = 0
    private var frameCount: Int = 0
    private var frameDuration: Long = 50L
    private var currentAnimationType: String? = null
    private var pingPongMode: Boolean = false
    private var isReversed = false // To track ascending or descending order
    private val paint = Paint()

    private val handler = Handler(Looper.getMainLooper())
    private val animationRunnable = object : Runnable {
        override fun run() {
            if (frameCount > 0) {
                // Update frame index based on ping-pong mode
                currentFrame = if (pingPongMode) {
                    if (isReversed) currentFrame - 1 else currentFrame + 1
                } else {
                    (currentFrame + 1) % frameCount
                }

                // Reverse direction at edges in ping-pong mode
                if (pingPongMode) {
                    if (currentFrame == 0) isReversed = false
                    else if (currentFrame == frameCount - 1) isReversed = true
                }

                invalidate() // Redraw the view
                handler.postDelayed(this, frameDuration)
            }
        }
    }

    init {
        // Set a default sprite sheet
        playAnimation("idle_animation")
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (spriteSheet == null) return // Graceful null handling

        spriteSheet?.let {
            val frameX = (currentFrame % (it.width / frameWidth)) * frameWidth
            val frameY = (currentFrame / (it.width / frameWidth)) * frameHeight

            val srcRect = Rect(frameX, frameY, frameX + frameWidth, frameY + frameHeight)
            val destRect = Rect(0, 0, width, height)

            canvas.drawBitmap(it, srcRect, destRect, paint)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAnimation()
    }

    fun startAnimation() {
        stopAnimation()
        handler.post(animationRunnable)
    }

    fun stopAnimation() {
        handler.removeCallbacksAndMessages(null)
    }

    /**
     * Set a new sprite sheet with its configuration.
     * @param resourceId Resource ID of the sprite sheet
     * @param rows Number of rows in the sprite sheet
     * @param columns Number of columns in the sprite sheet
     * @param totalFrames Total number of frames in the animation
     * @param frameDuration Duration of each frame in milliseconds (optional)
     * @param pingPongMode Whether the animation should play in ping-pong mode
     */
    private fun loadSpriteSheetAsync(
        resourceId: Int,
        rows: Int,
        columns: Int,
        totalFrames: Int,
        frameDuration: Long,
        pingPongMode: Boolean
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val options = BitmapFactory.Options().apply {
                    inPreferredConfig = Bitmap.Config.RGB_565
                    inSampleSize = 3
                }
                val bitmap = BitmapFactory.decodeResource(resources, resourceId, options)

                withContext(Dispatchers.Main) {
                    spriteSheet = bitmap
                    frameWidth = bitmap.width / columns
                    frameHeight = bitmap.height / rows
                    this@SpriteAnimationView.frameCount = totalFrames
                    this@SpriteAnimationView.frameDuration = frameDuration
                    this@SpriteAnimationView.pingPongMode = pingPongMode
                    this@SpriteAnimationView.isReversed = false // Reset direction
                    currentFrame = 0
                    startAnimation()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle loading error, if necessary
            }
        }
    }

    /**
     * Play the specified animation type.
     */
    fun playAnimation(animationType: String) {
        if (currentAnimationType == animationType) return // Avoid restarting the same animation

        // Stop the current animation before starting a new one
        stopAnimation()

        currentAnimationType = animationType
        when (animationType) {
            "happy_animation" -> loadSpriteSheetAsync(
                resourceId = R.drawable.sheet_duck_happy,
                rows = 5,
                columns = 8,
                totalFrames = 40,
                frameDuration = 1000L / 60,
                pingPongMode = false
            )
            "idle_animation" -> loadSpriteSheetAsync(
                resourceId = R.drawable.sheet_duck_idle,
                rows = 4,
                columns = 7,
                totalFrames = 26,
                frameDuration = 1000L / 60,
                pingPongMode = true
            )
            else -> throw IllegalArgumentException("Unknown animation type: $animationType")
        }
        startAnimation()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            performClick()
            return true
        }
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}
