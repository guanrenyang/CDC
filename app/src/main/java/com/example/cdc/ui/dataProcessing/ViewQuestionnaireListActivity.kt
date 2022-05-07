package com.example.cdc.ui.dataProcessing


import android.content.Intent
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

class ViewQuestionnaireListActivity : AppCompatActivity() {

    private val nameList = mutableListOf<String>()
    private val idList = mutableListOf<String>()
    private val safetyList = mutableListOf<Boolean>()
    val BASE_URL ="http://124.71.150.114"
    val client: OkHttpClient = OkHttpClient.Builder()    //builder构造者设计模式
        .connectTimeout(10, TimeUnit.SECONDS) //连接超时时间
        .readTimeout(10, TimeUnit.SECONDS)    //读取超时
        .writeTimeout(10, TimeUnit.SECONDS)  //写超时，也就是请求超时
        .build()
    /*
    * admin_get_id() 管理员可以通过调用该接口获得最新数据库中每一个条目的id，name,safety这三个属性（约定：每一种问卷都具有id、name、safety这三种属性）
    * 返回报文的格式是一个字符串，格式如下: id1;name1;safety1;\nid2;name2;safety2;\n
    * 即每一行代表一个用户的三个属性，用”;“隔开,每个用户信息之间通过换行符”\n“隔开，你可以使用String.split()函数来解析报文
    * */
    fun admin_get_id(){
        val request: Request = Request.Builder()
            .url("${BASE_URL}/admin_get_id.php")
            .build()
        val call: Call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("OkHttp","get response onFailure :${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val body:String? = response.body?.string()
                runOnUiThread {
                    val res = body?.split("\n")
                    val res_array:Array<String> = res?.toTypedArray()!!
                    for (res_item in res_array){
                        if (res_item != ""){
                            val sub_res = res_item.split(";")
                            val sub_res_list:Array<String> = sub_res.toTypedArray()
                            idList.add(sub_res_list[0])
                            nameList.add(sub_res_list[1])
                            safetyList.add(sub_res_list[2].toBoolean())
                        }
                    }
                    val questionnaireListRecyclerView: RecyclerView = findViewById(R.id.questionnaire_list_view)
                    questionnaireListRecyclerView.adapter = QuestionnaireListAdapter(nameList, idList)
                }
                Log.e("OkHttp","get response successfully :${body}")
            }

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_questionnaire_list)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        admin_get_id()
        val abnormalSelectButton : Button = findViewById(R.id.ScreeningAbnormalQuestionnaires_button)
        abnormalSelectButton.setOnClickListener { view ->
            val intent = Intent(view.context, ViewAbnormalQuestionnaireListActivity::class.java)
            val unsafeNameList = ArrayList<String>()
            val unsafeIDList = ArrayList<String>()
            for (i in safetyList.indices){
                if(!safetyList[i]){
                    unsafeNameList.add(nameList[i])
                    unsafeIDList.add(idList[i])
                }
            }
            intent.putStringArrayListExtra("name",unsafeNameList)
            intent.putStringArrayListExtra("id",unsafeIDList)
            view.context.startActivity(intent)
        }
    }

    class QuestionnaireListAdapter(val nameList: MutableList<String>, val idList: MutableList<String>) :
        RecyclerView.Adapter<QuestionnaireListViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionnaireListViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_name_id, parent, false)
            return QuestionnaireListViewHolder(view)
        }

        override fun onBindViewHolder(holder: QuestionnaireListViewHolder, position: Int) {
            holder.bind(nameList[position], idList[position])
            holder.itemView.setOnClickListener { view ->
                val intent = Intent(view.context, ViewQuestionnaireActivity::class.java)
                intent.putExtra("id", idList[position])
                view.context.startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return nameList.size
        }
    }

    class QuestionnaireListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.name_text)
        private val idTextView: TextView = itemView.findViewById(R.id.id_text)
        fun bind(name: String, id: String) {
            nameTextView.text = name
            idTextView.text = id
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
