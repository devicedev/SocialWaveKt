package com.devicedev.socialwavekt.data.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Entity(tableName = "users")
data class User(

    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    val name: String,

    val email: String,

    val password: String,

    val gender: String,

    val birthday: String,

    @SerializedName("profile")
    val image: String

) {

    fun getFormatedBirthday(): String? {
        var birthday: String? = null
        try {

            birthday = SimpleDateFormat("yyyy-MM-dd").format(
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(this.birthday)
            )
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return birthday

    }

}