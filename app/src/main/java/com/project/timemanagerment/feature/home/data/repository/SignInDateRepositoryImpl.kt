package com.project.timemanagerment.feature.home.data.repository

import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.SignInDate
import com.project.timemanagerment.feature.home.data.source.SignInDateDataSource
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject

class SignInDateRepositoryImpl @Inject constructor(
    private val signInDateDataSource: SignInDateDataSource
) :
    SignInDateRepository {
    override suspend fun insert(signInDate: SignInDate): Long {
        val nowMaxIndex =
            if (getMaxOrderIndexInSignInDate() != null) getMaxOrderIndexInSignInDate()!! + 1 else 1
        signInDate.orderIndex = nowMaxIndex
        return signInDateDataSource.insert(signInDate)
    }

    override suspend fun delete(vararg signInDate: SignInDate) {
        signInDateDataSource.delete(*signInDate)
    }

    override suspend fun update(vararg signInDate: SignInDate) {
        signInDateDataSource.update(*signInDate)
    }

    override fun getSignInDateByPrimaryKey(id: Long): Flow<SignInDate> {
        return signInDateDataSource.getSignInDateByPrimaryKey(id)
    }

    override suspend fun getSignInDateByPrimaryKeyNotFlow(signInDateId: Long): SignInDate? {
        return signInDateDataSource.getSignInDateByPrimaryKeyNotFlow(signInDateId)
    }

    override fun getSignInDatesByParentWorkId(parentWorkId: Long): Flow<MutableList<SignInDate>> {
        return signInDateDataSource.getSignInDatesByParentWorkId(parentWorkId)
    }

    override fun getSignInDatesByParentWorkIdOrderByDateTimeAsc(parentWorkId: Long): Flow<MutableList<SignInDate>> {
        return signInDateDataSource.getSignInDatesByParentWorkIdOrderByDateTimeAsc(parentWorkId)
    }

    override fun getSignInDatesByParentWorkIdOrderByDateTimeAsc(
        parentWorkId: Long,
        endTime: Date?
    ): Flow<MutableList<SignInDate>> {
        return signInDateDataSource.getSignInDatesByParentWorkIdOrderByDateTimeAsc(parentWorkId, endTime)
    }

    override suspend fun getMaxOrderIndexInSignInDate(): Long? {
        return signInDateDataSource.getMaxOrderIndexInSignInDate()
    }

    override suspend fun updateRecentSignInDateIntroduction(
        signInWorkId: Long,
        introduction: String
    ) {
        val recentDate = signInDateDataSource.getRecentSinInDate(signInWorkId)
        recentDate?.let {
            it.introduction = introduction
            signInDateDataSource.update(it)
        }
    }

    override suspend fun getSignInDateByParentAndDateTime(
        parentWorkId: Long?,
        dateTime: Date
    ): SignInDate? {
        return signInDateDataSource.getSignInDateByParentAndDateTime(parentWorkId, dateTime)
    }

    override fun getDatesSizeByParentWorkId(parentWorkId: Long): Flow<Int> {
        return signInDateDataSource.getDatesSizeByParentWorkId(parentWorkId)
    }

    override fun getDatesSizeByParentWorkId(parentWorkId: Long, endTime: Date?): Flow<Int> {
        return signInDateDataSource.getDatesSizeByParentWorkIdHasEndTime(parentWorkId, endTime)

    }

    override suspend fun getSignInDatesByParentWorkIdNotFlow(parentWorkId: Long): MutableList<SignInDate> {
        return signInDateDataSource.getSignInDatesByParentWorkIdNotFlow(parentWorkId)
    }

    override suspend fun getMaxOrderIndexInSignInDateBySignInWorkId(parentWorkId: Long?): Long? {
        return signInDateDataSource.getMaxOrderIndexInSignInDateBySignInWorkId(parentWorkId)
    }
}