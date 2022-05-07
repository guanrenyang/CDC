package com.example.cdc.ui.dataProcessing

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.cdc.R
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit
import com.example.cdc.ui.Questionnaire

class ViewQuestionnaireActivity : AppCompatActivity() {

    private val questionnaire = Questionnaire()
    val BASE_URL ="http://124.71.150.114"
    val client: OkHttpClient = OkHttpClient.Builder()    //builder构造者设计模式
        .connectTimeout(10, TimeUnit.SECONDS) //连接超时时间
        .readTimeout(10, TimeUnit.SECONDS)    //读取超时
        .writeTimeout(10, TimeUnit.SECONDS)  //写超时，也就是请求超时
        .build();
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
                runOnUiThread {
                    val res = body?.split(";")
                    val res_list:MutableList<String> = res?.toMutableList()!!
                    res_list.remove("safety")
                    res_list.remove("\n")
                    questionnaire.questionList = res_list
                }
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
                    val res = body?.split(";")
                    val res_list:MutableList<String> = res?.toMutableList()!!
                    res_list.remove("\n")
                    res_list.removeAt(res_list.lastIndex)
                    questionnaire.answerList = res_list
                    val questionnaireRecyclerView: RecyclerView = findViewById(R.id.questionnaire)
                    questionnaireRecyclerView.adapter = QuestionnaireAdapter(questionnaire.questionList, questionnaire.answerList)
                }
                Log.e("OkHttp","get response successfully :${body}")
            }

        })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_questionnaire)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val bundle = intent.extras
        val id = bundle?.getString("id")

        user_get_table()
        id?.let { admin_get_questionnaire(it) }

    }

    class QuestionnaireAdapter(val questionList: MutableList<String>, val answerList: MutableList<String>) :
        RecyclerView.Adapter<QuestionnaireAdapter.QuestionnaireViewHolder>() {
        inner class QuestionnaireViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val questionTextView: TextView = itemView.findViewById(R.id.questionnaire_question_text)
            private val answerTextView: TextView = itemView.findViewById(R.id.questionnaire_answer_text)
            fun bind(question: String, answer: String) {
                questionTextView.text = question
                answerTextView.text = answer
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionnaireViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_questionnaire, parent, false)
            return QuestionnaireViewHolder(view)
        }

        override fun onBindViewHolder(holder: QuestionnaireViewHolder, position: Int) {
            holder.bind(questionList[position], answerList[position])
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
