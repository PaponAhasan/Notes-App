package com.example.notesappadvance.viewmodel

import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesappadvance.models.login.LoginRequest
import com.example.notesappadvance.models.login.LoginResponse
import com.example.notesappadvance.models.registation.RegistationRequest
import com.example.notesappadvance.models.registation.RegistationResponse
import com.example.notesappadvance.repository.UserRepository
import com.example.notesappadvance.utils.ExtraFunction
import com.example.notesappadvance.utils.Helper
import com.example.notesappadvance.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val application: Application
) : ViewModel() {

    private val _loginState =
        MutableStateFlow<NetworkResult<LoginResponse>>(NetworkResult.Empty())
    val loginState: StateFlow<NetworkResult<LoginResponse>>
        get() = _loginState

    private val _signUpState =
        MutableStateFlow<NetworkResult<RegistationResponse>>(NetworkResult.Empty())
    val signUpState: StateFlow<NetworkResult<RegistationResponse>>
        get() = _signUpState

    fun requestLogin(loginRequest: LoginRequest) {
        try {
            if (ExtraFunction.hasInternetConnection(application)) {
                viewModelScope.launch {
                    _loginState.value = NetworkResult.Loading()
                    val response = userRepository.remote.requestLogin(loginRequest)
                    if (response.isSuccessful && response.body() != null) {
                        //save token
                        val accessToken = response.body()!!.access_token
                        //Timber.d("Token: $accessToken")
                        userRepository.dataStore.setToken(accessToken)

                        _loginState.value = NetworkResult.Success(response.body()!!)
                    } else if (response.errorBody() != null) {
                        Timber.d(response.message())
                        val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                        _loginState.value = NetworkResult.Error(errorObj.getString("message"))
                    } else {
                        _loginState.value = NetworkResult.Error("Something went wrong")
                    }
                }
            } else {
                _loginState.value = NetworkResult.Error("No Internet Connection")
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _loginState.value = NetworkResult.Error("Network Failure")

                is NullPointerException -> _loginState.value =
                    NetworkResult.Error("Null Pointer Exception")

                is SocketTimeoutException -> _loginState.value =
                    NetworkResult.Error("Server is under maintenance")

                else -> _loginState.value = NetworkResult.Error("Conversion Error")
            }
        }
    }

    fun requestSignup(registationRequest: RegistationRequest) {
        try {
            if (ExtraFunction.hasInternetConnection(application)) {
                viewModelScope.launch {
                    _signUpState.value = NetworkResult.Loading()
                    val response = userRepository.remote.requestSignup(registationRequest)
                    if (response.isSuccessful && response.body() != null) {
                        _signUpState.value = NetworkResult.Success(response.body()!!)
                    } else if (response.errorBody() != null) {
                        val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                        _signUpState.value = NetworkResult.Error(errorObj.getString("error"))
                    } else {
                        _signUpState.value = NetworkResult.Error("Something went wrong")
                    }
                }
            } else {
                _signUpState.value = NetworkResult.Error("No Internet Connection")
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _signUpState.value = NetworkResult.Error("Network Failure")

                is NullPointerException -> _signUpState.value =
                    NetworkResult.Error("Null Pointer Exception")

                is SocketTimeoutException -> _signUpState.value =
                    NetworkResult.Error("Server is under maintenance")

                else -> _signUpState.value = NetworkResult.Error("Conversion Error")
            }
        }
    }

    fun validateCredentials(
        emailAddress: String, userName: String, password: String,
        isLogin: Boolean
    ): Pair<Boolean, String> {

        var result = Pair(true, "")
        if (TextUtils.isEmpty(emailAddress) || (!isLogin && TextUtils.isEmpty(userName)) || TextUtils.isEmpty(
                password
            )
        ) {
            result = Pair(false, "Please provide the credentials")
        } else if (!Helper.isValidEmail(emailAddress)) {
            result = Pair(false, "Email is invalid")
        } else if (!TextUtils.isEmpty(password) && password.length <= 5) {
            result = Pair(false, "Password length should be greater than 5")
        }
        return result
    }
}