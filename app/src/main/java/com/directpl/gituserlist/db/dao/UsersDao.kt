package com.directpl.gituserlist.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.directpl.gituserlist.db.Users

@Dao
interface UsersDao {

    @Query("SELECT * FROM Users")
    fun getSavedUsersFromDao(): LiveData<MutableList<Users>>

    @Query("SELECT * FROM Users WHERE Users.id = :idParam")
    fun getNote(idParam: Int): LiveData<Users>

    @Query("SELECT note FROM Users WHERE Users.id = :idParam")
    fun getNote2(idParam: Int): String

    @Insert
    fun save(user: Users)

    @Query("DELETE FROM Users WHERE id = :idParam")
    fun deleteUser(idParam: Int)

    @Query("DELETE FROM Users")
    fun clearAll()
}