package com.example.cdc.ui.searchInfo

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.NumberPicker
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.cdc.databinding.FragmentSearchInfoBinding
import com.example.cdc.ui.dataProcessing.ChangeQuestionnaireContentActivity
import com.example.cdc.ui.login.LoginActivity


class SearchInfoFragment : Fragment() {

    private var _binding: FragmentSearchInfoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val contentList = arrayOf<String>("S0", "S1", "S2", "S3", "S4", "S5")

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentSearchInfoBinding.inflate(inflater, container, false)
        val root: View = binding.root


        //交流板部分控件
        val buttonACBoard: Button = binding.buttonACBoard


        buttonACBoard.setOnClickListener(){
            Log.e("search info","ACBoard Button Yes")
            val intent: Intent = Intent(activity, ACBoard::class.java)
            startActivity(intent)
        }

        //查询个人信息部分控件

        //从外部传来的account信息，用于查询个人信息时的key
        val account = arguments?.getString("account").toString()
        Log.e("search info", account)

        val buttonSearchSelfInfo: Button = binding.buttonSearchSelfInfo


        buttonSearchSelfInfo.setOnClickListener(){
            Log.e("search info","Search Self Information Yes")
            val intent: Intent = Intent(activity, SelfInfo::class.java)
            intent.putExtra("account",account.toString())
            startActivity(intent)
        }

        //查询防疫政策部分控件
        val editTextSearchPolicy: EditText = binding.edittextSearchPolicy
        editTextSearchPolicy.isFocusable = false
        editTextSearchPolicy.isFocusableInTouchMode = false

        val editTextSearchDate: EditText = binding.edittextSearchDate
        editTextSearchDate.isFocusable = false
        editTextSearchDate.isFocusableInTouchMode = false

        val numberPickerPolicySelection: NumberPicker = binding.numberPickerSearchPolicy
        numberPickerPolicySelection.minValue = 0
        numberPickerPolicySelection.maxValue = contentList.size - 1
        numberPickerPolicySelection.isVisible = false
        numberPickerPolicySelection.displayedValues = contentList
        numberPickerPolicySelection.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        val buttonNumberPickerConfirm : Button = binding.buttonNumberPickerConfirm
        buttonNumberPickerConfirm.isVisible = false
        buttonNumberPickerConfirm.setOnClickListener(){
            Log.e("search info","Search Policy button confirm")
            binding.numberPickerSearchPolicy.isVisible = false
            binding.buttonNumberPickerConfirm.isVisible = false
        }

        val buttonBackToLogin: Button = binding.buttonBackToLogin2
        buttonBackToLogin.setOnClickListener {
                startActivity(Intent(activity, LoginActivity::class.java))
            }

        val datePickerDateSelection: DatePicker = binding.datePickerSearchDate
        datePickerDateSelection.isVisible = false

        val buttonDatePickerConfirm: Button = binding.buttonDatePickerConfirm
        buttonDatePickerConfirm.isVisible = false
        buttonDatePickerConfirm.setOnClickListener(){
            Log.e("search info","Search Date Button confirm")
            binding.datePickerSearchDate.isVisible = false
            binding.buttonDatePickerConfirm.isVisible = false
        }


        val buttonSearchPolicy: Button = binding.buttonSearchPolicy
        buttonSearchPolicy.setOnClickListener(){
            Log.e("search info","Search Policy Yes")
            val intent: Intent = Intent(activity, PolicyInfo::class.java)
            intent.putExtra("PolicyType",binding.edittextSearchPolicy.text.toString())
            intent.putExtra("DateSelected",binding.edittextSearchDate.text.toString())
            startActivity(intent)
        }

        editTextSearchPolicy.setOnClickListener(){
            Log.e("search info",contentList[binding.numberPickerSearchPolicy.value])
            binding.edittextSearchPolicy.setText(contentList[binding.numberPickerSearchPolicy.value])
            binding.numberPickerSearchPolicy.isVisible = !binding.numberPickerSearchPolicy.isVisible
            binding.buttonNumberPickerConfirm.isVisible = !binding.buttonNumberPickerConfirm.isVisible
            binding.datePickerSearchDate.isVisible = false
            binding.buttonDatePickerConfirm.isVisible = false
        }

        editTextSearchDate.setOnClickListener(){
            var dateSelected: String = binding.datePickerSearchDate.year.toString() + "-"
            dateSelected += if(binding.datePickerSearchDate.month<10){
                "0" + (binding.datePickerSearchDate.month+1).toString()
            } else{
                (binding.datePickerSearchDate.month+1).toString()
            }
            dateSelected += "-"
            dateSelected += if(binding.datePickerSearchDate.dayOfMonth<10){
                "0$binding.datePickerSearchDate.dayOfMonth"
            } else{
                binding.datePickerSearchDate.dayOfMonth.toString()
            }
            Log.e("search info",dateSelected)
            binding.edittextSearchDate.setText(dateSelected)
            binding.datePickerSearchDate.isVisible = !binding.datePickerSearchDate.isVisible
            binding.buttonDatePickerConfirm.isVisible = !binding.buttonDatePickerConfirm.isVisible
            binding.numberPickerSearchPolicy.isVisible = false
            binding.buttonNumberPickerConfirm.isVisible = false
        }

        numberPickerPolicySelection.setOnValueChangedListener { picker, oldVal, newVal ->
            Log.e("search info",picker.value.toString())
            binding.edittextSearchPolicy.setText(contentList[binding.numberPickerSearchPolicy.value])
        }

        datePickerDateSelection.setOnDateChangedListener { datePicker, year, monthOfYear, dayOfMonth ->
            var dateSelected: String = year.toString() + "-"
            dateSelected += if(monthOfYear<10){
                "0" + (monthOfYear+1).toString()
            } else{
                (monthOfYear+1).toString()
            }
            dateSelected += "-"
            dateSelected += if(dayOfMonth<10){
                "0$dayOfMonth"
            } else{
                dayOfMonth.toString()
            }
            binding.edittextSearchDate.setText(dateSelected)
            Log.e("search info",dateSelected)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}