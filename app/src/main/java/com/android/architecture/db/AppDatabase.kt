package com.android.architecture.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.architecture.travel.datasource.OrderLocalDataSource.OrderDao
import com.android.architecture.travel.model.OrderEntity

@Database(entities = [OrderEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private const val DB_NAME = "app-database"
        @Volatile private var instance: AppDatabase? = null

        fun instance(context: Context): AppDatabase {
            val checkInstance = instance
            if (checkInstance != null) {
                return checkInstance
            }
            return synchronized(this) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        DB_NAME
                    ).build()
                }
                instance!!
            }
        }
    }

    abstract fun orderDao(): OrderDao
}
