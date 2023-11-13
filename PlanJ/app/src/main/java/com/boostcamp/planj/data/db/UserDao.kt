package com.boostcamp.planj.data.db

import androidx.room.Dao
import androidx.room.Insert
import com.boostcamp.planj.data.model.User

@Dao
interface UserDao {


    @Insert()
    fun insertDB(user : User)
}