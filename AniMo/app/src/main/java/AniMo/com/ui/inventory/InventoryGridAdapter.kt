package AniMo.com.ui.inventory

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

class InventoryGridAdapter (var itemList: List<Item>, var viewmodel: InventoryViewModel, var activity: Activity) : BaseAdapter() {

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
        val buy = v.findViewById<Button>(R.id.itemEquipButton)


        val itm = itemList[p0]
        val img = finder.search(itm.icon)

        gridpic.setImageResource(img)
        name.text = itm.name
        var txt = ""
        var equip = false
//        if (itm.equipped == 1){
//            txt = "Equip"
//        } else {
//            txt = "Unequip"
//            equip = true
//        }
        buy.text = txt
        buy.setOnClickListener(){
            // CHECK IF IN USE
            if (equip){
                val t = "Unequip"
                buy.text = t
                // unequip
            } else {
                val t = "Equip"
                buy.text = t
                // equip
            }
        }

        return v
    }

    fun replace(newList: List<Item>){
        itemList = newList
    }
}