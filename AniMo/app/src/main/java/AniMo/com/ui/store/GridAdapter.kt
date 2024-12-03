package AniMo.com.ui.store

import AniMo.com.R
import AniMo.com.Util
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
        gridpic.setImageResource(img)
        name.text = itm.name

        val txt = itm.price.toString() + " \u2764"
        buy.text = txt
        buy.setOnClickListener(){
            if (uid != null) {
                println("not NULL")
                // CHECK IF USER HAS ENOUGH HEARTS
                Util.getHearts(uid) { hearts ->

                    if ( hearts >= itm.price) {
                        println("ENOUGH")
                        viewmodel.buyItem(itm, uid)
                        val txt = "bought"
                        buy.text = txt
                        buy.isEnabled = false

                        val cost = -itm.price

                        Util.updateStats(uid, cost, 0, 0.0, 0, 0, 0)


                    }
                }



            }

        }

        return v
    }


    fun replace(newList: List<Item>){
        itemList = newList
    }
}
