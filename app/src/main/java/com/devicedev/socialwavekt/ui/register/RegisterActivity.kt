package com.devicedev.socialwavekt.ui.register

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.devicedev.socialwavekt.R
import com.devicedev.socialwavekt.ui.main.MainActivity
import com.devicedev.socialwavekt.utils.*
import com.devicedev.socialwavekt.utils.TokenUtils.TOKEN_KEY
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.header.*
import java.util.*


class RegisterActivity : AppCompatActivity() {

    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        viewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)

        setUpViewModel()

        setUpBirthday()

        setUpProgressBar()

        setUpRegisterButton()

//        Prefill
        prefill()

        backButton.setOnClickListener {
            onBackPressed()
        }


    }

    private fun prefill() {

        nameEditText.setText(TEST_NAME)
        emailEditText.setText(TEST_EMAIL)
        passwordEditText.setText(TEST_PASSWORD)
        maleRadioButton.isChecked = true
        birthdayTextView.text = TEST_BIRTHDAY


    }

    private fun setUpBirthday() {

        birthdayTextView.setOnClickListener {
            val calendar = Calendar.getInstance()

            calendar.add(Calendar.YEAR, -resources.getInteger(R.integer.min_age))

            val year = calendar.get(Calendar.YEAR)

            val month = calendar.get(Calendar.MONTH) + 1

            val day = calendar.get(Calendar.DAY_OF_MONTH)


            val datePicker = DatePickerDialog(
                this,
                R.style.DatePickerDialogApp,
                DatePickerDialog.OnDateSetListener { view, year, month, day ->

                    birthdayTextView.text = String.format("%04d-%02d-%02d", year, month, day)

                }, year, month, day
            )
            datePicker.show()


        }


    }

    private fun setUpProgressBar() {
        viewModel.progressBar.observe(this, Observer {
            toggleProgressBar(it)
        })
    }

    private fun setUpViewModel() {
        viewModel.registerResponse.observe(this, Observer {
            it?.let {
                toggleProgressBar(View.INVISIBLE)

                SharedPreferencesUtils.save(getSharedPreferences(APP_KEY, MODE_PRIVATE), TOKEN_KEY, it.token)

                startMainActivity()
            }
        })

    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)

        startActivity(intent)

        finish()
    }

    private fun setUpRegisterButton() {

        signUpButton.setOnClickListener {

            toggleProgressBar(View.VISIBLE)

            val name = nameEditText.text.toString().trim()

            val email = emailEditText.text.toString().trim()

            val password = passwordEditText.text.toString().trim()

            val birthday = birthdayTextView.text.toString().trim()

            val gender = if (maleRadioButton.isChecked && !femaleRadioButton.isChecked) {

                "male"

            } else if (femaleRadioButton.isChecked && !maleRadioButton.isChecked) {

                "female"

            } else {

                null

            }

            var success = true

            val nameResult = Validator.Register.name(name)

            val emailResult = Validator.Login.email(email)

            val passwordResult = Validator.Register.password(password)

            val birthdayResult = Validator.Register.birthday(birthday, resources.getInteger(R.integer.min_age))

            val genderResult = Validator.Register.gender(gender)

            if (!nameResult.success) {

                nameEditText.error = getString(nameResult.message)

                success = false
            }

            if (!emailResult.success) {

                emailEditText.error = getString(emailResult.message)

                success = false
            }
            if (!passwordResult.success) {

                passwordEditText.error = getString(passwordResult.message, passwordResult.arg)

                success = false

            }
            if (!genderResult.success) {

                onError(getString(genderResult.message))

                success = false

            }
            if (!birthdayResult.success) {

                onError(getString(birthdayResult.message, birthdayResult.arg))

                success = false

            }

            if (!success) {

                toggleProgressBar(View.INVISIBLE)

                return@setOnClickListener
            }

            viewModel.register(name, email, password, gender!!, birthday)
        }


    }

    private fun toggleProgressBar(visibility: Int) {

        progressBar.visibility = visibility

        signUpButton.isEnabled = visibility != View.VISIBLE

    }
}
