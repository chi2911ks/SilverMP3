package com.cbtool.silvermp3.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import com.cbtool.silvermp3.R
import com.cbtool.silvermp3.databinding.FragmentProfileBinding
import com.cbtool.silvermp3.utils.glideCustom
import org.koin.androidx.viewmodel.ext.android.viewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel: ProfileViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }
    fun init() {
        profileViewModel.getCurrentUser()
        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        profileViewModel.user.observe(viewLifecycleOwner) { it->
            binding.nameTv.text = it.name.ifEmpty { "Không có tên" }
            binding.emailTv.text = it.email.ifEmpty { it.numberPhone }
            if (it.avatarURL != null && it.avatarURL != "" && it.avatarURL != "null") {
                glideCustom(requireContext(), binding.avatarImg, it.avatarURL)
            }

        }
        binding.logoutBtn.setOnClickListener {
            profileViewModel.logout()
        }
        binding.deleteAccountBtn.setOnClickListener {
            profileViewModel.deleteUser()
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    companion object {
        @JvmStatic
        fun newInstance() =
            ProfileFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}