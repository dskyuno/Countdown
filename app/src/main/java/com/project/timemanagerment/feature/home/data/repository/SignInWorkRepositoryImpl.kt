package com.project.timemanagerment.feature.home.data.repository

import com.project.timemanagerment.feature.home.data.datasource.database.dao.SignInWorkDao
import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.SignInWork
import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.SignInWorkWithDates
import com.project.timemanagerment.feature.home.data.source.SignInWorkDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignInWorkRepositoryImpl @Inject constructor(private val signInWorkDatasource: SignInWorkDataSource) :
    SignInWorkRepository {
    override suspend fun insert(signInWork: SignInWork) {
        val currentMaxIndex = getMaxOrderIndexInSignWork()
        val newIndex = if (currentMaxIndex != null) currentMaxIndex + 1 else 1
        signInWork.orderIndex = newIndex
        signInWorkDatasource.insert(signInWork)
    }

    override suspend fun delete(vararg signInWork: SignInWork) {
        signInWorkDatasource.delete(*signInWork)
    }

    override suspend fun update(vararg signInWork: SignInWork) {
        signInWorkDatasource.update(*signInWork)
    }

    override fun getSignInWorkByPrimaryKey(id: Long): Flow<SignInWork?> {
        return signInWorkDatasource.getSignInWorkByPrimaryKey(id)
    }

    override suspend fun getSignInWorkByPrimaryKeyNotFlow(id: Long): SignInWork? {
      return  signInWorkDatasource.getSignInWorkByPrimaryKeyNotFlow(id)
    }

    override fun getSignInWorksByOrderIndexDesc(): Flow<MutableList<SignInWork>> {
        return signInWorkDatasource.getSignInWorksByOrderIndexDesc()
    }

    override suspend fun getMaxOrderIndexInSignWork(): Long? {
        return signInWorkDatasource.getMaxOrderIndexInSignWork()
    }

    override fun getSignInWorksWithSignInDateByOrderIndexDesc(): Flow<MutableList<SignInWorkWithDates>> {
        return signInWorkDatasource.getSignInWorksWithSignInDateByOrderIndexDesc()
    }

    override suspend fun getSignInWorksWithSignInDateByOrderIndexDescNotFlow(): MutableList<SignInWorkWithDates> {
       return signInWorkDatasource.getSignInWorksWithSignInDateByOrderIndexDescNotFlow()
    }

    override suspend fun getSignWorkCount(): Long {
        return signInWorkDatasource.getSignWorkCount()
    }
}