package com.cbtool.silvermp3.ui.auth.register.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.cbtool.silvermp3.MainActivity
import com.cbtool.silvermp3.data.state.LoginState
import com.cbtool.silvermp3.databinding.FragmentEnterPasswordBinding
import com.cbtool.silvermp3.ui.auth.register.viewmodel.EmailRegisterViewModel
import com.cbtool.silvermp3.utils.startNewActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class EnterPasswordFragment : Fragment() {
    private var _binding: FragmentEnterPasswordBinding? = null
    private val binding get() = _binding!!
    private var email: String? = null

    private val emailRegisterViewModel: EmailRegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            email = it.getString(EMAIL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEnterPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emailRegisterViewModel.loginState.observe(viewLifecycleOwner) {
            when (it) {
                is LoginState.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }

                LoginState.Idle -> {}
                LoginState.Loading -> {}
                is LoginState.Success -> {

                    Toast.makeText(requireContext(), "Đăng ký thành công!", Toast.LENGTH_SHORT)
                        .show()
                    requireActivity().startNewActivity(MainActivity::class.java, true)
                }

            }

        }
        binding.backBtn.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.nextBtn.setOnClickListener {
            emailRegisterViewModel.register(email!!, binding.passwordInput.text.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val EMAIL = "EMAIL"

        @JvmStatic
        fun newInstance(email: String) =
            EnterPasswordFragment().apply {
                arguments = Bundle().apply {
                    putString(EMAIL, email)
                }
            }
    }
}