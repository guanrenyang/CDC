package com.example.cdc.ui.dataProcessing

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.cdc.R
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit
import com.example.cdc.ui.Question


class ChangeQuestionnaireContentActivity : AppCompatActivity() {
    private var question = Question()

    val BASE_URL = "http://124.71.150.114"
    val client: OkHttpClient = OkHttpClient.Builder()    //builder构造者设计模式
        .connectTimeout(10, TimeUnit.SECONDS) //连接超时时间
        .readTimeout(10, TimeUnit.SECONDS)    //读取超时
        .writeTimeout(10, TimeUnit.SECONDS)  //写超时，也就是请求超时
        .build()
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

            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call, response: Response) {
                val body:String? = response.body?.string()
                Log.e("OkHttp","get response successfully :${body}")
                runOnUiThread {
                    val res = body?.split(";")
                    val res_list:MutableList<String> = res?.toMutableList()!!
                    res_list.remove("safety")
                    res_list.remove("\n")
                    question.questionList = res_list
                    val changeQuestionnaireContentView: RecyclerView =
                        findViewById(R.id.ChangeQuestionnaireContent_list)
                    val changeAdapter = ChangeQuestionnaireContentViewAdapter(question.questionList)
                    changeQuestionnaireContentView.adapter = changeAdapter
                    val addButton: Button = findViewById(R.id.add_button_ChangeQuestionnaireContent)
                    addButton.setOnClickListener() {
                        question.questionList.add("Question")
                        changeAdapter.notifyDataSetChanged()
                    }
                }
            }

        })
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_questionnaire_content)


        val actionBar: ActionBar? = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        user_get_table()
        val saveListButton: Button = findViewById(R.id.Save_ChangeQuestionnaireContent)
        saveListButton.setOnClickListener() {
            val postList= mutableListOf<String>()
            postList.addAll(question.questionList)
            postList.add("safety")
            admin_post_table(postList)
        }

    }

    class ChangeQuestionnaireContentViewAdapter(private val questionList: MutableList<String>) :
        RecyclerView.Adapter<ChangeQuestionnaireContentViewAdapter.ChangeQuestionnaireContentViewHolder>() {
        inner class ChangeQuestionnaireContentViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            private val questionTextView: TextView =
                itemView.findViewById(R.id.ChangeQuestionnaireContent_text)
            private val editButton: Button =
                itemView.findViewById(R.id.ChangeQuestionnaireContent_edit_button)
            private val saveButton: Button =
                itemView.findViewById(R.id.ChangeQuestionnaireContent_save_button)
            private val deleteButton: Button =
                itemView.findViewById(R.id.ChangeQuestionnaireContent_delete_button)

            fun bind(question: String, index: Int) {
                questionTextView.text = question
                editButton.setOnClickListener {
                    questionTextView.isFocusable = true
                    questionTextView.isFocusableInTouchMode = true
                    questionTextView.requestFocus()
                    editButton.isEnabled = false
                    saveButton.isEnabled = true
                }
                deleteButton.setOnClickListener { deleteItem(index) }
                saveButton.setOnClickListener {
                    val questionEdited = questionTextView.text.toString()
                    saveItem(questionEdited, index)
                    questionTextView.isFocusable = false
                    questionTextView.isFocusableInTouchMode = false
                    editButton.isEnabled = true
                    saveButton.isEnabled = false
                }
            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ChangeQuestionnaireContentViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_change_questionnaire, parent, false)
            return ChangeQuestionnaireContentViewHolder(view)
        }

        override fun onBindViewHolder(holder: ChangeQuestionnaireContentViewHolder, position: Int) {
            holder.bind(questionList[position], position)
        }

        override fun getItemCount(): Int {
            return questionList.size
        }

        @SuppressLint("NotifyDataSetChanged")
        fun deleteItem(index: Int) {
            questionList.removeAt(index)
            notifyDataSetChanged()
        }

        @SuppressLint("NotifyDataSetChanged")
        fun saveItem(question: String, index: Int) {
            questionList[index] = question
            notifyDataSetChanged()
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