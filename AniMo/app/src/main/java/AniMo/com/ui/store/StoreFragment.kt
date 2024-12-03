package AniMo.com.ui.store

import AniMo.com.MainViewModel
import AniMo.com.R
import AniMo.com.database.Item
import AniMo.com.database.User
import AniMo.com.databinding.FragmentStoreBinding
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
    private lateinit var arrayList: List<Item>
    private lateinit var database: FirebaseDatabase
    private lateinit var userReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentStoreBinding.inflate(inflater, container, false)
        val root: View = binding.root


        // button to go to inventory
        invenButton = root.findViewById(R.id.inven_button)
        invenButton.setOnClickListener() {
            // open inventory page
            findNavController().navigate(R.id.action_storeFragment_to_inventoryFragment)
        }

        // get user
        val sharedPreferences = requireActivity().getSharedPreferences("user", MODE_PRIVATE)
        val uid = sharedPreferences.getString("uid", "")
        database = FirebaseDatabase.getInstance()
        userReference = database.getReference("Users")

        // item list by name: Winter Gift, Underwater Flowing, Cold Aurora Nights, Beach Views, Rock On, Vinyl House, Tokyo Cafe, Flower Blossom's Orchestral, Backyard Lofi, Country Menu
        //get data and


        storeViewModel = ViewModelProvider(this).get(StoreViewModel::class.java)


        // SET BG ITEMS
        gridViewBG = root.findViewById(R.id.backgroundStore)
        arrayList = emptyList()
        gridAdaptBG = GridAdapter(arrayList, storeViewModel, uid, requireActivity())
        gridViewBG.adapter = gridAdaptBG
            //compare user owned items to store items

        storeViewModel.bgs.observe(viewLifecycleOwner) {

            if (uid != null) {
                val toRemove = it.toMutableList() // Create a mutable copy of the list
                // If the user already owns a background, don't display it in the store
                userReference.child(uid).get().addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        val user = snapshot.getValue(User::class.java)
                        if (user != null) {
                            val ownedBgSet = user.backgroundsOwned.toSet()
                            for (item in it) {
                                if (ownedBgSet.contains(item.name)) {
                                    toRemove.remove(item)
                                }
                            }
                        }

                        // Update the adapter with the filtered list
                        gridAdaptBG.replace(toRemove)
                        gridAdaptBG.notifyDataSetChanged()
                    }
                }
            }
            gridAdaptBG.replace(it)
            gridAdaptBG.notifyDataSetChanged()
        }
        storeViewModel.getBackgroundsData()

        // SET MUSIC ITEMS
        gridViewMusic = root.findViewById(R.id.musicStore)
        gridAdaptMusic = GridAdapter(arrayList, storeViewModel, uid, requireActivity())
        gridViewMusic.adapter = gridAdaptMusic
        storeViewModel.music.observe(viewLifecycleOwner) { it ->

            if (uid != null) {
                val toRemove = it.toMutableList() // Create a mutable copy of the list
                // If the user already owns a song, don't display it in the store
                userReference.child(uid).get().addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        val user = snapshot.getValue(User::class.java)
                        if (user != null) {
                            val ownedMusicSet = user.musicOwned.toSet()
                            for (item in it) {
                                if (ownedMusicSet.contains(item.name)) {
                                    toRemove.remove(item)
                                }
                            }
                        }

                        // Update the adapter with the filtered list
                        gridAdaptMusic.replace(toRemove)
                        gridAdaptMusic.notifyDataSetChanged()
                    }
                }
            } else {
                gridAdaptMusic.replace(it)
                gridAdaptMusic.notifyDataSetChanged()
            }
        }

        storeViewModel.getMusicData()



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}