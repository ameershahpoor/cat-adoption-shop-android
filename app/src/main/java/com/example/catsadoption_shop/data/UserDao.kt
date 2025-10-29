package com.example.catsadoption_shop.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface userDao{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun register(user: User)

    @Query("SELECT * FROM users WHERE username= :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?
}
