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

class ViewAbnormalQuestionnaireListActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_abnormal_questionnaire_list)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val unsafeNameList = this.intent.getStringArrayListExtra("name")
        val unsafeIDList = this.intent.getStringArrayListExtra("id")
        val questionnaireListRecyclerView: RecyclerView = findViewById(R.id.abnormalListRecyclerView)
        questionnaireListRecyclerView.adapter =
            unsafeNameList?.let { unsafeIDList?.let { it1 -> QuestionnaireListAdapter(it, it1) } }
    }

    class QuestionnaireListAdapter(val nameList: MutableList<String>, val idList: MutableList<String>) :
        RecyclerView.Adapter<QuestionnaireListViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionnaireListViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_name_id, parent, false)
            return QuestionnaireListViewHolder(view)
        }

        override fun onBindViewHolder(holder: QuestionnaireListViewHolder, position: Int) {
            holder.bind(nameList[position], idList[position])
            holder.itemView.setOnClickListener { view ->
                val intent = Intent(view.context, ViewQuestionnaireActivity::class.java)
                intent.putExtra("id", idList[position])
                view.context.startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return nameList.size
        }
    }

    class QuestionnaireListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.name_text)
        private val idTextView: TextView = itemView.findViewById(R.id.id_text)
        fun bind(name: String, id: String) {
            nameTextView.text = name
            idTextView.text = id
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


