package com.project.timemanagerment.feature.home.data.datasource.database.dao

import androidx.room.*
import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.SignInDate
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface SignInDateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(signInDate: SignInDate): Long

    @Delete
    suspend fun delete(vararg signInDate: SignInDate)

    @Update
    suspend fun update(vararg signInDate: SignInDate)

    @Query("select * from sign_in_date where signInDateId = :id")
    fun getSignInDateByPrimaryKey(id: Long): Flow<SignInDate>

    @Query("select * from sign_in_date where signInDateId = :id")
    suspend fun getSignInDateByPrimaryKeyNotFlow(id: Long): SignInDate?

    @Query("select * from sign_in_date where parentWorkId =:parentWorkId")
    fun getSignInDatesByParentWorkId(parentWorkId: Long): Flow<MutableList<SignInDate>>

    @Query("select * from sign_in_date where parentWorkId =:parentWorkId order by dateTime asc")
    fun getSignInDatesByParentWorkIdOrderByDateTimeAsc(parentWorkId: Long): Flow<MutableList<SignInDate>>

    @Query("select * from sign_in_date where parentWorkId =:parentWorkId and (:endTime is null or dateTime<=:endTime) order by dateTime asc")
    fun getSignInDatesByParentWorkIdOrderByDateTimeAsc(parentWorkId: Long,endTime: Date?): Flow<MutableList<SignInDate>>

    @Query("select max(orderIndex) as orderIndex from sign_in_date")
    suspend fun getMaxOrderIndexInSignInDate(): Long?

    @Query("select * from sign_in_date where parentWorkId =:parentWorkId order by orderIndex desc limit 1")
    suspend fun getRecentSinInDate(parentWorkId: Long): SignInDate?

    @Query("select * from sign_in_date where parentWorkId=:parentWorkId and dateTime =:dateTime")
    suspend fun getSignInDateByParentAndDateTime(parentWorkId: Long?, dateTime: Date): SignInDate?

    @Query("select COUNT(signInDateId) from sign_in_date where parentWorkId=:parentWorkId")
    fun getDatesSizeByParentWorkId(parentWorkId: Long): Flow<Int>

    @Query("select COUNT(signInDateId) from sign_in_date where parentWorkId=:parentWorkId and (:endTime is null or  dateTime<= :endTime)")
    fun getDatesSizeByParentWorkIdHasEndTime(parentWorkId: Long, endTime: Date?): Flow<Int>


    @Query("select * from sign_in_date where parentWorkId =:parentWorkId")
    suspend fun getSignInDatesByParentWorkIdNotFlow(parentWorkId: Long): MutableList<SignInDate>
    @Query("select max(orderIndex) as orderIndex from sign_in_date where parentWorkId=:parentWorkId")
    suspend fun getMaxOrderIndexInSignInDateBySignInWorkId(parentWorkId: Long?): Long?


}