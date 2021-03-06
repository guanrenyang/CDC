package com.example.cdc.ui.searchInfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cdc.R
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit

class SelfInfo : AppCompatActivity() {
    val BASE_URL ="http://124.71.150.114"
    val client:OkHttpClient  = OkHttpClient.Builder()    //builder构造者设计模式
        .connectTimeout(10, TimeUnit.SECONDS) //连接超时时间
        .readTimeout(10, TimeUnit.SECONDS)    //读取超时
        .writeTimeout(10, TimeUnit.SECONDS)  //写超时，也就是请求超时
        .build();
    /*
    *  user_get_selfinfo(Id:String):用户通过该函数向个人信息数据库请求数据，输入参数为个人的id
        get成功后返回一个String，每个元素用 ; 隔开。元素分别是id;name;age;address;gender(0-->女人，1-->男人);healthy(0-->未感染新冠，1-->感染新冠)
        可以使用string.split()函数解析报文
        测试数据:uesr_get_selfinfo("0318")
    */
    fun user_get_selfinfo(Id: String){
        val request: Request = Request.Builder()
            .url("${BASE_URL}/user_get_selfinfo.php?Id=$Id")
            .build()
        val call:Call = client.newCall(request)
        call.enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    val connectionFailedText: TextView = findViewById(R.id.title_self_info_connection_failed)
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
                    val selfInfoRecyclerView: RecyclerView = findViewById(R.id.recycler_view_self_info)
                    selfInfoRecyclerView.adapter = SelfInfoAdapter(res_array)
                }
                Log.e("OkHttp","get response successfully :${body}")
            }

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_self_info)

        val noSelfInfoText: TextView = findViewById(R.id.title_no_self_info)
        noSelfInfoText.isVisible = false
        val connectionFailedText: TextView = findViewById(R.id.title_self_info_connection_failed)
        connectionFailedText.isVisible = false

        //从外部输入的查询参数account
        val extraData = intent.getStringExtra("account")
        Log.e("SelfInfo",extraData.toString())

        user_get_selfinfo("0318")
        //selfInfoRecyclerView.adapter = SelfInfoAdapter(selfInfoList)
    }



    class SelfInfoAdapter(val selfInfoList: Array<String>): RecyclerView.Adapter<SelfInfoViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelfInfoViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_self_info, parent, false)
            return SelfInfoViewHolder(view)
        }

        override fun onBindViewHolder(holder: SelfInfoViewHolder, position: Int) {
            if(position % 6 == 0){
                holder.bind("ID: "+selfInfoList[position])
            }
            if(position % 6 == 1){
                holder.bind("Name: "+selfInfoList[position])
            }
            if(position % 6 == 2){
                holder.bind("Age: "+selfInfoList[position])
            }
            if(position % 6 == 3){
                holder.bind("Address: "+selfInfoList[position])
            }
            if(position % 6 == 4){
                if(selfInfoList[position][0]=='0'){
                    holder.bind("Sex: 女")
                }
                if(selfInfoList[position][0]=='1'){
                    holder.bind("Sex: 男")
                }
            }
//            if(position % 6 == 5){
//                if(selfInfoList[position][0]=='0'){
//                    holder.bind("Health: 未感染新冠")
//                }
//                if(selfInfoList[position][0]=='1'){
//                    holder.bind("Health: 感染新冠")
//                }
//            }

        }

        override fun getItemCount(): Int {
            return selfInfoList.size - 2
        }

    }

    class SelfInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val selfInfoTextView : TextView = itemView.findViewById(R.id.self_info_text)
        fun bind(info: String){
            selfInfoTextView.text = info
        }
    }

//    class Datasource(val context: Context){
//        fun getArray():Array<String>{
//            return context.resources.getStringArray(R.array.array_self_info)
//        }
//    }
}