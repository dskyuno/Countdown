package com.project.timemanagerment.feature.home.data.repository


import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.SignInDate
import kotlinx.coroutines.flow.Flow
import java.util.*

interface SignInDateRepository {

    suspend fun insert(signInDate: SignInDate): Long

    suspend fun delete(vararg signInDate: SignInDate)

    suspend fun update(vararg signInDate: SignInDate)

    fun getSignInDateByPrimaryKey(id: Long): Flow<SignInDate>

    suspend fun getSignInDateByPrimaryKeyNotFlow(signInDateId: Long): SignInDate?

    fun getSignInDatesByParentWorkId(parentWorkId: Long): Flow<MutableList<SignInDate>>

    fun getSignInDatesByParentWorkIdOrderByDateTimeAsc(parentWorkId: Long): Flow<MutableList<SignInDate>>

    fun getSignInDatesByParentWorkIdOrderByDateTimeAsc(parentWorkId: Long,endTime: Date?): Flow<MutableList<SignInDate>>

    suspend fun getMaxOrderIndexInSignInDate(): Long?

    suspend fun updateRecentSignInDateIntroduction(signInWorkId: Long, introduction: String)
    suspend fun getSignInDateByParentAndDateTime(parentWorkId: Long?, dateTime: Date): SignInDate?
    fun getDatesSizeByParentWorkId(parentWorkId: Long): Flow<Int>

    fun getDatesSizeByParentWorkId(parentWorkId: Long, endTime: Date?): Flow<Int>
    suspend fun getSignInDatesByParentWorkIdNotFlow(parentWorkId: Long): MutableList<SignInDate>
    suspend fun getMaxOrderIndexInSignInDateBySignInWorkId(parentWorkId: Long?): Long?

}