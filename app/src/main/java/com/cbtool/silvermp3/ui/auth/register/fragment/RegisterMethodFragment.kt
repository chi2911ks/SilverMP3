package com.cbtool.silvermp3.ui.auth.register.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cbtool.silvermp3.MainActivity
import com.cbtool.silvermp3.data.state.LoginState
import com.cbtool.silvermp3.databinding.FragmentRegisterMethodBinding
import com.cbtool.silvermp3.ui.OnBoardingActivity
import com.cbtool.silvermp3.ui.auth.login.LoginActivity
import com.cbtool.silvermp3.ui.auth.login.fragment.EnterPhoneFragment
import com.cbtool.silvermp3.ui.auth.login.viewmodel.GoogleLoginViewModel
import com.cbtool.silvermp3.ui.auth.register.RegisterActivity
import com.cbtool.silvermp3.utils.startNewActivity
import org.koin.androidx.viewmodel.ext.android.getViewModel

class RegisterMethodFragment : Fragment() {
    private var _binding: FragmentRegisterMethodBinding? = null
    private val binding get() = _binding!!
    private val googleLoginViewModel: GoogleLoginViewModel by lazy { getViewModel<GoogleLoginViewModel>() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterMethodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        googleLoginViewModel.loginState.observe(requireActivity()) {
            when (it) {
                is LoginState.Error -> {}
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
        binding.emailRegisterBtn.setOnClickListener {
            navigateTo(EmailRegisterFragment())
        }
        binding.phoneRegisterBtn.setOnClickListener {
            navigateTo(EnterPhoneFragment())
        }
        binding.loginBtn.setOnClickListener {
            requireActivity().startNewActivity(LoginActivity::class.java, true)

        }
        binding.googleBtn.setOnClickListener {
            googleLoginViewModel.login()
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