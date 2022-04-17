package com.example.cdc.ui.policyIssue

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cdc.databinding.FragmentPolicyIssueBinding

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
    val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

    _binding = FragmentPolicyIssueBinding.inflate(inflater, container, false)
    val root: View = binding.root


    return root
  }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}