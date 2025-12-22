package com.cbtool.silvermp3.ui.auth.login.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.cbtool.silvermp3.R
import com.cbtool.silvermp3.data.state.PhoneAuthState
import com.cbtool.silvermp3.databinding.FragmentEmailRegisterBinding
import com.cbtool.silvermp3.databinding.FragmentEnterPhoneBinding
import com.cbtool.silvermp3.ui.auth.login.LoginActivity
import com.cbtool.silvermp3.ui.auth.login.viewmodel.PhoneAuthViewModel
import com.cbtool.silvermp3.ui.auth.register.RegisterActivity
import com.cbtool.silvermp3.ui.custom.LoadingDialog
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue
import kotlin.toString

class EnterPhoneFragment : Fragment() {
    private var _binding: FragmentEnterPhoneBinding?=null
    private val binding get() = _binding!!
    private var countryCode: String = "+84"
    private var phoneNumber: String = ""
    private val phoneAuthViewModel: PhoneAuthViewModel by activityViewModel()
    private var isSentCode = false
    private val loadingDialog by lazy { LoadingDialog(requireContext()) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEnterPhoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ABC", "phoneAuthViewModel: $phoneAuthViewModel")
        phoneAuthViewModel.authState.observe(viewLifecycleOwner) {
            when(it){
                is PhoneAuthState.CodeSent -> {
                    loadingDialog.dismiss()
                    if (isSentCode) return@observe
                    isSentCode = true
                    if (requireActivity() is LoginActivity) {
                        (activity as LoginActivity).navigateTo(EnterCodeFragment.newInstance(phoneNumber), true)
                    }
                    else{
                        (activity as RegisterActivity).navigateTo(EnterCodeFragment.newInstance(phoneNumber), true)
                    }
                }
                is PhoneAuthState.Error -> {
                    loadingDialog.dismiss()
                    binding.inputLayoutPhoneNumber.error = it.message
                }
                is PhoneAuthState.Loading -> {
                    loadingDialog.show()
                }

                else -> {}
            }
        }
        binding.backBtn.setOnClickListener { requireActivity().supportFragmentManager.popBackStack() }
        binding.nextBtn.setOnClickListener {
            isSentCode = false
            phoneNumber = binding.phoneInput.text.toString()
            if (!isValid(phoneNumber)) return@setOnClickListener
            phoneNumber = countryCode +  if (phoneNumber.startsWith("0")) phoneNumber.substring(1) else phoneNumber
            phoneAuthViewModel.verifyPhoneNumber(phoneNumber, requireActivity())

        }
        parentFragmentManager.setFragmentResultListener("countryRequestKey", this) { _, bundle ->
            val countryName = bundle.getString("name")
            countryCode = bundle.getString("code").toString()
            binding.selectCountryTv.text = countryCode
            binding.selectCountryBtn.text = "$countryName"
        }
        binding.selectCountryBtn.setOnClickListener {
            val dialog = CountryDialogFragment()
            dialog.show(parentFragmentManager, "CountryDialogFragment")
        }
        binding.phoneInput.addTextChangedListener{
                text -> binding.nextBtn.isEnabled = !text.toString().isEmpty()
        }
    }
    private fun isValid(phone: String): Boolean {
        if (phone.isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng nhập số điện thoại!", Toast.LENGTH_SHORT).show()
            return false
        }
        return true

    }
}