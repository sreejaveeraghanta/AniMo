package AniMo.com.ui.store

import AniMo.com.R
import AniMo.com.database.Item
import AniMo.com.database.User
import androidx.lifecycle.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.IllegalArgumentException


class StoreViewModel() : ViewModel() {
    private var database = FirebaseDatabase.getInstance()
    private var musicReference = database.getReference("Music")
    private var bgReference = database.getReference("Backgrounds")

    private val _musicLiveData = MutableLiveData<List<Item>>().apply {
        MutableLiveData<List<Item>>()
    }
    val music:  LiveData<List<Item>> = _musicLiveData

    private val _bgLiveData = MutableLiveData<List<Item>>().apply {
        MutableLiveData<List<Item>>()
    }
    val bgs:  LiveData<List<Item>> = _bgLiveData



//    private val _bgLiveData = MutableLiveData<List<Item>>().apply {
//        value = Item()
//    }
//    val bgs:  LiveData<List<Item>> = _bgLiveData

//    val backgroundItems = arrayOf(R.drawable.igloo, R.drawable.autumn, R.drawable.castle,
//        R.drawable.flowers)
//
//    val bgNames = arrayOf("Winter", "Autumn", "Haunted", "Spring")
//
//    val bgPrices = arrayOf("200 \u2764", "200 \u2764", "200 \u2764", "200 \u2764", "200 \u2764")
//
//    val musicItems = arrayOf(R.drawable.rock,
//        R.drawable.guitar)
//
//    val musicNames = arrayOf("Rock On!", "Country Sweets")
//
//    val musicPrices = arrayOf("200 \u2764", "200 \u2764", "200 \u2764", "200 \u2764")

    fun getMusicData() {
        musicReference.addValueEventListener(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val list: List<Item> = emptyList()
                    for (musicdata in snapshot.children) {
                        val song = musicdata.getValue(Item::class.java)
                        if (song != null) {
                            list.plus(song)
                            _musicLiveData.postValue(list)
                            println("CHECK")
                            println("CHECK  " + _musicLiveData)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("error: ${error.message}")
            }
        })
    }

    fun getBackgroundsData() {
        bgReference.addValueEventListener(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val list: List<Item> = emptyList()
                    for (bgdata in snapshot.children) {
                        val bg = bgdata.getValue(Item::class.java)
                        if (bg != null) {
                            list.plus(bg)
                            _bgLiveData.postValue(list)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("error: ${error.message}")
            }
        })
    }

//class StoreViewModelFactory (private val repository: ItemRepository) : ViewModelProvider.Factory {
//    override fun<T: ViewModel> create(modelClass: Class<T>) : T{ //create() creates a new instance of the modelClass, which is CommentViewModel in this case.
//        if(modelClass.isAssignableFrom(StoreViewModel::class.java))
//            return StoreViewModel(repository) as T
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
}




