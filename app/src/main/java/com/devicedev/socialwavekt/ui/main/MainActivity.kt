package com.devicedev.socialwavekt.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.devicedev.socialwavekt.R
import com.devicedev.socialwavekt.ui.login.LoginActivity
import com.devicedev.socialwavekt.ui.main.fragments.FindFriendsFragment
import com.devicedev.socialwavekt.ui.main.fragments.HomeFragment
import com.devicedev.socialwavekt.ui.main.fragments.profile.ProfileFragment
import com.devicedev.socialwavekt.ui.main.fragments.settings.SettingsFragment
import com.devicedev.socialwavekt.utils.APP_KEY
import com.devicedev.socialwavekt.utils.SharedPreferencesUtils
import com.devicedev.socialwavekt.utils.TokenUtils
import com.devicedev.socialwavekt.utils.TokenUtils.TOKEN_KEY
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.progressBar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val token = SharedPreferencesUtils.get(getSharedPreferences(APP_KEY, MODE_PRIVATE), TOKEN_KEY)

        if (!TokenUtils.isValid(token)) {
            startLoginActivity()
        }
        Log.d("asdads","onCreate: $token")
        token!!

        if (savedInstanceState == null) {
            changeFragment(HomeFragment(token))
        }


        setUpBottomNav(token)


    }

    private fun setUpBottomNav(token: String) {
        bottomNavigationView.setOnNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.nav_home -> changeFragment(HomeFragment(token))
                R.id.nav_find_friends -> changeFragment(FindFriendsFragment(token))
                R.id.nav_profile -> changeFragment(ProfileFragment(token))
                R.id.nav_settings -> changeFragment(SettingsFragment(token))

            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragmentLayout, fragment).commit()
    }

    private fun startLoginActivity() {

        val intent = Intent(this, LoginActivity::class.java)

        startActivity(intent)

        finish()

    }

    fun toggleProgressBar(visibility: Int) {

        progressBar.visibility = visibility

        fragmentLayout.visibility = if (visibility == View.VISIBLE) View.INVISIBLE else View.VISIBLE

    }
}
