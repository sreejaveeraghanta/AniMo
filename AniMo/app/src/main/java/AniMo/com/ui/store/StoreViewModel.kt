package AniMo.com.ui.store

import AniMo.com.R
import AniMo.com.database.inventoryStore.Item
import AniMo.com.database.inventoryStore.ItemRepository
import androidx.lifecycle.*
import java.lang.IllegalArgumentException


class StoreViewModel(private val repository: ItemRepository) : ViewModel() {
    val allItemsLiveData: LiveData<List<Item>> = repository.allItems.asLiveData()
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

    fun insert(item: Item) {
        repository.insert(item)
    }

    fun getItem(pos: Int){
        val itemList = allItemsLiveData.value
        if (!itemList.isNullOrEmpty()){
            val id = itemList[pos].id
            repository.getItem(id)
        }
    }

    fun deleteItem(pos: Int){
        val itemList = allItemsLiveData.value
        if (!itemList.isNullOrEmpty()){
            val id = itemList[pos].id
            repository.delete(id)
        }
    }

    fun deleteAll(){
        val itemList = allItemsLiveData.value
        if (!itemList.isNullOrEmpty())
            repository.deleteAll()
    }
}

class StoreViewModelFactory (private val repository: ItemRepository) : ViewModelProvider.Factory {
    override fun<T: ViewModel> create(modelClass: Class<T>) : T{ //create() creates a new instance of the modelClass, which is CommentViewModel in this case.
        if(modelClass.isAssignableFrom(StoreViewModel::class.java))
            return StoreViewModel(repository) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}




