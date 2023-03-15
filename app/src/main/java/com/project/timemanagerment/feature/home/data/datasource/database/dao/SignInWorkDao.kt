package com.project.timemanagerment.feature.home.data.datasource.database.dao

import androidx.room.*
import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.SignInWork
import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.SignInWorkWithDates
import kotlinx.coroutines.flow.Flow


@Dao
interface SignInWorkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(signInWork: SignInWork)

    @Delete
    suspend fun delete(vararg signInWork: SignInWork)

    @Update
    suspend fun update(vararg signInWork: SignInWork)

    @Query("select * from sign_in_work where signInWorkId =:id")
    fun getSignInWorkByPrimaryKey(id: Long): Flow<SignInWork?>

    @Query("select * from sign_in_work where signInWorkId =:id")
    suspend fun getSignInWorkByPrimaryKeyNotFlow(id: Long): SignInWork?

    @Query("select * from sign_in_work order by orderIndex desc")
    fun getSignInWorksByOrderIndexDesc(): Flow<MutableList<SignInWork>>

    @Query("select max(orderIndex) as orderIndex  from sign_in_work")
    suspend fun getMaxOrderIndexInSignWork(): Long?

    @Transaction
    @Query("select * from sign_in_work order by orderIndex desc")
    fun getSignInWorksWithSignInDateByOrderIndexDesc(): Flow<MutableList<SignInWorkWithDates>>

    @Transaction
    @Query("select * from sign_in_work order by orderIndex desc")
    suspend fun getSignInWorksWithSignInDateByOrderIndexDescNotFlow(): MutableList<SignInWorkWithDates>

    @Query("select count(signInWorkId) from sign_in_work")
    suspend fun getSignWorkCount(): Long

}