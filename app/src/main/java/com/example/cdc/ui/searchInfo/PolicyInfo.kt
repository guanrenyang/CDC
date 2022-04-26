package com.example.cdc.ui.searchInfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.cdc.R
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit

class PolicyInfo : AppCompatActivity() {

    private val policyInfoList = arrayOf<String>("name", "sex", "year", "birthday", "address")
    /*val BASE_URL ="http://124.71.150.114"
    val client: OkHttpClient = OkHttpClient.Builder()    //builder构造者设计模式
        .connectTimeout(10, TimeUnit.SECONDS) //连接超时时间
        .readTimeout(10, TimeUnit.SECONDS)    //读取超时
        .writeTimeout(10, TimeUnit.SECONDS)  //写超时，也就是请求超时
        .build();
    /*
        user_get_policy(Type,Date):用户通过调用该函数来获得政策，筛选条件是Type（S0~S5）和Date(eg:2022-04-25)。可以有缺省
        获得的相应报文（val body:String?）格式如下:每条政策之间用换行符“\n”隔开，每条政策有id、type、date、content共四条属性，它们之间用";"隔开.
        你可以使用kotlin中String类的split()来解析相应报文
        测试数据:        //user_get_policy(Type = "S1")
                       //user_get_policy(Date = "2022-04-18")
                       //user_get_policy("S1","2022-04-18")
    */
    fun user_get_policy(Type:String="Empty",Date:String="Empty"){
        val request: Request = Request.Builder()
            .url("${BASE_URL}/user_get_policy.php?Type=$Type&Date=$Date")
            .build()
        val call: Call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("OkHttp","get response onFailure :${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val body:String? = response.body?.string()
                runOnUiThread {
                    //在这里完成对UI界面的控制
                }
                Log.e("OkHttp","get response successfully :${body}")
            }

        })
    }*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_policy_info)

        val policyInfoRecyclerView: RecyclerView = findViewById(R.id.recycler_view_policy_info)
        policyInfoRecyclerView.adapter = PolicyInfoAdapter(policyInfoList)
    }


    class PolicyInfoAdapter(val policyInfoList: Array<String>): RecyclerView.Adapter<PolicyInfoViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PolicyInfoViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_policy_info, parent, false)
            return PolicyInfoViewHolder(view)
        }

        override fun onBindViewHolder(holder: PolicyInfoViewHolder, position: Int) {
            holder.bind(policyInfoList[position])
        }

        override fun getItemCount(): Int {
            return policyInfoList.size
        }

    }

    class PolicyInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val policyInfoTextView : TextView = itemView.findViewById(R.id.policy_info_text)
        fun bind(info: String){
            policyInfoTextView.text = info
        }
    }

//    class Datasource(val context: Context){
//        fun getArray():Array<String>{
//            return context.resources.getStringArray(R.array.array_self_info)
//        }
//    }
}