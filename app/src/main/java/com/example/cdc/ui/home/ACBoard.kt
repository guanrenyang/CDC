package com.example.cdc.ui.home

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.cdc.R


class ACBoard : AppCompatActivity() {

    private val problemList = arrayOf<String>("name", "sex", "year", "birthday", "address")
    private val answerList = arrayOf<String>("name", "sex", "year", "birthday", "address")

    fun hideKeyboard(view: View){
        val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        view.requestFocus()
        inputManager.hideSoftInputFromWindow(view.windowToken,0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acboard)

        //提交按钮
        val submitButton: Button = findViewById(R.id.button_submitProblem)
        submitButton.setOnClickListener(){
            Log.e("home","yes")
            print("yes")
            val inputProblemBoard: EditText = findViewById(R.id.editText_inputProblem)
            inputProblemBoard.setText("")
            val view: View? = this.currentFocus
            if (view != null) {
                hideKeyboard(view)
            }
        }
        //数据库问题显示
        val acboardRecyclerView :RecyclerView = findViewById(R.id.recycler_view_ACBoard)
        acboardRecyclerView.adapter = ACBoardAdapter(problemList, answerList)

        val divisionLine : DividerItemDecoration
        //divisionLine.setOrientation()
        //acboardRecyclerView.addItemDecoration(DividerItemDecoration.VERTICAL)
    }



    class ACBoardAdapter(val problemList: Array<String>, val answerList: Array<String>): RecyclerView.Adapter<ACBoardViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ACBoardViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_acboard, parent, false)
            return ACBoardViewHolder(view)
        }

        override fun onBindViewHolder(holder: ACBoardViewHolder, position: Int) {
            holder.bind(problemList[position], answerList[position])
        }

        override fun getItemCount(): Int {
            return problemList.size
        }

    }

    class ACBoardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val problemTextView : TextView = itemView.findViewById(R.id.ACBoard_problem_text)
        private val answerTextView : TextView = itemView.findViewById(R.id.ACBoard_answer_text)
        fun bind(problem: String, answer: String){
            problemTextView.text = problem
            answerTextView.text = answer
        }
    }

//    class Datasource(val context: Context){
//        fun getArray():Array<String>{
//            return context.resources.getStringArray(R.array.array_self_info)
//        }
//    }
}