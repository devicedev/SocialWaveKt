package com.devicedev.socialwavekt.ui.main.fragments.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.devicedev.socialwavekt.R
import com.devicedev.socialwavekt.ui.login.LoginActivity
import com.devicedev.socialwavekt.utils.APP_KEY
import com.devicedev.socialwavekt.utils.SharedPreferencesUtils
import com.devicedev.socialwavekt.utils.TokenUtils.TOKEN_KEY
import kotlinx.android.synthetic.main.settings_fragment.*

class SettingsFragment(val token: String) : Fragment() {


    private lateinit var viewModel: SettingsViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)

        logoutButton.setOnClickListener {

            SharedPreferencesUtils.save(context?.getSharedPreferences(APP_KEY,Context.MODE_PRIVATE), TOKEN_KEY, null)

            viewModel.logout()

            startLoginActivity()
        }
    }

    private fun startLoginActivity() {

        val intent = Intent(activity, LoginActivity::class.java)

        startActivity(intent)

        activity?.finish()

    }
}