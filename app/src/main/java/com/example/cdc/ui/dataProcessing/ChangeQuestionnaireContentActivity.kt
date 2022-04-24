package com.example.cdc.ui.dataProcessing

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.cdc.R

class ChangeQuestionnaireContentActivity : AppCompatActivity() {

    private val questionList = mutableListOf<String>("name", "sex", "year", "birthday", "address", "girlfriend")

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_questionnaire_content)
        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        val changeQuestionnaireContentView: RecyclerView = findViewById(R.id.ChangeQuestionnaireContent_list)
        val changeAdapter = ChangeQuestionnaireContentViewAdapter(questionList)
        changeQuestionnaireContentView.adapter = changeAdapter
        val addButton: Button = findViewById(R.id.add_button_ChangeQuestionnaireContent)
        addButton.setOnClickListener(){
            questionList.add("Question")
            changeAdapter.notifyDataSetChanged()
        }

    }

    class ChangeQuestionnaireContentViewAdapter(private val questionList: MutableList<String>) :
        RecyclerView.Adapter<ChangeQuestionnaireContentViewAdapter.ChangeQuestionnaireContentViewHolder>() {
        inner class ChangeQuestionnaireContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val questionTextView: TextView = itemView.findViewById(R.id.ChangeQuestionnaireContent_text)
            private val editButton: Button = itemView.findViewById(R.id.ChangeQuestionnaireContent_edit_button)
            private val saveButton: Button = itemView.findViewById(R.id.ChangeQuestionnaireContent_save_button)
            private val deleteButton: Button = itemView.findViewById(R.id.ChangeQuestionnaireContent_delete_button)
            fun bind(question: String, index: Int) {
                questionTextView.text = question
                editButton.setOnClickListener{
                    questionTextView.isFocusable = true
                    questionTextView.isFocusableInTouchMode = true
                    questionTextView.requestFocus()
                    editButton.isEnabled = false
                    saveButton.isEnabled = true
                }
                deleteButton.setOnClickListener{deleteItem(index)}
                saveButton.setOnClickListener{
                    val questionEdited = questionTextView.text.toString()
                    saveItem(questionEdited,index)
                    questionTextView.isFocusable = false
                    questionTextView.isFocusableInTouchMode = false
                    editButton.isEnabled = true
                    saveButton.isEnabled = false
                }
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChangeQuestionnaireContentViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_change_questionnaire, parent, false)
            return ChangeQuestionnaireContentViewHolder(view)
        }

        override fun onBindViewHolder(holder: ChangeQuestionnaireContentViewHolder, position: Int) {
            holder.bind(questionList[position],position)
        }

        override fun getItemCount(): Int {
            return questionList.size
        }

        @SuppressLint("NotifyDataSetChanged")
        fun deleteItem(index: Int){
            questionList.removeAt(index)
            notifyDataSetChanged()
        }

        @SuppressLint("NotifyDataSetChanged")
        fun saveItem(question: String, index: Int){
            questionList[index] = question
            notifyDataSetChanged()
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