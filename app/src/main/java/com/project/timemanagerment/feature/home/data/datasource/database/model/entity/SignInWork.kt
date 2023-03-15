package com.project.timemanagerment.feature.home.data.datasource.database.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.project.timemanagerment.base.datasource.convert.ConvertersDate
import java.util.*

@Entity(tableName = "sign_in_work")
@TypeConverters(ConvertersDate::class)
data class SignInWork(
    val name: String,
    val startTime: Date,
    val startTimeString: String,
    val iconIndex: Int,
    var orderIndex: Long,
    val introduction: String?,
    var sinInWorkType: Int,
    val endTimeActive: Boolean,
    val endTime: Date?,
    var isShowRemind: Boolean,
    var isEditDisableOutsideRange: Boolean,
    var finalSignInDateTime: Date?,
    var finalSignInDateId: Long?,
    val createTime: Date?,
    val updateTime: Date?

) {
    @PrimaryKey(autoGenerate = true)
    var signInWorkId = 0L

    companion object {
        const val freeNum = 5

        const val signInWorkTypeDefault = 1
        const val signInWorkTypeUrgency = 2

        const val endTimeStatusInActive = 1
        const val endTimeStatusActive = 2


        fun updateSignInWork(
            name: String,
            startTime: Date,
            startTimeString: String,
            iconIndex: Int,
            introduction: String?,
            sinInWorkType: Int,
            endTimeActive: Boolean,
            endTime: Date?,
            isShowRemind: Boolean, oldSignInWork: SignInWork
        ): SignInWork {
            val work = SignInWork(
                name,
                startTime,
                startTimeString,
                iconIndex,
                oldSignInWork.orderIndex,
                introduction,
                sinInWorkType,
                endTimeActive,
                endTime,
                isShowRemind,
                oldSignInWork.isEditDisableOutsideRange,
                oldSignInWork.finalSignInDateTime,
                oldSignInWork.finalSignInDateId,
                oldSignInWork.createTime,
                Date()
            )
            work.signInWorkId = oldSignInWork.signInWorkId
            return work
        }


        fun createSignInWork(
            name: String,
            startTime: Date,
            startTimeString: String,
            iconIndex: Int,
            introduction: String?,
            sinInWorkType: Int,
            endTimeActive: Boolean,
            endTime: Date?,
            isShowRemind: Boolean
        ): SignInWork {
            return SignInWork(
                name,
                startTime,
                startTimeString,
                iconIndex,
                0,
                introduction,
                sinInWorkType,
                endTimeActive,
                endTime,
                isShowRemind,
                true,
                null,
                null,
                Date(),
                Date()
            )
        }
    }

}