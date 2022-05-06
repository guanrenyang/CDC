package com.example.cdc.ui.dataProcessing

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.cdc.R
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit

class ViewQuestionnaireActivity : AppCompatActivity() {

    private val questionList = arrayOf<String>("name", "sex", "year", "birthday", "address")
    private val answerList = arrayOf<String>("aaa", "bbb", "ccc", "ddd", "eee")
    val BASE_URL ="http://124.71.150.114"
    val client: OkHttpClient = OkHttpClient.Builder()    //builder构造者设计模式
        .connectTimeout(10, TimeUnit.SECONDS) //连接超时时间
        .readTimeout(10, TimeUnit.SECONDS)    //读取超时
        .writeTimeout(10, TimeUnit.SECONDS)  //写超时，也就是请求超时
        .build();
    /*
        user_post_info(map):用户通过该函数向服务器的最新一个防疫数据库提交防疫信息，输入参数为HushMap的形式。
        注意：提交的信息将会存储在最新被管理层创建的数据库中
        eg: 利用user_post_info()接口向服务器中5月3号的问卷数据库上报用户信息
        val mMap = HashMap<String,String>()
        mMap.put("name","yzy")
        mMap.put("id","0002")
        mMap.put("safety","1")
        mMap.put("temperature","36.9")
        user_post_info(mMap)
    */
    fun user_post_info(mMap:HashMap<String,String>){
        var x = FormBody.Builder()
        for(key in mMap.keys){
            x.add(key,mMap.getValue(key))
        }
        x.build()
        var body : RequestBody = x.build()
        val request: Request = Request.Builder()
            .url("${BASE_URL}/user_post_info.php")
            .post(body)
            .build()
        val call: Call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("OkHttp","get response onFailure :${e.message}")
                runOnUiThread { //
                    //todo:上报信息失败，请在这里控制UI界面
                }
            }
            override fun onResponse(call: Call, response: Response) {
                val body:String? = response.body?.string()
                runOnUiThread { //
                    //todo:上报信息成功，请在这里控制UI界面
                }
                Log.e("OkHttp","get response successfully :${body}")
            }

        })
    }
    /*
    *  user_get_table():用户通过调用该函数获得最新的防疫问卷都有哪些具体属性;
    *  返回值为一个String，格式如下: 问题1;问题2;问题3;
    *  可以使用kotlin中String类内嵌的split方法来解析该字符串
    *  eg: user_get_table()
    *  收到的响应报文为: name;id;temperature;safety;
    * */
    fun user_get_table(){
        val request: Request = Request.Builder()
            .url("$BASE_URL/user_get_table.php")
            .build()
        val call:Call =client.newCall(request)
        call.enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.e("OkHttp","get response onFailure :${e.message}")
                runOnUiThread { //
                    //todo:获取信息失败，请在这里控制UI界面
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val body:String? = response.body?.string()
                Log.e("OkHttp","get response successfully :${body}")
                runOnUiThread { //
                    //todo:获取信息成功，请在这里控制UI界面
                }
            }

        })
    }
    /*
    * admin_post_table(qlist):管理员通过该接口来创建一个最新的问卷数据库，指定该数据库所具有的属性，并更新后端的Header指针（Header指针指向最新创建的数据库）。
    * 参数格式: qlist数据类型为MutableList<String>，其中的每一个元素代表了一个问卷属性（约定：每一种问卷都具有id、name、safety这三种属性）
    * eg: 创建最新的一个数据库，其中的问卷的内容有name、id、gender、temperature
    * val testList: MutableList<String> = mutableListOf<String>("name", "id", "gender", "temperature","safety")
    * admin_post_table(testList)
    * */
    fun admin_post_table(qlist:MutableList<String>){
        var x = FormBody.Builder()
        var i:Int =1
        for (item in qlist){
            x.add(i.toString(),item)
            i=i+1
        }
        x.build()
        var body :RequestBody= x.build()
        val request: Request = Request.Builder()
            .url("${BASE_URL}/admin_post_table.php")
            .post(body)
            .build()
        val call:Call = client.newCall(request)
        call.enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.e("OkHttp","get response onFailure :${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val body:String? = response.body?.string()
                runOnUiThread {
                    //todo 数据上传成功，在这里控制UI界面
                }
                Log.e("OkHttp","get response successfully :${body}")
            }

        })
    }
    /*
    * admin_get_id() 管理员可以通过调用该接口获得最新数据库中每一个条目的id，name,safety这三个属性（约定：每一种问卷都具有id、name、safety这三种属性）
    * 返回报文的格式是一个字符串，格式如下: id1;name1;safety1;\nid2;name2;safety2;\n
    * 即每一行代表一个用户的三个属性，用”;“隔开,每个用户信息之间通过换行符”\n“隔开，你可以使用String.split()函数来解析报文
    * */
    fun admin_get_id(){
        val request: Request = Request.Builder()
            .url("${BASE_URL}/admin_get_id.php")
            .build()
        val call:Call = client.newCall(request)
        call.enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.e("OkHttp","get response onFailure :${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val body:String? = response.body?.string()
                runOnUiThread { //
                    //todo 数据获取成功，在这里控制UI界面
                }
                Log.e("OkHttp","get response successfully :${body}")
            }

        })
    }
    /*
    * admin_get_questionnaire(id) 管理员通过该接口来获取某一特定用户在最新数据库所填写的问卷内容
    * 参数：id:String ————>用于指定要查询的用户身份
    * eg: 查询最新数据库中id为0318的用户信息
    * admin_get_questionnaire("0318")
    * */
    fun admin_get_questionnaire(id:String){
        val request: Request = Request.Builder()
            .url("${BASE_URL}/admin_get_questionnaire.php?Id=$id")
            .build()
        val call:Call = client.newCall(request)
        call.enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.e("OkHttp","get response onFailure :${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val body:String? = response.body?.string()
                runOnUiThread { //
                    //todo 数据获取成功，在这里控制UI界面
                }
                Log.e("OkHttp","get response successfully :${body}")
            }

        })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_questionnaire)
        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        val questionnaireRecyclerView: RecyclerView = findViewById(R.id.questionnaire)

        questionnaireRecyclerView.adapter = QuestionnaireAdapter(questionList, answerList)

    }

    class QuestionnaireAdapter(val questionList: Array<String>, val answerList: Array<String>) :
        RecyclerView.Adapter<QuestionnaireAdapter.QuestionnaireViewHolder>() {
        inner class QuestionnaireViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val nameTextView: TextView = itemView.findViewById(R.id.name_text)
            private val idTextView: TextView = itemView.findViewById(R.id.id_text)
            fun bind(name: String, id: String) {
                nameTextView.text = name
                idTextView.text = id
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionnaireViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_name_id, parent, false)
            return QuestionnaireViewHolder(view)
        }

        override fun onBindViewHolder(holder: QuestionnaireViewHolder, position: Int) {
            holder.bind(questionList[position], answerList[position])
            holder.itemView.setOnClickListener { view ->
                val intent = Intent(view.context, ViewQuestionnaireActivity::class.java)
                view.context.startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return questionList.size
        }
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
