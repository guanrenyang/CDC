package com.example.cdc.ui.searchInfo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.cdc.R
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit


class ACBoard : AppCompatActivity() {

    private val problemList = arrayOf<String>("name", "sex", "year", "birthday", "address")
    private val answerList = arrayOf<String>("name", "sex", "year", "birthday", "address")
    val BASE_URL ="http://124.71.150.114"
    val client: OkHttpClient = OkHttpClient.Builder()    //builder构造者设计模式
        .connectTimeout(10, TimeUnit.SECONDS) //连接超时时间
        .readTimeout(10, TimeUnit.SECONDS)    //读取超时
        .writeTimeout(10, TimeUnit.SECONDS)  //写超时，也就是请求超时
        .build();
    /*
        * user_get_ACBoard():用户和管理员调用该函数获得留言板上的信息
        * 响应报文格式如下:每个条目之间通过"\n"分隔，每个条目具有3个属性，id;question;answer;  通过";"进行分隔
        * 你可以使用String.split()函数解析报文
     * */
    fun user_get_ACBoard(){
        val request: Request = Request.Builder()
            .url("${BASE_URL}/user_get_ACBoard.php?")
            .build()
        val call: Call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    val connectionFailedText: TextView = findViewById(R.id.title_ACBoard_connection_failed)
                    connectionFailedText.isVisible = true
                }
                Log.e("OkHttp","get response onFailure :${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val body:String? = response.body?.string()
                runOnUiThread { //
                    //解析响应报文body:
                    var res = body?.split(";")
                    val res_array:Array<String> = res?.toTypedArray()!!
                    val acboardRecyclerView :RecyclerView = findViewById(R.id.recycler_view_ACBoard)
                    acboardRecyclerView.adapter = ACBoardAdapter(res_array)
                    //todo...
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
        setContentView(R.layout.activity_acboard)

        val noACBoardText: TextView = findViewById(R.id.title_no_ACBoard_info)
        noACBoardText.isVisible = false
        val connectionFailedText: TextView = findViewById(R.id.title_ACBoard_connection_failed)
        connectionFailedText.isVisible = false

        //提交按钮
        val submitButton: Button = findViewById(R.id.button_submitProblem)
        submitButton.setOnClickListener(){
            Log.e("home","yes")
            print("yes")
            val inputProblemBoard: EditText = findViewById(R.id.editText_inputProblem)
            inputProblemBoard.setText("")
            val view: View? = this.currentFocus
            if (view != null) {
                hideKeyboard(view)
            }
        }
        //数据库问题显示
        user_get_ACBoard()


    }



    class ACBoardAdapter(val pairList: Array<String>): RecyclerView.Adapter<ACBoardViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ACBoardViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_acboard, parent, false)
            return ACBoardViewHolder(view)
        }

        override fun onBindViewHolder(holder: ACBoardViewHolder, position: Int) {
            holder.bind(pairList[3*position+1], pairList[3*position+2])
        }

        override fun getItemCount(): Int {
            return (pairList.size/3)
        }

    }

    class ACBoardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val problemTextView : TextView = itemView.findViewById(R.id.ACBoard_problem_text)
        private val answerTextView : TextView = itemView.findViewById(R.id.ACBoard_answer_text)
        fun bind(problem: String, answer: String){
            problemTextView.text = "问题: "+ problem
            answerTextView.text = "回答: "+answer
        }
    }

//    class Datasource(val context: Context){
//        fun getArray():Array<String>{
//            return context.resources.getStringArray(R.array.array_self_info)
//        }
//    }
}