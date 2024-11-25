package AniMo.com.database.inventoryStore

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Item::class], version = 3, exportSchema = true)
abstract class StoreDatabase : RoomDatabase() { //XD: Room automatically generates implementations of your abstract CommentDatabase class.
    abstract val itemDatabaseDao: ItemDatabaseDao

    companion object{
        //The Volatile keyword guarantees visibility of changes to the INSTANCE variable across threads
        @Volatile
        private var INSTANCE: StoreDatabase? = null

        fun getInstance(context: Context) : StoreDatabase{
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(context.applicationContext,
                        StoreDatabase::class.java, "item_table").createFromAsset("database/itemdb.db").fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}