package AniMo.com.ui.inventory

import AniMo.com.database.Item
import AniMo.com.database.User
import AniMo.com.ui.home.HomeViewModel
import androidx.lifecycle.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.IllegalArgumentException
import java.security.PrivilegedAction


class InventoryViewModel(private val uid: String?, private val homeViewModel: HomeViewModel?) : ViewModel() {
    private var database = FirebaseDatabase.getInstance()
    private var bgReference = database.getReference("Backgrounds")
    private var userReference = database.getReference("Users")


    private val _bgLiveData = MutableLiveData<List<Item>>().apply {
        MutableLiveData<List<Item>>()
    }
    var userbgs:  LiveData<List<Item>> = _bgLiveData

    private val _isEquippedLiveData = MutableLiveData<Boolean>().apply {
        MutableLiveData<Boolean>()
    }
    val isEquippedLiveData: LiveData<Boolean> = _isEquippedLiveData


    fun getUserItems() {
        val allitemslist = mutableListOf<Item>() // MutableList

        // get list of items
        bgReference.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                for (bgdata in snapshot.children) {
                    val bg = bgdata.getValue(Item::class.java)
                    if (bg != null) {
                        allitemslist.add(bg) // Add song to the mutable list
                    }
                }
            }
        }

        if (uid != null) {
            val useritems = mutableListOf<Item>() // MutableList
            userReference.child(uid).get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        // check if user already owns any music
                        if (snapshot.hasChild("backgroundsOwned")) {
                            val itemsOwned = user.backgroundsOwned
                            for( item in allitemslist){
                                if (itemsOwned.contains(item.name)) {
                                    useritems.add(item)
                                }

                            }
                        }
                    }
                    _bgLiveData.postValue(useritems) // Update LiveData with the new list
                }
            }
        }

    }

    fun checkIfItemEquipped(item: Item){
        if (uid != null) {
            userReference.child(uid).get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        if (snapshot.hasChild("backgroundEquipped")) {
                            val itemEquipped = user.backgroundEquipped
                            if (item.name == itemEquipped)
                                _isEquippedLiveData.value = true
                            }
                        } else {
                            val name = listOf(item.name)
                            val newfield = mapOf("backgroundEquipped" to name)
                            userReference.child(uid).updateChildren(newfield)
                            _isEquippedLiveData.value = true
                        }
                }
            }
        }

    }

    fun equipItem(item: Item){
        if (uid != null) {
            var itemToUnequip = ""
            userReference.child(uid).get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        if (snapshot.hasChild("backgroundEquipped")) {
                            itemToUnequip = user.backgroundEquipped
                            val name = item.name
                            val newfield = mapOf("backgroundEquipped" to name)
                            userReference.child(uid).updateChildren(newfield)
                            _isEquippedLiveData.value = true
                        }
                    }
                }
            }
            homeViewModel?.getEquippedBackground()

        }

    }

    class InventoryViewModelFactory (private val uid: String?, private val homeViewModel: HomeViewModel?) : ViewModelProvider.Factory {
        override fun<T: ViewModel> create(modelClass: Class<T>) : T{ //create() creates a new instance of the modelClass, which is CommentViewModel in this case.
            if(modelClass.isAssignableFrom(InventoryViewModel::class.java))
                return InventoryViewModel(uid, homeViewModel) as T
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}





