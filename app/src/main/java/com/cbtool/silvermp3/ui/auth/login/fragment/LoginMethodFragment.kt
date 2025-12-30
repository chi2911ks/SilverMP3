package com.cbtool.silvermp3.ui.auth.login.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cbtool.silvermp3.MainActivity
import com.cbtool.silvermp3.ui.auth.LoginState
import com.cbtool.silvermp3.databinding.FragmentLoginMethodBinding
import com.cbtool.silvermp3.ui.OnBoardingActivity
import com.cbtool.silvermp3.ui.auth.login.LoginActivity
import com.cbtool.silvermp3.ui.auth.login.viewmodel.GoogleLoginViewModel
import com.cbtool.silvermp3.ui.auth.register.RegisterActivity
import com.cbtool.silvermp3.utils.startNewActivity

import org.koin.androidx.viewmodel.ext.android.getViewModel


class LoginMethodFragment : Fragment() {
    private var _binding: FragmentLoginMethodBinding? = null
    private val binding get() = _binding!!
    private val googleLoginViewModel: GoogleLoginViewModel by lazy { getViewModel<GoogleLoginViewModel>() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginMethodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        googleLoginViewModel.loginState.observe(requireActivity()) {
            when (it) {
                is LoginState.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }

                LoginState.Idle -> {}
                LoginState.Loading -> {}
                is LoginState.Success -> {
                    Toast.makeText(requireContext(), "Đăng nhập thành công!", Toast.LENGTH_SHORT)
                        .show()
                    requireActivity().startNewActivity(MainActivity::class.java, true)
                }

            }
        }
        binding.backBtn.setOnClickListener {
            requireActivity().startNewActivity(OnBoardingActivity::class.java, true)

        }
        binding.emailLoginBtn.setOnClickListener {
            navigateTo(EmailLoginFragment())
        }
        binding.phoneLoginBtn.setOnClickListener {
            navigateTo(EnterPhoneFragment())
        }
        binding.googleLoginBtn.setOnClickListener {
            googleLoginViewModel.login()
        }
        binding.registerBtn.setOnClickListener {
            requireActivity().startNewActivity(RegisterActivity::class.java, true)
        }


    }

    fun navigateTo(fragment: Fragment) {
        (activity as LoginActivity).navigateTo(fragment, true)
    }

}