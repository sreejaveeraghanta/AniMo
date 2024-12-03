package AniMo.com

import AniMo.com.database.Item
import AniMo.com.database.User
import AniMo.com.ui.inventory.InventoryViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.IllegalArgumentException

class MainViewModel(private val uid: String) : ViewModel() {

    private var database = FirebaseDatabase.getInstance()
    private var userReference = database.getReference("Users").child(uid)

    private val _userLiveData = MutableLiveData<Int>().apply {
        MutableLiveData<Int>()
    }
    var userhearts: LiveData<Int> = _userLiveData

    fun updateHearts() {

        userReference.addValueEventListener (object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var hrt = 0
                if (snapshot.exists()) {
                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                            hrt = user.hearts // Add song to the mutable list
                        }
                        _userLiveData.postValue(hrt) // Update LiveData with the new list
                        println("CHECK")
                        println("CHECK  " + _userLiveData)
                    }

                }

            override fun onCancelled(error: DatabaseError) {
                println("error: ${error.message}")
            }
        })
    }

    class MainViewModelFactory (private val uid: String?) : ViewModelProvider.Factory {
        override fun<T: ViewModel> create(modelClass: Class<T>) : T{ //create() creates a new instance of the modelClass, which is CommentViewModel in this case.
            if(modelClass.isAssignableFrom(MainViewModel::class.java))
                return MainViewModel(uid ?: throw IllegalArgumentException("UID cannot be null")) as T
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
