package com.project.timemanagerment.feature.home.data.repository


import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.SignInWork
import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.SignInWorkWithDates
import kotlinx.coroutines.flow.Flow

interface SignInWorkRepository {

    suspend fun insert(signInWork: SignInWork)

    suspend fun delete(vararg signInWork: SignInWork)

    suspend fun update(vararg signInWork: SignInWork)

    fun getSignInWorkByPrimaryKey(id: Long): Flow<SignInWork?>

    suspend fun getSignInWorkByPrimaryKeyNotFlow(id: Long): SignInWork?

    fun getSignInWorksByOrderIndexDesc(): Flow<MutableList<SignInWork>>

    suspend fun getMaxOrderIndexInSignWork(): Long?

    fun getSignInWorksWithSignInDateByOrderIndexDesc(): Flow<MutableList<SignInWorkWithDates>>
    suspend fun getSignInWorksWithSignInDateByOrderIndexDescNotFlow(): MutableList<SignInWorkWithDates>

    suspend fun getSignWorkCount(): Long
}