package AniMo.com.ui.store

import AniMo.com.R
import AniMo.com.database.inventoryStore.Item
import AniMo.com.database.inventoryStore.ItemDatabaseDao
import AniMo.com.database.inventoryStore.ItemRepository
import AniMo.com.database.inventoryStore.StoreDatabase
import AniMo.com.databinding.FragmentStoreBinding
import AniMo.com.ui.inventory.InventoryActivity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider


class StoreFragment : Fragment() {

    private var _binding: FragmentStoreBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var gridAdaptMusic: GridAdapter
    private lateinit var gridAdaptBG: GridAdapter
    private lateinit var gridViewMusic: GridView
    private lateinit var gridViewBG: GridView
    private lateinit var picSet1: Array<Int>
    private lateinit var nameSet1: Array<String>
    private lateinit var priceSet1: Array<String>
    private lateinit var picSet2: Array<Int>
    private lateinit var nameSet2: Array<String>
    private lateinit var priceSet2: Array<String>

    private lateinit var invenButton: Button
    private lateinit var database: StoreDatabase
    private lateinit var databaseDao: ItemDatabaseDao
    private lateinit var repository: ItemRepository
    private lateinit var viewModelFactory: StoreViewModelFactory
    private lateinit var storeViewModel: StoreViewModel
    private lateinit var arrayList: ArrayList<Item>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val storeViewModel =
//            ViewModelProvider(this).get(StoreViewModel::class.java)

        _binding = FragmentStoreBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textStore
//        storeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        // button to go to inventory
        invenButton = root.findViewById(R.id.inven_button)
        invenButton.setOnClickListener(){
            // open inventory page
            val intent = Intent(activity, InventoryActivity::class.java)
            startActivity(intent)
//            val transact = requireActivity().supportFragmentManager.beginTransaction()
//            transact.replace(R.id.storepage, InventoryActivity())
//            transact.commit()
        }

        //
        database = StoreDatabase.getInstance(requireActivity())
        databaseDao = database.itemDatabaseDao
        repository = ItemRepository(databaseDao)
        viewModelFactory = StoreViewModelFactory(repository)
        storeViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(StoreViewModel::class.java)




        // SET BG ITEMS
        gridViewBG = root.findViewById(R.id.backgroundStore)
        arrayList = ArrayList()
        gridAdaptBG = GridAdapter(arrayList, storeViewModel, requireActivity())
        gridViewBG.adapter = gridAdaptBG
        storeViewModel.allItemsLiveData.observe(requireActivity(), Observer { it ->
            var array: ArrayList<Item> = ArrayList()
            for (item in it) {
                if (item.type == "BACKGROUND") {
                    array.add(item)
                }
            }
            gridAdaptBG.replace(array)
            gridAdaptBG.notifyDataSetChanged()
        })

        // SET MUSIC ITEMS
        gridViewMusic = root.findViewById(R.id.musicStore)
        gridAdaptMusic = GridAdapter(arrayList, storeViewModel, requireActivity())
        gridViewMusic.adapter = gridAdaptMusic
        storeViewModel.allItemsLiveData.observe(requireActivity(), Observer { it ->
            var array: ArrayList<Item> = ArrayList()
            for (item in it) {
                if (item.type == "MUSIC") {
                    array.add(item)
                }
            }
            gridAdaptMusic.replace(array)
            gridAdaptMusic.notifyDataSetChanged()
        })



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}