package com.example.cdc.ui.dataProcessing

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cdc.databinding.FragmentDataProcessingBinding

class DataProcessingFragment : Fragment() {

    private var _binding: FragmentDataProcessingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DataProcessingViewModel::class.java)

        _binding = FragmentDataProcessingBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val buttonViewQuestionnaireList: Button = binding.ViewQuestionnaireListButton
        buttonViewQuestionnaireList.setOnClickListener {
            val intent: Intent = Intent(activity, ViewQuestionnaireListActivity::class.java)
            startActivity(intent)
        }
        val buttonChangeQuestionnaireContent: Button = binding.ChangeQuestionnaireContentButton

        buttonChangeQuestionnaireContent.setOnClickListener {
            startActivity(Intent(activity, ChangeQuestionnaireContentActivity::class.java))
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}