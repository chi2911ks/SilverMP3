package com.cbtool.silvermp3.ui.auth.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.cbtool.silvermp3.databinding.ActivityLoginBinding
import com.cbtool.silvermp3.ui.auth.login.fragment.LoginMethodFragment
import com.cbtool.silvermp3.utils.navigateTo

class LoginActivity : AppCompatActivity() {
    val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            navigateTo(LoginMethodFragment())
        }
    }
    fun navigateTo(fragment: Fragment, addToBackStack: Boolean = false) = supportFragmentManager.navigateTo(binding.fragmentContainer.id, fragment, addToBackStack)
    override fun onDestroy() {
        super.onDestroy()
    }
}