package AniMo.com.ui.store

import AniMo.com.R
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StoreViewModel : ViewModel() {

    val backgroundItems = arrayOf(R.drawable.igloo, R.drawable.autumn, R.drawable.castle,
        R.drawable.flowers)

    val bgNames = arrayOf("Winter", "Autumn", "Haunted", "Spring")

    val bgPrices = arrayOf("200 \u2764", "200 \u2764", "200 \u2764", "200 \u2764", "200 \u2764")

    val musicItems = arrayOf(R.drawable.rock,
        R.drawable.guitar)

    val musicNames = arrayOf("Rock On!", "Country Sweets")

    val musicPrices = arrayOf("200 \u2764", "200 \u2764", "200 \u2764", "200 \u2764")

}