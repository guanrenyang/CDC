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
        user_post_info(map):用户通过该函数向服务器提交防疫问卷，输入参数为HushMap的形式。
        注意：在HushMap中，一定需要一个key值为Date的条目，它用于绑定后端的数据库，其格式为：db_日期
        eg: 利用user_post_info()接口向服务器中5月3号的问卷数据库上报用户信息
        val mMap = HashMap<String,String>()
        mMap.put("name","ybc")
        mMap.put("id","0011")
        mMap.put("gender","male")
        mMap.put("temperature","36.5")
        mMap.put("Date","db_0503") //!!一定要有！！
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
    *  user_get_table(Date):用户通过调用该函数获得某一天的防疫问卷都有哪些具体属性，值得注意的是Date采用db_日期的形式;
    *  返回值为一个String，格式如下: 问题1;问题2;问题3;
    *  可以使用kotlin中String类内嵌的split方法来解析该字符串
    *  eg: user_get_table("db_0503")
    *  收到的响应报文为: name;id;gender;temperature;
    * */
    fun user_get_table(Date: String){
        val request: Request = Request.Builder()
            .url("$BASE_URL/user_get_table.php?Date=$Date")
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
    * admin_post_table(qlist):管理员通过该接口来创建一个问卷数据库，并指定该数据库所具有的属性。
    * 参数格式: qlist数据类型为Array<String>,它的第一个元素为问卷日期（同时也是数据库的名字），格式为"db_日期"
    *         之后的每个元素代表着创建数据库所具有的属性
    * eg: 为5月4日创建一个数据库，其中的问卷的内容有name、id、gender、temperature
    * val testList = arrayOf<String>("db_0504", "name", "id", "gender", "temperature")
    * admin_post_table(testList)
    * */
    fun admin_post_table(qlist:Array<String>){
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
