package com.devicedev.socialwavekt.utils

import android.content.Context
import android.widget.Toast
import es.dmoral.toasty.Toasty

fun Context.onSuccess(resourceId: Int, length: Int = Toast.LENGTH_SHORT) {
    Toasty.success(this, getString(resourceId), length).show()
}
fun Context.onError(resourceId: Int, length: Int = Toast.LENGTH_SHORT) {
    Toasty.error(this, getString(resourceId), length).show()
}

fun Context.onSuccess(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toasty.success(this, message, length).show()
}
fun Context.onError(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toasty.error(this, message, length).show()
}
