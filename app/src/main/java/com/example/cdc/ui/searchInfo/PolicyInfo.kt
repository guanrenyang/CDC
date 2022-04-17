package com.example.cdc.ui.searchInfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cdc.R

class PolicyInfo : AppCompatActivity() {

    private val policyInfoList = arrayOf<String>("name", "sex", "year", "birthday", "address")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_policy_info)

        val policyInfoRecyclerView: RecyclerView = findViewById(R.id.recycler_view_policy_info)
        policyInfoRecyclerView.adapter = PolicyInfoAdapter(policyInfoList)
    }


    class PolicyInfoAdapter(val policyInfoList: Array<String>): RecyclerView.Adapter<PolicyInfoViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PolicyInfoViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_policy_info, parent, false)
            return PolicyInfoViewHolder(view)
        }

        override fun onBindViewHolder(holder: PolicyInfoViewHolder, position: Int) {
            holder.bind(policyInfoList[position])
        }

        override fun getItemCount(): Int {
            return policyInfoList.size
        }

    }

    class PolicyInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val policyInfoTextView : TextView = itemView.findViewById(R.id.policy_info_text)
        fun bind(info: String){
            policyInfoTextView.text = info
        }
    }

//    class Datasource(val context: Context){
//        fun getArray():Array<String>{
//            return context.resources.getStringArray(R.array.array_self_info)
//        }
//    }
}