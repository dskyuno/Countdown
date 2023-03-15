package com.project.timemanagerment.feature.home.data.datasource.database.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.project.timemanagerment.base.datasource.convert.ConvertersDate
import java.util.*

@Entity(tableName = "sign_in_date")
@TypeConverters(ConvertersDate::class)
data class SignInDate(
    var parentWorkId: Long,
    val dateTime: Date,
    val dateTimeString: String,
    var introduction: String?,
    var orderIndex: Long,
    val createTime: Date
) {
    @PrimaryKey(autoGenerate = true)
    var signInDateId = 0L

    companion object{
     /*   fun  createSignInDate(parentWorkId: Long,dateTime: Date,dateTimeString: String):SignInDate{


            return SignInDate()
        }*/

    }
}