package AniMo.com.ui.inventory

import AniMo.com.R
import AniMo.com.databinding.FragmentInventoryBinding
import AniMo.com.databinding.FragmentStoreBinding
import AniMo.com.ui.store.StoreViewModel
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val invenViewModel =
            ViewModelProvider(this).get(InventoryViewModel::class.java)
        _binding = FragmentInventoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        itemGrid = root.findViewById(R.id.inventoryItemsGrid)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}