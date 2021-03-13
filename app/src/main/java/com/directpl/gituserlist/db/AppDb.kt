package com.directpl.gituserlist.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.directpl.gituserlist.db.dao.UsersDao

@Database(
    entities = [Users::class],
    version = 1,
    exportSchema = false
)
abstract class AppDb : RoomDatabase() {

    abstract fun trackDao(): UsersDao

}