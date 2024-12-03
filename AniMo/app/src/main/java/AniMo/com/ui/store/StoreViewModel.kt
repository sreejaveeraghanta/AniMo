package AniMo.com.ui.store

import AniMo.com.R
import AniMo.com.database.Item
import AniMo.com.database.User
import AniMo.com.ui.FindIcon
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

    private var userReference = database.getReference("Users")

    private val _musicLiveData = MutableLiveData<List<Item>>().apply {
        MutableLiveData<List<Item>>()
    }
    var music: LiveData<List<Item>> = _musicLiveData

    private val _bgLiveData = MutableLiveData<List<Item>>().apply {
        MutableLiveData<List<Item>>()
    }
    var bgs: LiveData<List<Item>> = _bgLiveData


    fun getMusicData() {
        println("MUSIC: " + music)

        if (music.value.isNullOrEmpty()) {
            println("TRUE")

            musicReference.get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val list = mutableListOf<Item>() // MutableList
                    for (musicdata in snapshot.children) {
                        val song = musicdata.getValue(Item::class.java)
                        if (song != null) {
                            list.add(song) // Add song to the mutable list
                        }
                    }
                    _musicLiveData.postValue(list) // Update LiveData with the new list
                    println("CHECK")
                    println("CHECK  " + _musicLiveData)
                }
            }
        }

        musicReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val list = mutableListOf<Item>() // MutableList
                    for (musicdata in snapshot.children) {
                        val song = musicdata.getValue(Item::class.java)
                        if (song != null) {
                            list.add(song) // Add song to the mutable list
                        }
                    }
                    _musicLiveData.postValue(list) // Update LiveData with the new list
                    println("CHECK")
                    println("CHECK  " + _musicLiveData)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("error: ${error.message}")
            }
        })
    }

    fun getBackgroundsData() {
        println("BGS: " + bgs)


        if (bgs.value.isNullOrEmpty()) {
            println("TRUE")

            bgReference.get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val list = mutableListOf<Item>() // MutableList
                    for (bgdata in snapshot.children) {
                        val bg = bgdata.getValue(Item::class.java)
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

        bgReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val list = mutableListOf<Item>() // MutableList
                    for (bgdata in snapshot.children) {
                        val bg = bgdata.getValue(Item::class.java)
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

    fun buyItem(itm: Item, uid: String) {

        // find out if item is bg or music
        val iconName = itm.icon
        val finder = FindIcon()
        val type = finder.findCategory(iconName)
        // add name of item to user owned list
        if (type == "MUSIC") {

            userReference.child(uid).get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        // check if user already owns any music
                        if (snapshot.hasChild("musicOwned")) {
                            val itemsOwned = user.musicOwned.toMutableList()
                            itemsOwned.add(itm.name)
                            // Update the user data in Firebase
                            userReference.child(uid).child("musicOwned").setValue(itemsOwned)

                        } else { // if not, start list (create field)

                            val item = listOf(itm.name)
                            val newfield = mapOf("musicOwned" to item)
                            userReference.child(uid).updateChildren(newfield)

                        }
                    }
                }
            }

        } else if (type == "BG") {
            userReference.child(uid).get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        // check if user already owns any music
                        if (snapshot.hasChild("backgroundsOwned")) {
                            val itemsOwned = user.backgroundsOwned.toMutableList()
                            itemsOwned.add(itm.name)
                            // Update the user data in Firebase
                            userReference.child(uid).child("backgroundsOwned").setValue(itemsOwned)

                        } else { // if not, start list (create field)

                            val item = listOf(itm.name)
                            val newfield = mapOf("backgroundsOwned" to item)
                            userReference.child(uid).updateChildren(newfield)

                        }
                    }
                }
            }
        }


    }
}




