package AniMo.com.database.inventoryStore

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Item::class], version = 1)
abstract class InventoryDatabase : RoomDatabase() { //XD: Room automatically generates implementations of your abstract CommentDatabase class.
    abstract val itemDatabaseDao: ItemDatabaseDao

    companion object{
        //The Volatile keyword guarantees visibility of changes to the INSTANCE variable across threads
        @Volatile
        private var INSTANCE: InventoryDatabase? = null

        fun getInstance(context: Context) : InventoryDatabase{
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(context.applicationContext,
                        InventoryDatabase::class.java, "item_table").build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}