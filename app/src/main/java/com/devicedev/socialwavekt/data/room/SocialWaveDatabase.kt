package com.devicedev.socialwavekt.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.devicedev.socialwavekt.data.room.dao.UserDao
import com.devicedev.socialwavekt.data.room.entities.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class SocialWaveDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao


    companion object {

        const val DB_NAME = "social_wave.db"

        @Volatile
        private var INSTANCE: SocialWaveDatabase? = null


        fun getDatabase(context: Context, scope: CoroutineScope): SocialWaveDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context, scope).also {
                    INSTANCE = it
                }
            }

        private fun buildDatabase(context: Context, scope: CoroutineScope): SocialWaveDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                SocialWaveDatabase::class.java,
                DB_NAME
            ).addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    INSTANCE?.let { database ->
                        scope.launch(Dispatchers.IO) {
//                        database.noteDao().insert(Note("Welcome!", "This is a dummy introductory note"))

                        }

                    }
                }
            }).build()
    }

}