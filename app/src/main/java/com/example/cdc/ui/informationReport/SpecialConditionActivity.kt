package com.example.cdc.ui.informationReport

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.cdc.R

class SpecialConditionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_special_condition)

        // 从特殊信息上报页面提交表单
        val specialConditionSubmitButton : Button = findViewById(R.id.special_condition_submit_button)

        specialConditionSubmitButton.setOnClickListener {
            startActivity(Intent(this, SubmitSuccessMessageActivity::class.java))
        }
    }
}