package AniMo.com.ui.home

import AniMo.com.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import AniMo.com.databinding.FragmentHomeBinding

import AniMo.com.animations.PetAnimator
import AniMo.com.animations.SpriteAnimationView
import AniMo.com.ui.inventory.InventoryViewModel
import android.view.Gravity
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.FrameLayout

import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root = binding.root

        // init viewmodel factory
        val sharedPreferences = requireActivity().getSharedPreferences("user", MODE_PRIVATE)
        val uid = sharedPreferences.getString("uid", "")
        val viewModelFactory = HomeViewModel.HomeViewModelFactory(uid)


        // Initialize HomeViewModel
        homeViewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        // Observe background resource ID and set the background image
        homeViewModel.backgroundResId.observe(viewLifecycleOwner) { resourceId ->
            binding.roomBackground.setImageResource(resourceId)
        }
        homeViewModel.getEquippedBackground()


        // Observe carpet resource ID and set the background image
        homeViewModel.carpetResId.observe(viewLifecycleOwner) { resourceId ->
            binding.carpetImageView.setImageResource(resourceId)
        }

        // Find SpriteAnimationView for the pet
        val spriteAnimationView = binding.spriteAnimationView

        // Observe current animation and play it
        homeViewModel.currentAnimation.observe(viewLifecycleOwner) { animationType ->
            animationType?.let { spriteAnimationView.playAnimation(it) }
        }

        // Handle pet click to show interaction bubble
        spriteAnimationView.setOnClickListener {
            showInteractionBubble(it)
        }

        return root
    }

    private fun showInteractionBubble(anchorView: View) {
        // Inflate the chatbox_bubble layout
        val chatboxView = LayoutInflater.from(context).inflate(R.layout.chatbox_bubble, null)
        val parent = anchorView.rootView as ViewGroup

        // Add the chatbox to the parent layout with default params (position will be updated later)
        val layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        parent.addView(chatboxView, layoutParams)

        // Use ViewTreeObserver to adjust the position after measuring the layout
        chatboxView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // Remove the listener to prevent repeated calls
                chatboxView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                // Get the location of the pet (anchorView)
                val location = IntArray(2)
                anchorView.getLocationOnScreen(location)
                val petX = location[0]
                val petY = location[1]

                // Center-align the chatbox horizontally above the pet
                layoutParams.leftMargin = petX + (anchorView.width / 2) - (chatboxView.width / 2)
                layoutParams.topMargin = petY - chatboxView.height - 16 // Place above the pet with a margin

                // Apply the updated layout params
                chatboxView.layoutParams = layoutParams
            }
        })

        // Set up button actions
        chatboxView.findViewById<Button>(R.id.chatbox_option_feel).setOnClickListener {
            homeViewModel.handleInteraction("How do you feel?", 4000L)
            parent.removeView(chatboxView)
        }

        chatboxView.findViewById<Button>(R.id.chatbox_option_feed).setOnClickListener {
            homeViewModel.handleInteraction("Feed", 3000L)
            parent.removeView(chatboxView)
        }

        chatboxView.findViewById<Button>(R.id.chatbox_option_play).setOnClickListener {
            homeViewModel.handleInteraction("Play", 5000L)
            parent.removeView(chatboxView)
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}