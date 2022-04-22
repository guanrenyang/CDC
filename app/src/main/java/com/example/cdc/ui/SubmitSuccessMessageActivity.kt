package com.example.cdc.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.cdc.AdministratorActivity
import com.example.cdc.CommonUserActivity
import com.example.cdc.R

class SubmitSuccessMessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_success_message)

        // 从特殊信息上报页面提交表单
        val submitSuccessReturnButton : Button = findViewById(R.id.submit_success_return_button)

        submitSuccessReturnButton.setOnClickListener {
            startActivity(Intent(this, CommonUserActivity::class.java))
        }
    }
}