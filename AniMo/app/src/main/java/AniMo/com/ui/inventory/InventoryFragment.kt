package AniMo.com.ui.inventory

import AniMo.com.R
import AniMo.com.database.inventoryStore.InventoryDatabase
import AniMo.com.database.inventoryStore.ItemDatabaseDao
import AniMo.com.database.inventoryStore.ItemRepository
import AniMo.com.database.inventoryStore.StoreDatabase
import AniMo.com.databinding.FragmentInventoryBinding
import AniMo.com.databinding.FragmentStoreBinding
import AniMo.com.ui.store.GridAdapter
import AniMo.com.ui.store.StoreViewModel
import AniMo.com.ui.store.StoreViewModelFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class InventoryFragment : Fragment() {
    private var _binding: FragmentInventoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var itemGrid: GridView
    private lateinit var gridAdapt: InventoryGridAdapter
    private lateinit var database: InventoryDatabase
    private lateinit var databaseDao: ItemDatabaseDao
    private lateinit var repository: ItemRepository
    private lateinit var viewModelFactory: InventoryViewModelFactory
    private lateinit var invenViewModel: InventoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val invenViewModel =
//            ViewModelProvider(this).get(InventoryViewModel::class.java)
        _binding = FragmentInventoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // set up gridview + adapter
        itemGrid = root.findViewById(R.id.inventoryItemsGrid)
//        gridAdapt = InventoryGridAdapter(, , requireActivity())
        itemGrid.adapter = gridAdapt

        //
        database = InventoryDatabase.getInstance(requireActivity())
        databaseDao = database.itemDatabaseDao
        repository = ItemRepository(databaseDao)
        viewModelFactory = InventoryViewModelFactory(repository)
        invenViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(InventoryViewModel::class.java)


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}