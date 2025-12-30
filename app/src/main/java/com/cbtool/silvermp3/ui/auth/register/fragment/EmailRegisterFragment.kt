package com.cbtool.silvermp3.ui.auth.register.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cbtool.silvermp3.databinding.FragmentEmailRegisterBinding
import com.cbtool.silvermp3.ui.auth.register.RegisterActivity

class EmailRegisterFragment : Fragment() {
    private var _binding: FragmentEmailRegisterBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEmailRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backBtn.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()

        }
        binding.nextBtn.setOnClickListener {
            val emailStr = binding.emailInput.text.toString()

            navigateTo(EnterPasswordFragment.newInstance(emailStr))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun navigateTo(fragment: Fragment) {
        (activity as RegisterActivity).navigateTo(fragment, true)

    }
}