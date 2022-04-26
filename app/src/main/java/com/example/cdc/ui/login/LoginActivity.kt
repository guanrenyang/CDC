package com.example.cdc.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.example.cdc.AdministratorActivity
import com.example.cdc.CommonUserActivity
import com.example.cdc.databinding.ActivityLoginBinding

import com.example.cdc.R
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    val BASE_URL ="http://124.71.150.114"
    val client: OkHttpClient = OkHttpClient.Builder()    //builder构造者设计模式
        .connectTimeout(10, TimeUnit.SECONDS) //连接超时时间
        .readTimeout(10, TimeUnit.SECONDS)    //读取超时
        .writeTimeout(10, TimeUnit.SECONDS)  //写超时，也就是请求超时
        .build();
    /*
        * user_login(name,password):用户和管理员通过该函数登录app
        * 若密码正确则返回1
        * 密码错误返回0
        * 用户名不存在则返回空字符串
        * 测试数据:user_login("common_user",123456)  user_login("administrator",123456)
        * */
    fun user_login(name:String,password:String){
        val request: Request = Request.Builder()
            .url("${BASE_URL}/user_login.php?name=$name&password=$password")
            .build()
        val call: Call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("OkHttp","get response onFailure :${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val body:String? = response.body?.string()
                runOnUiThread { //
                    //解析响应报文body:
                    var res = body?.split(";")
                    val res_array:Array<String> = res?.toTypedArray()!!
                }
                Log.e("OkHttp","get response successfully :${body}")
            }

        })
    }

    fun hideKeyboard(view: View){
        val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        view.requestFocus()
        inputManager.hideSoftInputFromWindow(view.windowToken,0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //登录管理部分activity

        //activity_login的四个元素
        val username = binding.username
        val password = binding.password
        val login = binding.login
        val loading = binding.loading

        binding.username.setText("common_user")
        binding.password.setText("123456")
        login.isEnabled = true

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        //这里利用了observe办法在main thread中返回了loginFormState对应的loginState，根据其判断是否能够进入下一个部分
        //这里的loginFormState用于检查username与password是否符合格式，都合适，则我们将可以按动按钮
        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            //如果两者的形式都正确，login这个button将会被设置为可以按动
            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid
            //这里如果返回的是username形式错误，则进行usernameError的提示
            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            //这里如果返回的是password形式错误，则进行passwordError的提示
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        //本质上相当于对loginResult的一个事件监听器
        //这里利用了observe办法在main thread中返回了loginResult，根据其判断是否能通过账号密码检验
        //这里的loginResult用于检查username与password是否符合验证，都合适，则我们将可以进入下一个界面
        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            //开始loading动画
            loading.visibility = View.GONE
            //如果验证失败，则调用showLoginFailed展示失败信息
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            //如果验证失败，则调用updateUiWithUser进行接下来的动作
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
            //表示该activity正常结束
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            //为防止在验证失败后自动退出，应当不会在这里调用finish()
            // finish()
        })

        //当editText的控件内text发生改变时，利用loginViewModel提供的loginDataChanged接口将内部存储的username与password改变
        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            //当editText的控件内text发生改变时，利用loginViewModel提供的loginDataChanged接口将内部存储的username与password改变
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }


            //登录button的click监听，设置loading动画，调用login函数进行上方的login调用，进行login的判断
            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                val nameStr: String = username.text.toString()
                val passwordStr: String = password.text.toString()
                loginViewModel.login(nameStr, passwordStr, "result")
//                val request: Request = Request.Builder()
//                    .url("${BASE_URL}/user_login.php?name=$nameStr&password=$passwordStr")
//                    .build()
//                val call: Call = client.newCall(request)
//                call.enqueue(object : Callback {
//                    override fun onFailure(call: Call, e: IOException) {
//                        loginViewModel.login(nameStr, passwordStr, "-1")
//                        Log.e("OkHttp","get response onFailure :${e.message}")
//                    }
//
//                    override fun onResponse(call: Call, response: Response) {
//                        val body:String? = response.body?.string()
//                        runOnUiThread { //
//                            //解析响应报文body:
//                            var res = body?.split(";")
//                            val res_array:Array<String> = res?.toTypedArray()!!
//                            val result : String = res_array[0]
//                            loginViewModel.login(nameStr, passwordStr, result)
//                        }
//                        Log.e("OkHttp","get response successfully :${body}")
//                    }
//                })

            }
        }


    }

    //一个自定义函数，被用在验证通过后
    //可以通过重构这个函数来获得成功登录后的功能
    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
        Log.e("home","Search Self Information Yes")
        //普通用户
        if(model.type == 0){
            val intent: Intent = Intent(this, CommonUserActivity::class.java)
            intent.putExtra("account",binding.username.text.toString())
            startActivity(intent)
        }
        //管理员
        if(model.type == 1){
            val intent: Intent = Intent(this, AdministratorActivity::class.java)
            intent.putExtra("account",binding.username.text.toString())
            startActivity(intent)
        }


    }

    //一个自定义函数，被用在验证失败后，这里是设置了一个弹窗，提示失败
    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
        binding.password.setText("")
        val view: View? = this.currentFocus
        if (view != null) {
            hideKeyboard(view)
        }
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}