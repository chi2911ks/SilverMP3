package com.cbtool.silvermp3.ui.auth.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.cbtool.silvermp3.databinding.ActivityRegisterBinding
import com.cbtool.silvermp3.ui.auth.register.fragment.RegisterMethodFragment
import com.cbtool.silvermp3.utils.navigateTo

class RegisterActivity : AppCompatActivity() {
    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            navigateTo(RegisterMethodFragment())
        }
    }

    fun navigateTo(fragment: Fragment, addToBackStack: Boolean = false) =
        supportFragmentManager.navigateTo(binding.fragmentContainer.id, fragment, addToBackStack)

}