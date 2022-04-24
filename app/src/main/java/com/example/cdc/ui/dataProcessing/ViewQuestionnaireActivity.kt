package com.example.cdc.ui.dataProcessing

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.cdc.R

class ViewQuestionnaireActivity : AppCompatActivity() {

    private val questionList = arrayOf<String>("name", "sex", "year", "birthday", "address")
    private val answerList = arrayOf<String>("aaa", "bbb", "ccc", "ddd", "eee")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_questionnaire)
        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        val questionnaireRecyclerView: RecyclerView = findViewById(R.id.questionnaire)

        questionnaireRecyclerView.adapter = QuestionnaireAdapter(questionList, answerList)

    }

    class QuestionnaireAdapter(val questionList: Array<String>, val answerList: Array<String>) :
        RecyclerView.Adapter<QuestionnaireAdapter.QuestionnaireViewHolder>() {
        inner class QuestionnaireViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val nameTextView: TextView = itemView.findViewById(R.id.name_text)
            private val idTextView: TextView = itemView.findViewById(R.id.id_text)
            fun bind(name: String, id: String) {
                nameTextView.text = name
                idTextView.text = id
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionnaireViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_name_id, parent, false)
            return QuestionnaireViewHolder(view)
        }

        override fun onBindViewHolder(holder: QuestionnaireViewHolder, position: Int) {
            holder.bind(questionList[position], answerList[position])
            holder.itemView.setOnClickListener { view ->
                val intent = Intent(view.context, ViewQuestionnaireActivity::class.java)
                view.context.startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return questionList.size
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
