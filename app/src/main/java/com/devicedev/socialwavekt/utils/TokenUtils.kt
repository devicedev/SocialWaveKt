package com.devicedev.socialwavekt.utils

import android.util.Log
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject

object TokenUtils {

    const val TOKEN_KEY = "AUTH_TOKEN_KEY"

    fun isValid(token: String?): Boolean {

        if (token == null) {
            return false
        }

        try {
            val exp = Gson().fromJson(JWTUtils.get(token, 1),TokenPart::class.java).exp

            val now = System.currentTimeMillis() / 1000L

            if (exp <= now)
                return false

        } catch (e: JSONException) {

            e.printStackTrace()

            return false
        }


        return true

    }

    data class TokenPart(
        val exp: Long
    )

}