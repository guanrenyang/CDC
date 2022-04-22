package com.example.cdc.data

import com.example.cdc.data.model.LoggedInUser
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            //普通用户
            if(username == "common_user" && password == "123456"){
            val type: Int = 0
            // TODO: handle loggedInUser authentication
            //java.util.UUID.randomUUID()会生成一个随机主键，它保证对在同一时空中的所有机器都是唯一的，"Jane Doe"为此处返回的用户名，这里没有错误情况
            val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), username, type)
            //可以依靠这种throw的方法将错误情况扔出去，然后通过catch的方式输出Result.Error

                return Result.Success(fakeUser)
            }
            //管理员
            if(username == "administrator" && password == "123456"){
                val type: Int = 1
                // TODO: handle loggedInUser authentication
                //java.util.UUID.randomUUID()会生成一个随机主键，它保证对在同一时空中的所有机器都是唯一的，"Jane Doe"为此处返回的用户名，这里没有错误情况
                val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), username, type)
                //可以依靠这种throw的方法将错误情况扔出去，然后通过catch的方式输出Result.Error

                return Result.Success(fakeUser)
            }
            else{
                throw Exception("account-password pair error")
            }


        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}