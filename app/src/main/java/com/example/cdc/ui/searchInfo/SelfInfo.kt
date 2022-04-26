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
                Log.e("OkHttp","get response onFailure :${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val body:String? = response.body?.string()
                runOnUiThread { //

                }
                Log.e("OkHttp","get response successfully :${body}")
            }

        })
    }
    fun user_get_table(Date: String){
        val request: Request = Request.Builder()
            .url("${BASE_URL}/user_get_table.php?Date=$Date")
            .build()
        val call:Call = client.newCall(request)
        call.enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.e("OkHttp","get response onFailure :${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val body:String? = response.body?.string()
                runOnUiThread {
                    //在这里进行UI界面的操作

                    //解析响应报文body:
                    var res = body?.split(";")
                    val res_array:Array<String> = res?.toTypedArray()!!

                    //对UI界面进行操作
                    val selfInfoRecyclerView: RecyclerView = findViewById(R.id.recycler_view_self_info)
                    selfInfoRecyclerView.adapter = SelfInfoAdapter(res_array)
                    //Toast.makeText(this@SelfInfo ,"${res}", Toast.LENGTH_LONG).show();
                }
                Log.e("OkHttp","get response successfully :${body}")
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_self_info)

        //从外部输入的查询参数account
        val extraData = intent.getStringExtra("account")
        Log.e("SelfInfo",extraData.toString())

        user_get_table("db_0414")
        //selfInfoRecyclerView.adapter = SelfInfoAdapter(selfInfoList)
    }



    class SelfInfoAdapter(val selfInfoList: Array<String>): RecyclerView.Adapter<SelfInfoViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelfInfoViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_self_info, parent, false)
            return SelfInfoViewHolder(view)
        }

        override fun onBindViewHolder(holder: SelfInfoViewHolder, position: Int) {
            holder.bind(selfInfoList[position])
        }

        override fun getItemCount(): Int {
            return selfInfoList.size
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