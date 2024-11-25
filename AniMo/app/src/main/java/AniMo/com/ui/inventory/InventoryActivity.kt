package AniMo.com.ui.inventory

import AniMo.com.R
import AniMo.com.database.inventoryStore.InventoryDatabase
import AniMo.com.database.inventoryStore.Item
import AniMo.com.database.inventoryStore.ItemDatabaseDao
import AniMo.com.database.inventoryStore.ItemRepository
//import AniMo.com.databinding.FragmentInventoryBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class InventoryActivity : AppCompatActivity() {
//    private var _binding: FragmentInventoryBinding? = null
//
//    // This property is only valid between onCreateView and
//    // onDestroyView.
//    private val binding get() = _binding!!

    private lateinit var backButton: Button
    private lateinit var itemGrid: GridView
    private lateinit var gridAdapt: InventoryGridAdapter
    private lateinit var database: InventoryDatabase
    private lateinit var databaseDao: ItemDatabaseDao
    private lateinit var repository: ItemRepository
    private lateinit var viewModelFactory: InventoryViewModelFactory
    private lateinit var invenViewModel: InventoryViewModel
    private lateinit var arrayList: ArrayList<Item>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val invenViewModel =
//            ViewModelProvider(this).get(InventoryViewModel::class.java)
//        _binding = FragmentInventoryBinding.inflate(inflater, container, false)
//        val root: View = binding.root

        setContentView(R.layout.activity_inventory)

        // back
        backButton = findViewById(R.id.back_button)
        backButton.setOnClickListener(){
            finish()
        }

        // db
        database = InventoryDatabase.getInstance(this)
        databaseDao = database.itemDatabaseDao
        repository = ItemRepository(databaseDao)
        viewModelFactory = InventoryViewModelFactory(repository)
        invenViewModel = ViewModelProvider(this, viewModelFactory).get(InventoryViewModel::class.java)

        // set up gridview + adapter
        itemGrid = findViewById(R.id.inventoryItemsGrid)
        arrayList = ArrayList()
        gridAdapt = InventoryGridAdapter(arrayList, invenViewModel, this)
        itemGrid.adapter = gridAdapt
        invenViewModel.allItemsLiveData.observe(this, Observer { it ->
            var array: ArrayList<Item> = ArrayList()
            for (item in it) {
                if (item.equipped == 0) {
                    array.add(item)
                }
            }
            gridAdapt.replace(array)
            gridAdapt.notifyDataSetChanged()
        })

    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
}