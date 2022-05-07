package com.example.cdc.ui.informationReport

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.cdc.R
import com.example.cdc.databinding.FragmentInformationReportBinding
import com.example.cdc.ui.Questionnaire
import com.example.cdc.ui.login.afterTextChanged
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit


class InformationReportFragment : Fragment() {
    private var questionnaire = Questionnaire()
//    private val questionList = mutableListOf<String>("ID", "姓名", "体温")
//    private val answerList = mutableListOf<String>("", "", "")
    private var _binding: FragmentInformationReportBinding? = null
    private val binding get() = _binding!!

    val BASE_URL = "http://124.71.150.114"
    val client: OkHttpClient = OkHttpClient.Builder()    //builder构造者设计模式
        .connectTimeout(10, TimeUnit.SECONDS) //连接超时时间
        .readTimeout(10, TimeUnit.SECONDS)    //读取超时
        .writeTimeout(10, TimeUnit.SECONDS)  //写超时，也就是请求超时
        .build()

    /*
    *  user_get_table():用户通过调用该函数获得最新的防疫问卷都有哪些具体属性;
    *  返回值为一个String，格式如下: 问题1;问题2;问题3;
    *  可以使用kotlin中String类内嵌的split方法来解析该字符串
    *  eg: user_get_table()
    *  收到的响应报文为: name;id;temperature;safety;
    * */
    fun user_get_table() {
        val request: Request = Request.Builder()
            .url("$BASE_URL/user_get_table.php")
            .build()
        val call: Call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("OkHttp", "get response onFailure :${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val body: String? = response.body?.string()
                Log.e("OkHttp", "get response successfully :${body}")
                activity?.runOnUiThread {
                    val res = body?.split(";")
                    val res_list:MutableList<String> = res?.toMutableList()!!
                    res_list.remove("safety")
                    res_list.remove("\n")
                    questionnaire.questionList = res_list
                    questionnaire.answerList = MutableList<String>(questionnaire.questionList.size){""}
                    val informationView: RecyclerView = binding.informationView
                    val informationAdapter = InformationViewAdapter(questionnaire.questionList,questionnaire.answerList)
                    informationView.adapter = informationAdapter
                }
            }

        })
    }

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
    fun user_post_info(mMap: HashMap<String, String>) {
        var x = FormBody.Builder()
        for (key in mMap.keys) {
            x.add(key, mMap.getValue(key))
        }
        x.build()
        var body: RequestBody = x.build()
        val request: Request = Request.Builder()
            .url("${BASE_URL}/user_post_info.php")
            .post(body)
            .build()
        val call: Call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("OkHttp", "get response onFailure :${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val body: String? = response.body?.string()
                activity?.runOnUiThread { //
                    val intent = Intent(activity, SubmitSuccessMessageActivity::class.java)
                    startActivity(intent)
                }
                Log.e("OkHttp", "get response successfully :${body}")
            }

        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInformationReportBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val buttonNormalInformationSubmit: Button = binding.normalInformationSubmitButton
        buttonNormalInformationSubmit.setOnClickListener {
            val mMap = HashMap<String,String>()
            for(i in questionnaire.questionList.indices){
                mMap[questionnaire.questionList[i]] = questionnaire.answerList[i]
            }
            mMap["safety"] = questionnaire.safety.toString()
            user_post_info(mMap)
        }
//        val informationView = binding.informationView
//        val informationAdapter = InformationViewAdapter(questionList,answerList)
//        informationView.adapter = informationAdapter
        user_get_table()


        return root
    }

    class InformationViewAdapter(
        private val questionList: MutableList<String>,
        private val answerList: MutableList<String>
    ) :
        RecyclerView.Adapter<InformationViewAdapter.InformationViewHolder>() {
        inner class InformationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val questionTextView: TextView =
                itemView.findViewById(R.id.questionnaire_submit_question_text)
            private val answerTextView: EditText =
                itemView.findViewById(R.id.questionnaire_submit_answer_text)

            fun bind(question: String, index: Int) {
                questionTextView.text = question
                answerTextView.afterTextChanged{
                    val answerEdited = answerTextView.text.toString()
                    saveAnswer(answerEdited, index)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InformationViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_questionnaire_submit, parent, false)
            return InformationViewHolder(view)
        }

        override fun onBindViewHolder(holder: InformationViewHolder, position: Int) {
            holder.bind(questionList[position], position)

        }

        override fun getItemCount(): Int {
            return questionList.size
        }

        @SuppressLint("NotifyDataSetChanged")
        fun saveAnswer(answer: String, index: Int) {
            answerList[index] = answer
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


