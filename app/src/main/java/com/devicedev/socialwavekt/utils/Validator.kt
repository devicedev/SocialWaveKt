package com.devicedev.socialwavekt.utils

import android.util.Patterns
import com.devicedev.socialwavekt.R
import java.text.SimpleDateFormat
import java.util.*


object Validator {


    object Login {

        fun email(email: String): Result = when {
            email.isEmpty() -> Result(R.string.val_error_email_required)
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> Result(R.string.val_error_email_valid)
            else -> Result(success = true, message = R.string.success)
        }

        fun password(password: String): Result = when {
            password.isEmpty() -> Result(R.string.val_error_password_required)
            else -> Result(success = true, message = R.string.success)
        }


    }

    object Register {

        fun name(name: String): Result = when {
            name.isEmpty() -> Result(R.string.val_error_name_required)
            else -> Result(success = true, message = R.string.success)
        }

        fun password(password: String): Result = when {
            password.isEmpty() -> Result(R.string.val_error_password_required)
            !PasswordPatterns.DIGIT.matcher(password).find() -> Result(R.string.val_error_password_digit)
            !PasswordPatterns.LOWER_CASE.matcher(password).find() -> Result(R.string.val_error_password_lower)
            !PasswordPatterns.UPPER_CASE.matcher(password).find() -> Result(R.string.val_error_password_upper)
            !PasswordPatterns.MIN.matcher(password).find() -> Result(
                R.string.val_error_password_min,
                false,
                PasswordPatterns.MIN_CHARACTERS
            )
            !PasswordPatterns.MAX.matcher(password).find() -> Result(
                R.string.val_error_password_max,
                false,
                PasswordPatterns.MAX_CHARACTERS
            )
            else -> Result(success = true, message = R.string.success)
        }

        fun gender(gender: String?): Result = when (gender) {
            null -> Result(R.string.val_error_gender_required)
            else -> Result(success = true, message = R.string.success)
        }

        fun birthday(birthday: String, minAge: Int): Result = when {
            birthday.isEmpty() or (birthday == "Enter birthday...") -> Result(R.string.val_error_birthday_required)
            run {

                val calendar = Calendar.getInstance()

                calendar.add(Calendar.YEAR, -minAge)

                SimpleDateFormat("yyyy-MM-dd").parse(birthday).after(calendar.time)

            } -> Result(R.string.val_error_birthday_min, false, minAge)
            else -> Result(success = true, message = R.string.success)

        }

    }

    data class Result(
        val message: Int,
        val success: Boolean = false,
        val arg: Int? = null
    )

}