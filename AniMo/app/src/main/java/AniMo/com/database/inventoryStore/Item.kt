package AniMo.com.database.inventoryStore


import android.graphics.drawable.Drawable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item_table")
data class Item (
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "itemName_column")
    var name: String = "",

    @ColumnInfo(name = "price_column")
    var price: Int = 0,

    @ColumnInfo(name = "image_column")
    var img: String = ""

)