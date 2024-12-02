package AniMo.com.ui

import AniMo.com.R

class FindIcon {

    private val MUSICiconNames = arrayOf("iconrockstar", "iconcafe", "iconmeditation", "iconbackyard",
        "iconguitar", "iconcherryblossom", "iconvinyl")
    private val MUSICdrawables = arrayOf(R.drawable.iconrockstar, R.drawable.iconcafe,
        R.drawable.iconmeditation, R.drawable.backyardicon, R.drawable.iconguitar, R.drawable.iconcherryblossom,
        R.drawable.iconvinyl)

    private val BGiconNames = arrayOf("iconaurora", "iconigloo", "iconaurora2", "iconbeach", "iconwave")
    private val BGdrawables = arrayOf(R.drawable.iconaurora, R.drawable.iconigloo, R.drawable.iconaurora2,
        R.drawable.iconbeach, R.drawable.iconwave)


    fun search(str: String) : Int {
        var ind = 0
        // check if its a music item
        for (item in MUSICiconNames.indices) {
            if (MUSICiconNames[item] == str) {
                ind = item
                return MUSICdrawables[ind]
            }
        }

        //check if its a bg item
        for (item in BGiconNames.indices) {
            if (BGiconNames[item] == str) {
                ind = item
                return BGdrawables[ind]
            }
        }

        // if neither, return default image
        return R.drawable.icondefault
    }

    fun findCategory(iconName: String) : String {
        var type = ""

        // check if its a music item
        for (item in MUSICiconNames.indices) {
            if (MUSICiconNames[item] == iconName) {
                type = "MUSIC"
                return type
            }
        }

        //check if its a bg item
        for (item in BGiconNames.indices) {
            if (BGiconNames[item] == iconName) {
                type = "BG"
                return type

            }
        }

        return type
    }

}