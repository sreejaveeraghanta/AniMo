package AniMo.com.database.inventoryStore


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

//A Repository manages queries and allows you to use multiple backends.
// In the most common example, the Repository implements the logic for
// deciding whether to fetch data from a network or use results cached in a local database.
class ItemRepository(private val itemDatabaseDao: ItemDatabaseDao) {

    val allItems: Flow<List<Item>> = itemDatabaseDao.getAllItems()

    fun insert(item: Item){
        CoroutineScope(IO).launch{
            itemDatabaseDao.insertItem(item)
        }
    }

    fun delete(id: Long){
        CoroutineScope(IO).launch {
            itemDatabaseDao.deleteItem(id)
        }
    }

    fun deleteAll(){
        CoroutineScope(IO).launch {
            itemDatabaseDao.deleteAll()
        }
    }
}