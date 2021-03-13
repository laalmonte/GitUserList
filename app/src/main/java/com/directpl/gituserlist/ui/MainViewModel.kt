package com.directpl.gituserlist.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.directpl.gituserlist.db.Users
import com.directpl.gituserlist.model.Data
import com.directpl.gituserlist.repository.UserRepository
import kotlinx.coroutines.launch
import com.directpl.gituserlist.model.Results

class MainViewModel
@ViewModelInject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _data = MutableLiveData<Data<List<Results>>>()

    val data: LiveData<Data<List<Results>>>
        get() = _data

    private val _data2 = MutableLiveData<Data<Results>>()

    val data2: LiveData<Data<Results>>
        get() = _data2

    init {

    }

    fun getUsers() =
        viewModelScope.launch {
            _data.postValue(Data.loading(null))
            _data.postValue(userRepository.getUsers())
        }

    fun getUserDetail(username: String) =
        viewModelScope.launch {
            _data2.postValue(Data.loading(null))
            _data2.postValue(userRepository.getUserDetail(username))
        }

    fun getUsersFromDB() : LiveData<MutableList<Users>> = userRepository.getSaveUsers()

    fun clearData() { userRepository.clearData() }

}