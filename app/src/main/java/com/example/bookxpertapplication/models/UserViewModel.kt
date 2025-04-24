package com.example.bookxpertapplication.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookxpertapplication.repositories.UserRepository
import com.example.bookxpertapplication.roomdatabase.AppDatabase
import com.example.bookxpertapplication.roomdatabase.UserEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao = AppDatabase.getDatabase(application).userDao()
    private val repository = UserRepository(userDao)

    private val _user = MutableStateFlow<UserEntity?>(null)
    val user: StateFlow<UserEntity?> = _user

    init {
        getUser()
    }

    fun saveUser(user: UserEntity) {
        viewModelScope.launch {
            repository.saveUser(user)
        }
    }

    fun getUser(/*onResult: (UserEntity?) -> Unit*/) {
        viewModelScope.launch {
            _user.value = repository.getUser()
//            onResult(user)
        }
    }
}