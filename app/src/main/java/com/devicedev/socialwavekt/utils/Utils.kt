package com.devicedev.socialwavekt.utils

import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.net.Uri
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.devicedev.socialwavekt.utils.TokenUtils.TOKEN_KEY
import okhttp3.MediaType
import okhttp3.RequestBody
import java.util.regex.Pattern
import androidx.room.util.CursorUtil.getColumnIndexOrThrow
import java.net.URISyntaxException


const val APP_KEY = "SOCIAL_WAVE"

const val TEST_NAME = "Radu Mihai"
const val TEST_EMAIL = "devicedem@gmail.com"
const val TEST_PASSWORD = "Vica2001"
const val TEST_BIRTHDAY = "2001-11-17"


object SharedPreferencesUtils {

    fun save(sharedPreferences: SharedPreferences?, key: String, value: String?) {
        sharedPreferences?.let {
            it.edit()
                .putString(key, value)
                .apply()
        }

    }

    fun get(sharedPreferences: SharedPreferences?, key: String): String? {
        return sharedPreferences?.getString(key, null)

    }
}

object PasswordPatterns {

    const val MIN_CHARACTERS = 8

    const val MAX_CHARACTERS = 20

    //    At least 1 digit
    val DIGIT = Pattern.compile("[0-9]")

    //At least 1 upper case character
    val UPPER_CASE = Pattern.compile("[A-Z]")

    //    At least 1 lower case character
    val LOWER_CASE = Pattern.compile("[a-z]")

    //    At least MIN_CHARACTERS characters
    val MIN = Pattern.compile(
        "^" +
                ".{" + MIN_CHARACTERS + ",}" +
                "$"
    )
    //    Max MAX_CHARACTERS characters
    val MAX = Pattern.compile(
        "^" +
                ".{1," + MAX_CHARACTERS + "}" +
                "$"
    )
}

@GlideModule
class AppGlideModule : AppGlideModule()


const val MULTIPART_MEDIA_TYPE = "multipart/form-data"

const val IMAGE_MEDIA_TYPE = "image/*"

fun createRequestBody(s: String, type: String? = MULTIPART_MEDIA_TYPE): RequestBody {
    return RequestBody.create(
        MediaType.parse(type), s
    )
}

fun getPathFromURI(context: Context, uri: Uri): String? =
    if ("content".equals(uri.scheme, ignoreCase = true)) {
        val cursor: Cursor?

        var path: String? = null

        try {
            cursor = context.contentResolver.query(uri, arrayOf("_data"), null, null, null)
            val columnIndex = cursor!!.getColumnIndexOrThrow("_data")
            if (cursor.moveToFirst()) {
                path = cursor.getString(columnIndex)
            }
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        path

    } else if ("file".equals(uri.scheme, ignoreCase = true)) {
        uri.path
    } else {
        null
    }


