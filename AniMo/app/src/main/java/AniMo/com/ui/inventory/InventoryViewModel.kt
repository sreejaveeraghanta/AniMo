package AniMo.com.ui.inventory

import AniMo.com.database.inventoryStore.Item
import AniMo.com.database.inventoryStore.ItemRepository
import androidx.lifecycle.*
import java.lang.IllegalArgumentException


class InventoryViewModel(private val repository: ItemRepository) : ViewModel() {
    val allItemsLiveData: LiveData<List<Item>> = repository.allItems.asLiveData()

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

class InventoryViewModelFactory (private val repository: ItemRepository) : ViewModelProvider.Factory {
    override fun<T: ViewModel> create(modelClass: Class<T>) : T{ //create() creates a new instance of the modelClass, which is CommentViewModel in this case.
        if(modelClass.isAssignableFrom(InventoryViewModel::class.java))
            return InventoryViewModel(repository) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}




