package AniMo.com.ui.friends

import AniMo.com.database.User
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FriendsViewModel : ViewModel() {
    private lateinit var auth: FirebaseAuth
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var reference: DatabaseReference = database.reference.child("Users")

    private val _text = MutableLiveData<String>().apply {
        value = "Friends"
    }
    val text: LiveData<String> = _text

    private val _friends = MutableLiveData<List<String>>().apply {
        value = mutableListOf()
    }
    val friends: LiveData<List<String>> = _friends

    var friend_uids: List<String> = listOf()
    fun getFriends(uid: String) {
        reference.child(uid).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    val list: MutableList<String> = mutableListOf()
                    for (friend in user.friendList) {
                        reference.child(friend).get().addOnSuccessListener { friend_snapshot ->
                            if (friend_snapshot.exists()) {
                                val data = friend_snapshot.getValue(User::class.java)
                                list.add(data!!.name)
                            }
                            _friends.postValue(list)
                        }
                    }
                    friend_uids = user.friendList
                }
            }
        }
    }
}