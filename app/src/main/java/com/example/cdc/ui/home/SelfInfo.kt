package com.example.cdc.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cdc.R


class SelfInfo : AppCompatActivity() {

    private val selfInfoList = arrayOf<String>("name", "sex", "year", "birthday", "address")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_self_info)

        val selfInfoRecyclerView: RecyclerView = findViewById(R.id.recycler_view_self_info)
        selfInfoRecyclerView.adapter = SelfInfoAdapter(selfInfoList)
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