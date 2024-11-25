package AniMo.com.ui.store

import AniMo.com.R
import AniMo.com.database.inventoryStore.Item
import android.app.Activity
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat

class GridAdapter (var itemList: List<Item>, var viewmodel: StoreViewModel, var activity: Activity) : BaseAdapter() {

    override fun getItem(p0: Int): Any {
        return itemList[p0]
    }

    override fun getCount(): Int {
        return itemList.size
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val v: View = View.inflate(activity, R.layout.store_item, null)
        val gridpic = v.findViewById<ImageView>(R.id.itemImageView)
        val name = v.findViewById<TextView>(R.id.itemNameTextView)
        val buy = v.findViewById<Button>(R.id.itemBuyButton)


        val itm = itemList[p0]
        var img = 0
        if (itm.image == "igloo"){
            img = R.drawable.igloo
        } else if (itm.image == "rockstar"){
            img = R.drawable.rockstar
        } else if (itm.image == "guitar"){
            img = R.drawable.guitar
        } else if (itm.image == "vinyl"){
            img = R.drawable.vinyl
        } else if (itm.image == "cherryblossom"){
            img = R.drawable.cherryblossom
        }
//            ResourcesCompat.getDrawable(tst, null)
//        getIdentifier(itm.image, "drawable", "AniMo.com.ui.store")
        gridpic.setImageResource(img)
        name.text = itm.name
        val txt = itm.price.toString() + " \u2764"
        buy.text = txt
        buy.setOnClickListener(){
            // CHECK IF USER HAS ENOUGH HEARTS
            viewmodel.deleteItem(p0)
        }

        return v
    }

    fun replace(newList: List<Item>){
        itemList = newList
    }
}
