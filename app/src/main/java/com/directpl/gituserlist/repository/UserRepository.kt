package com.directpl.gituserlist.repository

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import com.directpl.gituserlist.api.UserService
import com.directpl.gituserlist.db.Users
import com.directpl.gituserlist.db.dao.UsersDao
import com.directpl.gituserlist.model.Data
import com.directpl.gituserlist.model.NoData
import com.directpl.gituserlist.model.Results
import com.directpl.gituserlist.util.SessionManager

import javax.inject.Inject

class UserRepository
@Inject constructor(
    private val usersDao: UsersDao,
    private val apiService: UserService
) {

    suspend fun getUsers() : Data<List<Results>> {
        try {
            val response = apiService.getUsersFromApi()
            if (response.isSuccessful) {
                response.body()?.let {
                    clearData()
                    var counter = 1
                    it.forEach { result ->
                        usersDao.save(result.toUsers())
                    }
                    return Data.success(it)
                } ?: throw NoData()
            } else {
                return Data.error(response.errorBody().toString(), null)
            }
        } catch (e: Exception) {
            return Data.error(e.message.toString(), null)
        }
    }


    suspend fun getUserDetail(username: String) : Data<Results> {
        try {
            val response = apiService.getUserDetailFromApi(username)
            if (response.isSuccessful) {
                response.body()?.let {

                    return Data.success(it)
                } ?: throw NoData()
            } else {
                return Data.error(response.errorBody().toString(), null)
            }
        } catch (e: Exception) {
            return Data.error(e.message.toString(), null)
        }
    }

    fun getSaveUsers(): LiveData<MutableList<Users>> = usersDao.getSavedUsersFromDao()

    fun clearData(){
        usersDao.clearAll()
    }


}