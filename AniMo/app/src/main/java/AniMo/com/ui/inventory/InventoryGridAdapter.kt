package AniMo.com.ui.inventory

import AniMo.com.R
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class InventoryGridAdapter (var imglist:Array<Int>, var namelist: Array<String>, var activity: Activity) : BaseAdapter() {

    override fun getItem(p0: Int): Any {
        return imglist[p0]
    }

    override fun getCount(): Int {
        return imglist.size
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val v: View = View.inflate(activity, R.layout.inventory_item, null)
        val gridpic = v.findViewById<ImageView>(R.id.itemImageView)
        val name = v.findViewById<TextView>(R.id.itemNameTextView)
        val equip = v.findViewById<Button>(R.id.itemEquipButton)

        val img = imglist[p0]
        gridpic.setImageResource(img)
        val item = namelist[p0]
        name.text = item
        equip.setOnClickListener(){
            // open home page and use?
        }
        return v
    }
}
