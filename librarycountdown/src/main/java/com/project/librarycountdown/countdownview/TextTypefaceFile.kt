package com.project.librarycountdown.countdownview

import com.project.librarycountdown.R


internal enum class TextTypefaceList(val typefaceIndex: Int, val fontId: Int) {
    typeface_1(1, R.font.a_default_1),
    typeface_2(2, R.font.aclonica_2),
    typeface_3(3, R.font.leckerli_one_3),
    typeface_4(4, R.font.akaya_telivigala_4),
    typeface_5(5, R.font.amaranth_5),
    typeface_6(6, R.font.akronim_6),
    typeface_7(7, R.font.alfa_slab_one_7),
    typeface_8(8, R.font.almendra_display_8),
    typeface_9(9, R.font.architects_daughter_9),
    typeface_10(10, R.font.audiowide_10),
    typeface_11(11, R.font.geostar_11),
    typeface_12(12, R.font.codystar_12);

    companion object {
        fun getTypefaceByIndex(index: Int): TextTypeface? {
            for (element in values()) {
                if (element.typefaceIndex == index) {
                    return TextTypeface(element.typefaceIndex, element.fontId)
                }
            }
            return null
        }

        fun getAllTextTypeface(): MutableList<TextTypeface> {
            val typefaceList = mutableListOf<TextTypeface>()
            for (element in values()) {
                val face = TextTypeface(element.typefaceIndex, element.fontId)
                typefaceList.add(face)
            }
            return typefaceList
        }
    }

}

internal data class TextTypeface(val typefaceIndex: Int, val fontId: Int)