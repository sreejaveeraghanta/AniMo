package AniMo.com.ui.inventory

import AniMo.com.R
import AniMo.com.database.User
import AniMo.com.database.inventoryStore.InventoryDatabase
import AniMo.com.database.Item
import AniMo.com.database.inventoryStore.ItemDatabaseDao
import AniMo.com.database.inventoryStore.ItemRepository
import AniMo.com.databinding.FragmentInventoryBinding
//import AniMo.com.databinding.FragmentInventoryBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InventoryFragment : Fragment() {
    private var _binding: FragmentInventoryBinding? = null
//
//    // This property is only valid between onCreateView and
//    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var backButton: Button
    private lateinit var itemGrid: GridView
    private lateinit var gridAdapt: InventoryGridAdapter

//    private lateinit var database: InventoryDatabase
//    private lateinit var databaseDao: ItemDatabaseDao
//    private lateinit var repository: ItemRepository
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
//        val invenViewModel =
//            ViewModelProvider(this).get(InventoryViewModel::class.java)
        _binding = FragmentInventoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        setContentView(R.layout.fragment_inventory)

        // back
        backButton = root.findViewById(R.id.back_button)
        backButton.setOnClickListener(){

        }

        // get user
        val sharedPreferences = requireActivity().getSharedPreferences("user", MODE_PRIVATE)
        val uid = sharedPreferences.getString("uid", "")
//        database = FirebaseDatabase.getInstance()
//        userReference = database.getReference("Users")

        // db
//        database = InventoryDatabase.getInstance(this)
//        databaseDao = database.itemDatabaseDao
//        repository = ItemRepository(databaseDao)
        viewModelFactory = InventoryViewModel.InventoryViewModelFactory(uid)
        invenViewModel = ViewModelProvider(this, viewModelFactory).get(InventoryViewModel::class.java)

        // set up gridview + adapter
        itemGrid = root.findViewById(R.id.inventoryItemsGrid)
        arrayList = ArrayList()
        gridAdapt = InventoryGridAdapter(arrayList, invenViewModel, requireActivity())
        itemGrid.adapter = gridAdapt
        invenViewModel.bgs.observe(viewLifecycleOwner){

            if (uid != null) {
                val toShow = ArrayList<Item>()
                // find if the user owns a background
                userReference.child(uid).get().addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        val user = snapshot.getValue(User::class.java)
                        if (user != null) {
                            val ownedBgSet = user.backgroundsOwned.toSet()
                            for (item in it) {
                                if (ownedBgSet.contains(item.name)) {
                                    toShow.add(item)
                                }
                            }
                        }

                        // Update the adapter with the filtered list
                        gridAdapt.replace(toShow)
                        gridAdapt.notifyDataSetChanged()
                    }
                }
            }

            gridAdapt.replace(it)
            gridAdapt.notifyDataSetChanged()
        }
        return root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}