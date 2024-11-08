package AniMo.com.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import AniMo.com.databinding.FragmentHomeBinding
import AniMo.com.animations.PetAnimator

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root = binding.root

        // Initialize HomeViewModel
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        // Observe background resource ID and set the background image
        homeViewModel.backgroundResId.observe(viewLifecycleOwner) { resourceId ->
            binding.roomBackground.setImageResource(resourceId)
        }

        // Observe carpet resource ID and set the background image
        homeViewModel.carpetResId.observe(viewLifecycleOwner) { resourceId ->
            binding.carpetImageView.setImageResource(resourceId)
        }

        // Initialize PetAnimator through HomeViewModel
        homeViewModel.initializePetAnimator(requireContext(), binding.petImageView)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        homeViewModel.stopPetAnimator()  // Stop the animator to release resources
        _binding = null
    }
}