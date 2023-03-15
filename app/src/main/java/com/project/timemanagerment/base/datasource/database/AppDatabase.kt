package com.project.timemanagerment.base.datasource.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import cn.cellapp.kotlinmodel.feature.home.data.database.coverters.Converters
import com.project.timemanagerment.feature.home.data.datasource.database.dao.CountdownDao
import com.project.timemanagerment.feature.home.data.datasource.database.dao.SignInDateDao
import com.project.timemanagerment.feature.home.data.datasource.database.dao.SignInWorkDao
import com.project.timemanagerment.feature.home.data.datasource.database.dao.UserDao
import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.Countdown
import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.SignInDate
import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.SignInWork
import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.User


@Database(
    entities = [User::class, Countdown::class, SignInWork::class, SignInDate::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    //feature Home
    abstract fun userDao(): UserDao
    abstract fun countDownDao(): CountdownDao
    abstract fun signInWorkDao(): SignInWorkDao
    abstract fun signInDateDao(): SignInDateDao
    //featureOther

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {

            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }
        }

        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): AppDatabase {

            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            /*    super.onCreate(db)
                                val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>()
                                    .setInputData(workDataOf(KEY_FILENAME to PLANT_DATA_FILENAME))
                                    .build()
                                WorkManager.getInstance(context).enqueue(request)*/
                        }
                    }
                )
                .build()
        }
    }
}