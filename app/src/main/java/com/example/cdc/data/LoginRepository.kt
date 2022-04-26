package com.example.cdc.data

import com.example.cdc.data.model.LoggedInUser

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object（代表着用户的名字）
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    fun login(username: String, password: String, result: String): Result<LoggedInUser> {
        // handle login
        //调用dataSource类中的login函数来获取结果
        val result = dataSource.login(username, password, result)
        //如果成功，就将LoginRepository中的user设置为该用户的用户名
        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }
        //此处没有错误返回处理，直接返回结果即可
        return result
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}