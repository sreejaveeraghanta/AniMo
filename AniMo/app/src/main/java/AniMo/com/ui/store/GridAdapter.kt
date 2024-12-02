package AniMo.com.ui.store

import AniMo.com.R
import AniMo.com.database.Item
import AniMo.com.ui.FindIcon
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class GridAdapter (var itemList: List<Item>, var viewmodel: StoreViewModel, val uid: String?, var activity: Activity) : BaseAdapter() {

    private var finder: FindIcon = FindIcon()

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
        println("ITM: " + itm)
        val img = finder.search(itm.icon)
//        if (itm.icon == "igloo"){
//            img = R.drawable.igloo
//        } else if (itm.icon == "rockstaricon"){
//            img = R.drawable.rockstaricon
//        } else if (itm.icon == "guitar"){
//            img = R.drawable.guitar
//        } else if (itm.icon == "vinyl"){
//            img = R.drawable.vinyl
//        } else if (itm.icon == "cherryblossom"){
//            img = R.drawable.cherryblossom
//        }
//            ResourcesCompat.getDrawable(tst, null)
//        getIdentifier(itm.image, "drawable", "AniMo.com.ui.store")
        gridpic.setImageResource(img)
        name.text = itm.name

        val txt = itm.price.toString() + " \u2764"
        buy.text = txt
        buy.setOnClickListener(){
            if (uid != null) {
                // CHECK IF USER HAS ENOUGH HEARTS
                viewmodel.buyItem(itm, uid)
            }

        }

        return v
    }


    fun replace(newList: List<Item>){
        itemList = newList
    }
}
