package AniMo.com.ui.friends

import AniMo.com.Util
import AniMo.com.database.User
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import AniMo.com.databinding.FragmentFriendsBinding
import android.content.Intent
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FriendsFragment : Fragment() {

    private var _binding: FragmentFriendsBinding? = null
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var reference: DatabaseReference = database.reference.child("Users")

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
        val list: ListView = binding.friendsList

        val cur_usr = auth.currentUser
        println(cur_usr!!.uid)
        friendsViewModel.getFriends(cur_usr!!.uid)
        friendsViewModel.friends.observe(viewLifecycleOwner) { friends ->
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, friends)
            list.adapter =adapter
            list.setOnItemClickListener(){ parent : AdapterView<*>, _: View, pos: Int, _: Long ->
                val uid = friendsViewModel.friend_uids[pos]
                reference.child(uid).get().addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        val user = snapshot.getValue(User::class.java)
                        val intent = Intent(requireContext(), displayStatsActivity::class.java)
                        intent.putExtra("name", user!!.name)
                        intent.putExtra("tasks", user.tasksCompleted)
                        intent.putExtra("friends", user.friends)
                        intent.putExtra("visited", user.visited)
                        intent.putExtra("visitors", user.visitors)
                        intent.putExtra("timeSpent", user.timeSpent)
                        intent.putExtra("uid", uid)
                        startActivity(intent)
                    }
                }
                Toast.makeText(requireContext(), "Viewing ${parent.getItemAtPosition(pos)}'s stats!", Toast.LENGTH_SHORT).show()
            }
        }
        val textView: TextView = binding.textFriends
        friendsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val imageButton: ImageButton = binding.addFriendsButton
        imageButton.setOnClickListener() {
            val intent = Intent(requireContext(), AddFriendActivity::class.java)
            startActivity(intent)
            Toast.makeText(requireContext(), "Adding New Friend!", Toast.LENGTH_SHORT).show()
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}