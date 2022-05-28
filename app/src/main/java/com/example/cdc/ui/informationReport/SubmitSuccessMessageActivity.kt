package com.example.cdc.ui.informationReport

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.cdc.CommonUserActivity
import com.example.cdc.R

class SubmitSuccessMessageActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_success_message)

        // 从特殊信息上报页面提交表单
        val submitSuccessReturnButton : Button = findViewById(R.id.submit_success_return_button)

        submitSuccessReturnButton.setOnClickListener {
            startActivity(Intent(this, CommonUserActivity::class.java))
        }

        val submitSuccessmessage: TextView =  findViewById(R.id.submit_success_message)
        val bundle = intent.extras
        val safety = bundle?.getBoolean("safety")
        if (safety == false){
            submitSuccessmessage.text = "提交成功\n您的状态异常!\n请及时报告！"
        }
    }
}