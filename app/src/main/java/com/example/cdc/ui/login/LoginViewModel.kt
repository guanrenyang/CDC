package com.example.cdc.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.example.cdc.data.LoginRepository
import com.example.cdc.data.Result

import com.example.cdc.R

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String, result: String) {
        // can be launched in a separate asynchronous job
        val resultTotal = loginRepository.login(username, password, result)
        //通过loginRepository来获取登录校验结果
        if (resultTotal is Result.Success) {
            //这里是将LoggedInUserView中的displayName赋值为想要的string
            //例如成功时应当将success设置为非null的对应值
            _loginResult.value =
                LoginResult(success = LoggedInUserView(displayName = resultTotal.data.displayName, type = resultTotal.data.type))
        } else {
            if(result[0] == '0'){
                _loginResult.value = LoginResult(error = R.string.login_failed)
            }
            else{
                _loginResult.value = LoginResult(error = R.string.account_failed)
            }

        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}