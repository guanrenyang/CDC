package com.example.cdc.data

import android.util.Log
import com.example.cdc.data.model.LoggedInUser
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit


/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String, result:String): Result<LoggedInUser> {
        try {
//            //普通用户
//            if(username == "common_user" && password == "123456"){
//            val type: Int = 0
//            // TODO: handle loggedInUser authentication
//            //java.util.UUID.randomUUID()会生成一个随机主键，它保证对在同一时空中的所有机器都是唯一的，"Jane Doe"为此处返回的用户名，这里没有错误情况
//            val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), username, type)
//            //可以依靠这种throw的方法将错误情况扔出去，然后通过catch的方式输出Result.Error
//
//                return Result.Success(fakeUser)
//            }
//            //管理员
//            if(username == "administrator" && password == "123456"){
//                val type: Int = 1
//                // TODO: handle loggedInUser authentication
//                //java.util.UUID.randomUUID()会生成一个随机主键，它保证对在同一时空中的所有机器都是唯一的，"Jane Doe"为此处返回的用户名，这里没有错误情况
//                val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), username, type)
//                //可以依靠这种throw的方法将错误情况扔出去，然后通过catch的方式输出Result.Error
//
//                return Result.Success(fakeUser)
//            }
            //登录成功
            if(result[0] == '1'){
                val type: Int = if(result[1] == '1'){
                    //管理员
                    1
                } else{
                    //普通用户
                    0
                }
                val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), username, type)
                return Result.Success(fakeUser)
            }
            //登录失败
            if(result[0] == '0'){
                val type: Int = 0
                val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), username, type)
                throw Exception("account-password pair error")
            }
            else{
                throw Exception("account not exists")
            }
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}