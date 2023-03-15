package cn.cellapp.kotlinmodelsingle.feature.home.data.database.coverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ConvertersLongList {
    @TypeConverter
    fun longListToString(longList: List<Long>): String {
        return Gson().toJson(longList)
    }
    @TypeConverter
    fun stringToLongList(longListString: String): List<Long> {
        val longList = object : TypeToken<List<Long>>() {}.type
        return Gson().fromJson<List<Long>>(longListString, longList)
    }
}