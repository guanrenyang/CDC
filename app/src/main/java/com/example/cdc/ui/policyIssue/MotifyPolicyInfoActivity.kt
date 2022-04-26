package com.example.cdc.ui.policyIssue

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.*
import android.widget.Toast.*
import androidx.appcompat.app.ActionBar
import com.example.cdc.R
import okhttp3.*
import okhttp3.Callback
import java.io.IOException
import java.util.concurrent.TimeUnit

class MotifyPolicyInfoActivity : AppCompatActivity() {
    val BASE_URL ="http://124.71.150.114"
    val client: OkHttpClient = OkHttpClient.Builder()    //builder构造者设计模式
        .connectTimeout(10, TimeUnit.SECONDS) //连接超时时间
        .readTimeout(10, TimeUnit.SECONDS)    //读取超时
        .writeTimeout(10, TimeUnit.SECONDS)  //写超时，也就是请求超时
        .build();
    fun admin_post_policy(Date: String,Rank:String,Content:String){
        val body : RequestBody = FormBody.Builder()
            .add("Date",Date)
            .add("Rank",Rank)
            .add("Content",Content)
            .build();
        val request: Request = Request.Builder()
            .url("$BASE_URL/admin_post_policy.php")
            .post(body)
            .build()
        val call: Call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("OkHttp","get response onFailure :${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val body:String? = response.body?.string()
                runOnUiThread { //
                    Toast.makeText(this@MotifyPolicyInfoActivity ,"${body}", Toast.LENGTH_LONG).show();
                    finish()
                }
                Log.e("OkHttp","get response successfully :${body}")
            }

        })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_motify_policy_info)
        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setTitle("政策发布")
        }
        val intent : Intent = getIntent()
        var _rank:Float =Float.NaN
        val bundle: Bundle? = intent.getExtras()
        //makeText(this, bundle?.getString("date"), LENGTH_LONG).show();
        val policyDate: TextView = findViewById(R.id.policyDate)
        policyDate.setText(bundle?.getString("date"))
        val thedate:String?=bundle?.getString("date")
        val policyRank : RatingBar = findViewById(R.id.ratingBar1)
        val policyIssueButton:Button = findViewById(R.id.btn)
        val policyContent: EditText = findViewById(R.id.policyContent)
        policyIssueButton.setOnClickListener {
            _rank=policyRank.rating
            val rank = _rank.toInt()
            val therank:String = "S"+rank.toString()
            val content :String = policyContent.text.toString()
            admin_post_policy(thedate!!,therank,content)
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