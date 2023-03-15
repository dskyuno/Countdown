package com.project.timemanagerment.feature.home.data.datasource.database.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.project.timemanagerment.base.datasource.convert.ConvertersDate
import java.util.*


@Entity(tableName = "countdown")
@TypeConverters(ConvertersDate::class)
data class Countdown(
    val title: String,
    val targetTime: Date?,
    val targetTimeType: Int,
    val targetTimeString: String?,
    val targetTimeChineseString: String?,
    val countdownType: Int,
    var orderIndex: Long,
    val backgroundPath: String?,
    var themeIndex: Int,
    var textFontIndex: Int,
    val createTime: Date?,
    var updateTime: Date?,
    /* optional*/
    val introduction: String?,
    val endTimeActive: Boolean,
    val endTime: Date?,
) {
    @PrimaryKey(autoGenerate = true)
    var countdownId = 0L

    companion object {
        const val  freeNum = 5

        const val timeTypeDefault = 1
        const val timeTypeChinese = 2
        const val countdownTypeGeneral = 1
        const val countdownUrgency = 2
        const val endTimeStatusInActive = 1
        const val endTimeStatusActive = 2

        fun updateCountdownByUserInput(
            title: String,
            targetTime: Date?,
            targetTimeType: Int,
            targetTimeString: String?,
            targetTimeChineseString: String?,
            countdownType: Int,
            introduction: String?,
            endTimeActive: Boolean,
            endTime: Date?, countdownOld: Countdown
        ): Countdown {
            val editCountdown = Countdown(
                title,
                targetTime,
                targetTimeType,
                targetTimeString,
                targetTimeChineseString,
                countdownType,
                countdownOld.orderIndex,
                countdownOld.backgroundPath,
                countdownOld.themeIndex,
                countdownOld.textFontIndex,
                countdownOld.createTime,
                Date(),
                introduction,
                endTimeActive,
                endTime

            )
            editCountdown.countdownId = countdownOld.countdownId
            return editCountdown
        }

        fun createCountdownByUserInput(
            title: String,
            targetTime: Date?,
            targetTimeType: Int,
            targetTimeString: String?,
            targetTimeChineseString: String?,
            countdownType: Int,
            introduction: String?,
            endTimeActive: Boolean,
            endTime: Date?

        ): Countdown {
            return Countdown(
                title,
                targetTime,
                targetTimeType,
                targetTimeString,
                targetTimeChineseString,
                countdownType,
                0,
                "",
                0,
                1,
                Date(),
                Date(),
                introduction,
                endTimeActive,
                endTime
            )
        }
    }


}




