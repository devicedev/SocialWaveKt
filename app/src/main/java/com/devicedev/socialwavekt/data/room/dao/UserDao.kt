package com.devicedev.socialwavekt.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.devicedev.socialwavekt.data.room.entities.User

@Dao
interface UserDao {

    @Query("SELECT * FROM users ORDER BY id DESC LIMIT 1")
    fun get(): LiveData<User>

    @Insert
    suspend fun insert(userEntity: User)

    @Update
    suspend fun update(userEntity: User)

    @Query("DELETE FROM users")
    suspend fun delete()


}