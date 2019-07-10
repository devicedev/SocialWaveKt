package com.devicedev.socialwavekt.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.devicedev.socialwavekt.R
import com.devicedev.socialwavekt.ui.main.MainActivity
import com.devicedev.socialwavekt.ui.register.RegisterActivity
import com.devicedev.socialwavekt.utils.*
import com.devicedev.socialwavekt.utils.TokenUtils.TOKEN_KEY
import kotlinx.android.synthetic.main.activity_start.*

class LoginActivity : AppCompatActivity() {
    companion object {

        private const val TAG = "LoginActivity"

        const val SHOW_SPLASH_SCREEN = 1 * 1000L

    }

    private val constraintSetLogin = ConstraintSet()

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val token = SharedPreferencesUtils.get(getSharedPreferences(APP_KEY, MODE_PRIVATE), TOKEN_KEY)

        val isTokenValid = TokenUtils.isValid(token)

        if (!isTokenValid) {

            viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        }
        Handler().postDelayed({

            if (!isTokenValid) {
                initialize()

//                Prefill
                prefill()

                stopSplashScreen()

                setupProgressBar()

                setUpViewModel()

                setUpLoginButton()

                signUpButton.setOnClickListener {

                    startSignUpActivity()

                }

            } else {

                startMainActivity()

            }


        }, SHOW_SPLASH_SCREEN)


    }

    private fun prefill() {
        emailEditText.setText(TEST_EMAIL)
        passwordEditText.setText(TEST_PASSWORD)

    }

    private fun setupProgressBar() {
        viewModel.progressBar.observe(this, Observer {
            toggleProgressBar(it)
        })
    }

    private fun initialize() {
        constraintSetLogin.clone(this, R.layout.activity_start_login)

    }

    private fun setUpLoginButton() {
        loginButton.setOnClickListener {

            toggleProgressBar(View.VISIBLE)

            val email = emailEditText.text.toString()

            val password = passwordEditText.text.toString()

            val emailResult = Validator.Login.email(email)

            val passwordResult = Validator.Login.password(password)

            var success = true

            if (!emailResult.success) {

                emailEditText.error = getString(emailResult.message)

                success = false
            }
            if (!passwordResult.success) {

                passwordEditText.error = getString(passwordResult.message)

                success = false


            }
            if (!success) {

                toggleProgressBar(View.INVISIBLE)

                return@setOnClickListener
            }

            viewModel.login(email, password)


        }

    }

    private fun setUpViewModel() {

        viewModel.loginResponse.observe(this, Observer {
            it?.let {

                toggleProgressBar(View.INVISIBLE)

                SharedPreferencesUtils.save(getSharedPreferences(APP_KEY, MODE_PRIVATE), TOKEN_KEY, it.token)


                startMainActivity()
            }

        })

    }


    private fun stopSplashScreen() {
        TransitionManager.beginDelayedTransition(rootLayout)
        constraintSetLogin.applyTo(rootLayout)

    }

    private fun startMainActivity() {

        val intent = Intent(this, MainActivity::class.java)

        startActivity(intent)

        finish()

    }

    private fun startSignUpActivity() {
        val intent = Intent(this, RegisterActivity::class.java)

        startActivity(intent)


    }

    private fun toggleProgressBar(visibility: Int) {

        progressBar.visibility = visibility

        loginButton.isEnabled = visibility != View.VISIBLE

    }

}
