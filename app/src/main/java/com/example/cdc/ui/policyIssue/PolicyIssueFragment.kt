package com.example.cdc.ui.policyIssue

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.DatePicker.OnDateChangedListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cdc.databinding.FragmentPolicyIssueBinding
import com.example.cdc.ui.dataProcessing.ViewQuestionnaireListActivity
import java.util.*

class PolicyIssueFragment : Fragment() {

  private var _binding: FragmentPolicyIssueBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    //val notificationsViewModel =
      //      ViewModelProvider(this).get(NotificationsViewModel::class.java)

    _binding = FragmentPolicyIssueBinding.inflate(inflater, container, false)
    val policyDatePick : DatePicker = binding.PolicyDatePicker
    val today = Calendar.getInstance()
    policyDatePick.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH)){ view, year, month, day ->
        val month = month + 1
        val msg = "$year-$month-$day"
        val intent: Intent = Intent(activity, MotifyPolicyInfoActivity::class.java)
        val bundle: Bundle =Bundle()
        bundle.putString("date",msg)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    val root: View = binding.root


    return root
  }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}