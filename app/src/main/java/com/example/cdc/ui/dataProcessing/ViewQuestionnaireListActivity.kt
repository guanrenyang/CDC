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


class ViewQuestionnaireListActivity : AppCompatActivity() {

    private val nameList = arrayOf<String>("AAA", "BBB", "CCC", "DDD", "EEE")
    private val idList = arrayOf<String>("111", "222", "333", "444", "555")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_questionnaire_list)
        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        val questionnaireListRecyclerView: RecyclerView = findViewById(R.id.questionnaire_list)

        questionnaireListRecyclerView.adapter = QuestionnaireListAdapter(nameList, idList)

    }

    class QuestionnaireListAdapter(val nameList: Array<String>, val idList: Array<String>) :
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
