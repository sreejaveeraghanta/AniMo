package AniMo.com.ui.store

import AniMo.com.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import AniMo.com.databinding.FragmentStoreBinding
import android.widget.Button
import android.widget.GridView

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val storeViewModel =
            ViewModelProvider(this).get(StoreViewModel::class.java)

        _binding = FragmentStoreBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textStore
//        storeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }


        println("str")
        // SET BG ITEMS
        picSet1 = storeViewModel.backgroundItems
        nameSet1 = storeViewModel.bgNames
        priceSet1 = storeViewModel.bgPrices

        gridViewBG = root.findViewById(R.id.backgroundStore)

        gridAdaptBG = GridAdapter(picSet1, nameSet1, priceSet1, requireActivity())
        gridViewBG.adapter = gridAdaptBG
        println("bg")

        // SET MUSIC ITEMS
        picSet1 = storeViewModel.musicItems
        nameSet1 = storeViewModel.musicNames
        priceSet1 = storeViewModel.musicPrices

        gridViewMusic = root.findViewById(R.id.musicStore)

        gridAdaptMusic = GridAdapter(picSet2, nameSet2, priceSet2, requireActivity())
        gridViewMusic.adapter = gridAdaptMusic

        println("ms")



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}