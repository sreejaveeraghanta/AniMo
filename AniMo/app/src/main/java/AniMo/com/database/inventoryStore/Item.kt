package AniMo.com.database.inventoryStore


import android.graphics.drawable.Drawable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item_table")
data class Item (
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "price")
    var price: Int = 0,

    @ColumnInfo(name = "image")
    var image: String = "",

    @ColumnInfo(name = "resource")
    var resource: String = "",

    @ColumnInfo(name = "type")
    var type: String = "",

    @ColumnInfo(name = "equipped")
    var equipped: Int = 0

)