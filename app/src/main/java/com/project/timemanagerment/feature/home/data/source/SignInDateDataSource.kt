package com.project.timemanagerment.feature.home.data.source

import com.project.timemanagerment.feature.home.data.datasource.database.dao.SignInDateDao
import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.SignInDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class SignInDateDataSource @Inject constructor(private val signInDateDao: SignInDateDao) {

    suspend fun insert(signInDate: SignInDate) = withContext(Dispatchers.IO) {
        signInDateDao.insert(signInDate)
    }

    suspend fun delete(vararg signInDate: SignInDate) {
        withContext(Dispatchers.IO) {
            signInDateDao.delete(*signInDate)
        }
    }

    suspend fun update(vararg signInDate: SignInDate) {
        withContext(Dispatchers.IO) {
            signInDateDao.update(*signInDate)
        }
    }

    fun getSignInDateByPrimaryKey(id: Long): Flow<SignInDate> {
        return signInDateDao.getSignInDateByPrimaryKey(id)
    }

    suspend fun getSignInDateByPrimaryKeyNotFlow(signInDateId: Long): SignInDate? =
        withContext(Dispatchers.IO) {
            signInDateDao.getSignInDateByPrimaryKeyNotFlow(signInDateId)
        }

    fun getSignInDatesByParentWorkId(parentWorkId: Long): Flow<MutableList<SignInDate>> {
        return signInDateDao.getSignInDatesByParentWorkId(parentWorkId)
    }

    fun getSignInDatesByParentWorkIdOrderByDateTimeAsc(parentWorkId: Long): Flow<MutableList<SignInDate>> {
        return signInDateDao.getSignInDatesByParentWorkIdOrderByDateTimeAsc(parentWorkId)
    }

    fun getSignInDatesByParentWorkIdOrderByDateTimeAsc(
        parentWorkId: Long,
        endTime: Date?
    ): Flow<MutableList<SignInDate>> {
        return signInDateDao.getSignInDatesByParentWorkIdOrderByDateTimeAsc(parentWorkId, endTime)
    }

    suspend fun getMaxOrderIndexInSignInDate(): Long? = withContext(Dispatchers.IO) {
        signInDateDao.getMaxOrderIndexInSignInDate()
    }

    suspend fun updateRecentSignInDateIntroduction(
        signInWorkId: Long,
        introduction: String
    ) {
        withContext(Dispatchers.IO) {
            val recentDate = signInDateDao.getRecentSinInDate(signInWorkId)
            recentDate?.let {
                it.introduction = introduction
                signInDateDao.update(it)
            }
        }
    }


    suspend fun getSignInDateByParentAndDateTime(
        parentWorkId: Long?,
        dateTime: Date
    ): SignInDate? = withContext(Dispatchers.IO) {
        signInDateDao.getSignInDateByParentAndDateTime(parentWorkId, dateTime)
    }

    fun getDatesSizeByParentWorkId(parentWorkId: Long): Flow<Int> {
        return signInDateDao.getDatesSizeByParentWorkId(parentWorkId)
    }

    fun getDatesSizeByParentWorkId(parentWorkId: Long, endTime: Date?): Flow<Int> {
        return signInDateDao.getDatesSizeByParentWorkIdHasEndTime(parentWorkId, endTime)

    }

    fun getDatesSizeByParentWorkIdHasEndTime(parentWorkId: Long, endTime: Date?): Flow<Int> {
        return signInDateDao.getDatesSizeByParentWorkIdHasEndTime(parentWorkId, endTime)
    }

    suspend fun getSignInDatesByParentWorkIdNotFlow(parentWorkId: Long): MutableList<SignInDate> =
        withContext(Dispatchers.IO) {
            signInDateDao.getSignInDatesByParentWorkIdNotFlow(parentWorkId)
        }

    suspend fun getMaxOrderIndexInSignInDateBySignInWorkId(parentWorkId: Long?): Long? =
        withContext(Dispatchers.IO) {
            signInDateDao.getMaxOrderIndexInSignInDateBySignInWorkId(parentWorkId)
        }

    suspend fun getRecentSinInDate(parentWorkId: Long): SignInDate? = withContext(Dispatchers.IO) {
        signInDateDao.getRecentSinInDate(parentWorkId)
    }
}