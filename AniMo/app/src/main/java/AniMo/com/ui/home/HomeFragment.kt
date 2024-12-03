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

        // Initialize PetAnimator through HomeViewModel
        //homeViewModel.initializePetAnimator(requireContext(), binding.petImageView)

        // Find SpriteAnimationView for the pet
        val spriteAnimationView = view?.findViewById<SpriteAnimationView>(R.id.spriteAnimationView)

        // Observe current animation and play it
        homeViewModel.currentAnimation.observe(viewLifecycleOwner) { animationType ->
            if (animationType != null) {
                spriteAnimationView?.playAnimation(animationType)
            }
        }

        // Handle pet click to show interaction bubble
        spriteAnimationView?.setOnClickListener {
            showInteractionBubble(it)
        }

        return root
    }

    private fun showInteractionBubble(view: View) {
        // Create a popup menu for interactions
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.pet_interaction_menu, popupMenu.menu)

        // Handle menu item clicks
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_feel -> homeViewModel.handleInteraction("How do you feel?", 4000L)
            }
            true
        }
        popupMenu.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //homeViewModel.stopPetAnimator()  // Stop the animator to release resources
        _binding = null
    }
}