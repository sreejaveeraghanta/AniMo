package AniMo.com.ui.friends

import AniMo.com.Util
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import AniMo.com.databinding.FragmentFriendsBinding
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast

class FriendsFragment : Fragment() {

    private var _binding: FragmentFriendsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val friendsViewModel =
            ViewModelProvider(this).get(FriendsViewModel::class.java)

        _binding = FragmentFriendsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val friends = listOf("John Smith", "Bob the builder", "Jane Doe", "Billy", "Emma", "Annabelle")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, friends)
        val list: ListView = binding.friendsList
        list.adapter =adapter
        list.setOnItemClickListener(){ parent : AdapterView<*>, _: View, pos: Int, _: Long ->
            Toast.makeText(requireContext(), "${parent.getItemAtPosition(pos)} clicked!", Toast.LENGTH_SHORT).show()

        }

        val textView: TextView = binding.textFriends
        friendsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val imageButton: ImageButton = binding.addFriendsButton
        imageButton.setOnClickListener() {
            Toast.makeText(requireContext(), "Added New Friend!", Toast.LENGTH_SHORT).show()
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}