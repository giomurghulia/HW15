package com.example.hw15.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hw15.Resource
import com.example.hw15.RetrofitClient
import com.example.hw15.User
import com.example.hw15.login.LogInModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val _registerState = MutableSharedFlow<Resource<Register>>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val registerState get() = _registerState.asSharedFlow()

    fun register(user: User) {
        viewModelScope.launch {
            val model = RegisterModel(user.email, user.pass)

            val response = RetrofitClient.authService.register(model)

            if (response.isSuccessful) {
                val body = response.body()
                _registerState.tryEmit(Resource.Success(body!!))
            } else {
                val errorBody = response.errorBody()
                _registerState.tryEmit(Resource.Error(errorBody?.toString()!!))
            }
        }
    }
}