package com.project.timemanagerment.feature.home.data.source

import com.project.timemanagerment.feature.home.data.datasource.database.dao.SignInWorkDao
import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.SignInWork
import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.SignInWorkWithDates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignInWorkDataSource @Inject constructor(private val signInWorkDao: SignInWorkDao) {
    suspend fun insert(signInWork: SignInWork) {
        withContext(Dispatchers.IO) {
            signInWorkDao.insert(signInWork)
        }

    }

    suspend fun delete(vararg signInWork: SignInWork) {
        withContext(Dispatchers.IO) {
            signInWorkDao.delete(*signInWork)
        }
    }

    suspend fun update(vararg signInWork: SignInWork) {
        withContext(Dispatchers.IO) {
            signInWorkDao.update(*signInWork)
        }

    }

    fun getSignInWorkByPrimaryKey(id: Long): Flow<SignInWork?> {
        return signInWorkDao.getSignInWorkByPrimaryKey(id)
    }

    suspend fun getSignInWorkByPrimaryKeyNotFlow(id: Long): SignInWork? =
        withContext(Dispatchers.IO) {
            signInWorkDao.getSignInWorkByPrimaryKeyNotFlow(id)
        }

    fun getSignInWorksByOrderIndexDesc(): Flow<MutableList<SignInWork>> {
        return signInWorkDao.getSignInWorksByOrderIndexDesc()
    }

    suspend fun getMaxOrderIndexInSignWork(): Long? = withContext(Dispatchers.IO) {
        signInWorkDao.getMaxOrderIndexInSignWork()
    }

    fun getSignInWorksWithSignInDateByOrderIndexDesc(): Flow<MutableList<SignInWorkWithDates>> {
        return signInWorkDao.getSignInWorksWithSignInDateByOrderIndexDesc()
    }

    suspend fun getSignInWorksWithSignInDateByOrderIndexDescNotFlow(): MutableList<SignInWorkWithDates> =
        withContext(Dispatchers.IO) {
            signInWorkDao.getSignInWorksWithSignInDateByOrderIndexDescNotFlow()
        }

    suspend fun getSignWorkCount(): Long = withContext(Dispatchers.IO) {
        signInWorkDao.getSignWorkCount()
    }
}