package com.example.hw15.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hw15.Resource
import com.example.hw15.RetrofitClient
import com.example.hw15.User
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class LogInViewModel : ViewModel() {

    private val _logInState = MutableSharedFlow<Resource<LogIn>>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val logInState get() = _logInState.asSharedFlow()

    fun logIn(user: User) {
        viewModelScope.launch {
            logInResponse(user.email, user.pass).collect() {
                _logInState.tryEmit(it)
            }
        }
    }

    private fun logInResponse(email: String, password: String) = flow {
        val model = LogInModel(email, password)

        val response = RetrofitClient.authService.logIn(model)

        if (response.isSuccessful) {
            val body = response.body()
            emit(Resource.Success(body!!))
        } else {
            val errorBody = response.errorBody()
            emit(Resource.Error(errorBody.toString()))
        }
    }


}