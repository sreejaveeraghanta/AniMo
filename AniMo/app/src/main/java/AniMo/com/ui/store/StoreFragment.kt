package AniMo.com.ui.store

import AniMo.com.R
import AniMo.com.database.Item
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
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class StoreFragment : Fragment() {

    private var _binding: FragmentStoreBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // buttons
    private lateinit var invenButton: Button

    // grid views
    private lateinit var gridAdaptMusic: GridAdapter
    private lateinit var gridAdaptBG: GridAdapter
    private lateinit var gridViewMusic: GridView
    private lateinit var gridViewBG: GridView


    private lateinit var storeViewModel: StoreViewModel


//    private lateinit var database: StoreDatabase
//    private lateinit var databaseDao: ItemDatabaseDao
//    private lateinit var repository: ItemRepository
//    private lateinit var viewModelFactory: StoreViewModelFactory
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
        invenButton.setOnClickListener() {
            // open inventory page
            val intent = Intent(activity, InventoryActivity::class.java)
            startActivity(intent)
//            val transact = requireActivity().supportFragmentManager.beginTransaction()
//            transact.replace(R.id.storepage, InventoryActivity())
//            transact.commit()
        }

        // get user
        val sharedPreferences = requireActivity().getSharedPreferences("user", MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "")


        // item list by name: Winter Gift, Underwater Flowing, Cold Aurora Nights, Beach Views, Rock On, Vinyl House, Tokyo Cafe, Flower Blossom's Orchestral, Backyard Lofi, Country Menu
        //get data and


        //
//        database = StoreDatabase.getInstance(requireActivity())
//        databaseDao = database.itemDatabaseDao
//        repository = ItemRepository(databaseDao)
//        viewModelFactory = StoreViewModelFactory(repository)
        storeViewModel = ViewModelProvider(this).get(StoreViewModel::class.java)


        // SET BG ITEMS
        gridViewBG = root.findViewById(R.id.backgroundStore)
        arrayList = ArrayList()
        gridAdaptBG = GridAdapter(arrayList, storeViewModel, requireActivity())
        gridViewBG.adapter = gridAdaptBG
        storeViewModel.getBackgroundsData()
        if (username != null) {
            //compare user owned items to store items

        } else {
            storeViewModel.bgs.observe(viewLifecycleOwner) {
                gridAdaptBG.replace(it)
                gridAdaptBG.notifyDataSetChanged()
            }
        }
//        storeViewModel.allItemsLiveData.observe(requireActivity(), Observer { it ->
//            var array: ArrayList<Item> = ArrayList()
//            for (item in it) {
//                if (item.type == "BACKGROUND") {
//                    array.add(item)
//                }
//            }
//            gridAdaptBG.replace(array)
//            gridAdaptBG.notifyDataSetChanged()
//        })

        // SET MUSIC ITEMS
        gridViewMusic = root.findViewById(R.id.musicStore)
        gridAdaptMusic = GridAdapter(arrayList, storeViewModel, requireActivity())
        gridViewMusic.adapter = gridAdaptMusic
        storeViewModel.getMusicData()
        if (username != null) {
            //compare user owned items to store items

        } else {
            storeViewModel.music.observe(requireActivity(), Observer { it ->
                gridAdaptMusic.replace(it)
                gridAdaptMusic.notifyDataSetChanged()
            })
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}