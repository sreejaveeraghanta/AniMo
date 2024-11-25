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
    private var isReversed = false // To track ascending or descending order
    private val paint = Paint()

    private val handler = Handler(Looper.getMainLooper())
    private val animationRunnable = object : Runnable {
        override fun run() {
            if (frameCount > 0) {
                // Update current frame based on direction
                currentFrame = if (isReversed) {
                    currentFrame - 1
                } else {
                    currentFrame + 1
                }

                // Reverse direction at the ends
                if (currentFrame == 0) {
                    isReversed = false
                } else if (currentFrame == frameCount - 1) {
                    isReversed = true
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
    private fun setSpriteSheet(
        resourceId: Int,
        rows: Int,
        columns: Int,
        totalFrames: Int,
        frameDuration: Long = 50L,
        pingPongMode: Boolean = false
    ) {
        spriteSheet = BitmapFactory.decodeResource(resources, resourceId, BitmapFactory.Options().apply {
            inSampleSize = 3
        })
        frameWidth = spriteSheet?.width?.div(columns) ?: 0
        frameHeight = spriteSheet?.height?.div(rows) ?: 0
        this.frameCount = totalFrames
        this.frameDuration = frameDuration
        this.isReversed = false // Reset direction for ping-pong

        // If ping-pong mode, set current frame to 0
        if (pingPongMode) {
            currentFrame = 0
        }

        invalidate()
    }

    /**
     * Play the specified animation type.
     */
    fun playAnimation(animationType: String) {
        if (currentAnimationType == animationType) return // Avoid restarting the same animation

        currentAnimationType = animationType
        when (animationType) {
            "happy_animation" -> setSpriteSheet(
                resourceId = R.drawable.sheet_duck_happy,
                rows = 5,
                columns = 8,
                totalFrames = 40,
                frameDuration = 50L
            )
            "idle_animation" -> setSpriteSheet(
                resourceId = R.drawable.sheet_duck_idle,
                rows = 4,
                columns = 7,
                totalFrames = 26,
                frameDuration = 100L,
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
