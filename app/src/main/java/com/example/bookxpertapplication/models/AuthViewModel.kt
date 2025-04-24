package com.example.bookxpertapplication.models

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() {

    private val _currentUser = MutableStateFlow<FirebaseUser?>(FirebaseAuth.getInstance().currentUser)
    val currentUser = _currentUser.asStateFlow()

    fun updateUser(user: FirebaseUser?) {
        Log.d("pp..", "inside updateUser")
        _currentUser.value = user
    }
}

