package AniMo.com.ui.stats

import AniMo.com.database.User
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class StatsViewModel : ViewModel() {
    private var database = FirebaseDatabase.getInstance()
    private var reference = database.getReference("Users")

    private val _text = MutableLiveData<String>().apply {
        value = "Statistics"
    }
    val text: LiveData<String> = _text

    private val _user = MutableLiveData<User>().apply {
        value = User()
    }
    val user:  LiveData<User> = _user

    fun getUserData(username: String, name: String) {
        reference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userdata in snapshot.children) {
                        val user = userdata.getValue(User::class.java)
                        if (user != null) {
                            _user.postValue(user)
                            _text.postValue("${name}'s Pet Statistics")
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("error: ${error.message}")
            }
        })
    }
}