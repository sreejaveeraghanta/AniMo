package AniMo.com.ui.inventory

import AniMo.com.R
import AniMo.com.database.Item
import AniMo.com.databinding.FragmentInventoryBinding
import AniMo.com.ui.home.HomeFragment
import AniMo.com.ui.home.HomeViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InventoryFragment : Fragment() {
    private var _binding: FragmentInventoryBinding? = null

//    // This property is only valid between onCreateView and
//    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var backButton: Button
    private lateinit var itemGrid: GridView
    private lateinit var gridAdapt: InventoryGridAdapter

    private lateinit var viewModelFactory: InventoryViewModel.InventoryViewModelFactory
    private lateinit var invenViewModel: InventoryViewModel
    private lateinit var arrayList: ArrayList<Item>
    private lateinit var database: FirebaseDatabase
    private lateinit var userReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInventoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // back
        backButton = root.findViewById(R.id.back_button)
        backButton.setOnClickListener(){
            findNavController().navigate(R.id.action_inventoryFragment_to_storeFragment)
        }


        // get user
        val sharedPreferences = requireActivity().getSharedPreferences("user", MODE_PRIVATE)
        val uid = sharedPreferences.getString("uid", "")
        database = FirebaseDatabase.getInstance()
        userReference = database.getReference("Users")
        val homeFragment = parentFragment as? HomeFragment
        val homeViewModel = homeFragment?.homeViewModel
//        // get home
//        val viewModelFactory1 = HomeViewModel.HomeViewModelFactory(uid)
//        // Initialize HomeViewModel
//        val homeViewModel = ViewModelProvider(this, viewModelFactory1).get(HomeViewModel::class.java)

        viewModelFactory = InventoryViewModel.InventoryViewModelFactory(uid, homeViewModel)
        invenViewModel = ViewModelProvider(this, viewModelFactory).get(InventoryViewModel::class.java)



        // set up gridview + adapter
        itemGrid = root.findViewById(R.id.inventoryItemsGrid)
        arrayList = ArrayList()
        gridAdapt = InventoryGridAdapter(arrayList, invenViewModel, requireActivity(), viewLifecycleOwner)
        itemGrid.adapter = gridAdapt
        invenViewModel.userbgs.observe(viewLifecycleOwner){
            if (uid != null) {
                val toShow = it
                // Update the adapter with the filtered list
                gridAdapt.replace(toShow)
                gridAdapt.notifyDataSetChanged()
            }
        }

        invenViewModel.getUserItems()
        return root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}