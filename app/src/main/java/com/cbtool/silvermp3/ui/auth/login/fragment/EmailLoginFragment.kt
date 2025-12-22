package com.cbtool.silvermp3.ui.auth.login.fragment

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.cbtool.silvermp3.MainActivity
import com.cbtool.silvermp3.R
import com.cbtool.silvermp3.data.state.LoginState
import com.cbtool.silvermp3.databinding.FragmentEmailLoginBinding
import com.cbtool.silvermp3.databinding.FragmentEnterPhoneBinding
import com.cbtool.silvermp3.ui.auth.login.LoginActivity
import com.cbtool.silvermp3.ui.auth.login.viewmodel.EmailLoginViewModel
import com.cbtool.silvermp3.ui.custom.LoadingDialog
import com.cbtool.silvermp3.utils.startNewActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue


class EmailLoginFragment : Fragment() {

    private var _binding: FragmentEmailLoginBinding?=null
    private val binding get() = _binding!!
    private val emailLoginViewModel: EmailLoginViewModel by viewModel()

    private val loadingDialog by lazy { LoadingDialog(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEmailLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.passwordInput.error = "Vui lòng nhập mật khẩu!"
        emailLoginViewModel.loginState.observe(requireActivity()) {
            when (it) {
                is LoginState.Error -> {
                    loadingDialog.dismiss()
                    binding.inputLayoutEmail.error = "1"
                    binding.inputLayoutPassword.error = it.message
                }
                LoginState.Idle -> {}
                LoginState.Loading -> {
                    loadingDialog.show()
                }
                is LoginState.Success -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                    requireActivity().startNewActivity(MainActivity::class.java, true)
                }

            }
        }
        binding.backBtn.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.loginBtn.setOnClickListener{
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()
            if (!isValid(email, password)) return@setOnClickListener
            emailLoginViewModel.login(email, password)
        }
        binding.forgotPasswordBtn.setOnClickListener {
            (activity as LoginActivity).navigateTo(ForgotPasswordFragment(), true)
        }
        binding.passwordInput.addTextChangedListener {
            if (it.toString().isEmpty()) {
                binding.inputLayoutPassword.error = "Vui lòng nhập mật khẩu!"
            } else {
                binding.inputLayoutPassword.error = null
            }
        }
        binding.emailInput.addTextChangedListener {
            if (it.toString().isEmpty()) {
                binding.inputLayoutEmail.error = "Vui lòng nhập email!"
            } else {
                binding.inputLayoutEmail.error = null
            }
        }
    }
    private fun isValid(email: String, password: String): Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.inputLayoutEmail.error = "Vui lòng nhập email đúng định dạng!"
//            Toast.makeText(requireContext(), "Vui lòng nhập email hoặc email chưa đúng định dạng!", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.isEmpty()) {
            binding.inputLayoutPassword.error = "Vui lòng nhập mật khẩu!"
            return false
        }
        return true
    }
}