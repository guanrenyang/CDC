package com.example.cdc.ui.searchInfo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.cdc.databinding.FragmentSearchInfoBinding



class SearchInfoFragment : Fragment() {

    private var _binding: FragmentSearchInfoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val contentList = arrayOf<String>("尝试", "或许", "也许")

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

        val numberPickerPolicySelection: NumberPicker = binding.numberPickerSearchPolicy
        numberPickerPolicySelection.minValue = 0
        numberPickerPolicySelection.maxValue = contentList.size - 1
        binding.numberPickerSearchPolicy.isVisible = false
        numberPickerPolicySelection.displayedValues = contentList
        numberPickerPolicySelection.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        val buttonSearchPolicy: Button = binding.buttonSearchPolicy


        buttonSearchPolicy.setOnClickListener(){
            Log.e("search info","Search Policy Yes")
            val intent: Intent = Intent(activity, PolicyInfo::class.java)
            startActivity(intent)
        }

        editTextSearchPolicy.setOnClickListener(){
            Log.e("search info",contentList[binding.numberPickerSearchPolicy.value])
            binding.edittextSearchPolicy.setText(contentList[binding.numberPickerSearchPolicy.value])
            binding.numberPickerSearchPolicy.isVisible = !binding.numberPickerSearchPolicy.isVisible
        }

        numberPickerPolicySelection.setOnValueChangedListener { picker, oldVal, newVal ->
            Log.e("search info",picker.value.toString())
            binding.edittextSearchPolicy.setText(contentList[binding.numberPickerSearchPolicy.value])
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}