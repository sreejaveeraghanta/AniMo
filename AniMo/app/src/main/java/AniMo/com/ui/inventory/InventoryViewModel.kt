package AniMo.com.ui.inventory

import AniMo.com.database.inventoryStore.Item
import AniMo.com.database.inventoryStore.ItemRepository
import androidx.lifecycle.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.IllegalArgumentException


class InventoryViewModel(private val uid: String?) : ViewModel() {
    private var database = FirebaseDatabase.getInstance()
    private var bgReference = database.getReference("Backgrounds")

    private val _bgLiveData = MutableLiveData<List<AniMo.com.database.Item>>().apply {
        MutableLiveData<List<AniMo.com.database.Item>>()
    }
    val bgs:  LiveData<List<AniMo.com.database.Item>> = _bgLiveData

    fun getBackgroundsData() {
        if (bgs.value.isNullOrEmpty() ) {
            println("TRUE")

            bgReference.get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val list = mutableListOf<AniMo.com.database.Item>() // MutableList for correct handling
                    for (bgdata in snapshot.children) {
                        val bg = bgdata.getValue(AniMo.com.database.Item::class.java)
                        if (bg != null) {
                            list.add(bg) // Add song to the mutable list
                        }
                    }
                    _bgLiveData.postValue(list) // Update LiveData with the new list
                    println("CHECK")
                    println("CHECK  " + _bgLiveData)
                }
            }
        }

        bgReference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val list = mutableListOf<AniMo.com.database.Item>() // MutableList for correct handling
                    for (bgdata in snapshot.children) {
                        val bg = bgdata.getValue(AniMo.com.database.Item::class.java)
                        if (bg != null) {
                            list.add(bg) // Add song to the mutable list
                        }
                    }
                    _bgLiveData.postValue(list) // Update LiveData with the new list
                    println("CHECK")
                    println("CHECK  " + _bgLiveData)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("error: ${error.message}")
            }
        })
    }

    class InventoryViewModelFactory (private val uid: String?) : ViewModelProvider.Factory {
        override fun<T: ViewModel> create(modelClass: Class<T>) : T{ //create() creates a new instance of the modelClass, which is CommentViewModel in this case.
            if(modelClass.isAssignableFrom(InventoryViewModel::class.java))
                return InventoryViewModel(uid) as T
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}





