package com.cbtool.silvermp3.ui.auth.login.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.cbtool.silvermp3.MainActivity
import com.cbtool.silvermp3.ui.auth.PhoneAuthState
import com.cbtool.silvermp3.databinding.FragmentEnterCodeBinding
import com.cbtool.silvermp3.ui.auth.login.viewmodel.PhoneAuthViewModel
import com.cbtool.silvermp3.ui.custom.LoadingDialog
import com.cbtool.silvermp3.utils.startNewActivity
import org.koin.androidx.viewmodel.ext.android.getActivityViewModel


class EnterCodeFragment : Fragment() {
    private lateinit var numberPhone: String
    private var _binding: FragmentEnterCodeBinding? = null
    private val binding get() = _binding!!
    private val phoneAuthViewModel by lazy { getActivityViewModel<PhoneAuthViewModel>() }
    private lateinit var listEditTextOtp: List<EditText>
    private val loadingDialog by lazy { LoadingDialog(requireContext()) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            numberPhone = it.getString(ARG_NUMBER_PHONE).toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEnterCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ABC", "phoneAuthViewModel: $phoneAuthViewModel")
        phoneAuthViewModel.authState.observe(viewLifecycleOwner) {
            when (it) {
                is PhoneAuthState.CodeSent -> {
                    loadingDialog.dismiss()
                    startResendTimer()
                }

                is PhoneAuthState.Loading -> {
                    loadingDialog.show()
                }

                is PhoneAuthState.Verified -> {
                    loadingDialog.dismiss()
                    phoneAuthViewModel.addUser()
                    Toast.makeText(requireContext(), "Xác nhận thành công!", Toast.LENGTH_SHORT)
                        .show()
                    requireActivity().startNewActivity(MainActivity::class.java, true)
                }

                is PhoneAuthState.Error -> {
                    loadingDialog.dismiss()
                }

                PhoneAuthState.Idle -> {}
            }
        }
        binding.textView5.text = "Chúng tôi đã gửi một mã gồm 6 chữ số tới\n" + numberPhone
        binding.backBtn.setOnClickListener { requireActivity().supportFragmentManager.popBackStack() }
        binding.nextBtn.setOnClickListener {
            val code = otpInput()
            if (code.length != 6) {
                Toast.makeText(requireContext(), "Vui lòng nhập mã 6 chữ số!", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            phoneAuthViewModel.verifyCode(code)
        }
        binding.tvResend.setOnClickListener {
            phoneAuthViewModel.verifyPhoneNumber(numberPhone, requireActivity())
        }
        startResendTimer()
        listEditTextOtp = listOf(
            binding.et1,
            binding.et2,
            binding.et3,
            binding.et4,
            binding.et5,
            binding.et6
        )
        addOtpChangeListener()
    }

    private fun addOtpChangeListener() {
        for (i in listEditTextOtp.indices) {
            listEditTextOtp[i].addTextChangedListener { text ->
                if (text.toString().isNotEmpty()) {
                    if (i < listEditTextOtp.size - 1) {
                        listEditTextOtp[i + 1].requestFocus()
                    }
                } else {
                    if (i > 0) {
                        listEditTextOtp[i - 1].requestFocus()
                    }
                }
            }
        }
    }

    private fun otpInput(): String {
        val code = StringBuilder().apply {
            for (et in listEditTextOtp) {
                append(et.text)
            }
        }.toString()
        return code.trim()
    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    private fun startResendTimer() {
        binding.tvResend.isEnabled = false
        object : CountDownTimer(30000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val s = millisUntilFinished / 1000
                binding.tvResend.text = "Gửi lại mã ($s s)"
            }

            override fun onFinish() {
                binding.tvResend.text = "Gửi lại mã"
                binding.tvResend.isEnabled = true
            }
        }.start()
    }

    companion object {
        private const val ARG_NUMBER_PHONE = "number_phone"

        @JvmStatic
        fun newInstance(number_phone: String) =
            EnterCodeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_NUMBER_PHONE, number_phone)
                }
            }
    }
}