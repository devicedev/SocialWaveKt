package com.devicedev.socialwavekt.ui.register

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devicedev.socialwavekt.R
import com.devicedev.socialwavekt.data.retrofit.ServiceGenerator
import com.devicedev.socialwavekt.data.retrofit.responses.ErrorMessage
import com.devicedev.socialwavekt.data.retrofit.responses.UserTokenResponse
import com.devicedev.socialwavekt.data.retrofit.services.UserService
import com.devicedev.socialwavekt.data.room.SocialWaveDatabase
import com.devicedev.socialwavekt.utils.onError
import com.devicedev.socialwavekt.utils.onSuccess
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext

    private val repository = RegisterRepository(
        SocialWaveDatabase.getDatabase(context, viewModelScope).userDao(),
        ServiceGenerator.create(UserService::class.java)
    )



    private val _progressBar = MutableLiveData<Int>()

    val progressBar: LiveData<Int> = _progressBar


    private val _registerResponse = MutableLiveData<UserTokenResponse>()

    val registerResponse: LiveData<UserTokenResponse> = _registerResponse


    fun register(name: String, email: String, password: String, gender: String, birthday: String) {

        viewModelScope.launch {

            try {
                val response = withContext(Dispatchers.IO) {
                    repository.register(name, email, password, gender, birthday)
                }
                if (!response.isSuccessful) {

                    context.onError(
                        Gson().fromJson(response.errorBody()?.string(), ErrorMessage::class.java).message
                    )
                    _progressBar.value = View.INVISIBLE

                    return@launch
                }

                val userTokenResponse = response.body()

                withContext(Dispatchers.IO) {

                    repository.store(userTokenResponse!!.user)

                }

                context.onSuccess(R.string.successful_register)

                _registerResponse.value = userTokenResponse

            } catch (e: IOException) {

                context.onError(R.string.err_no_network)

            } catch (e: HttpException) {

                e.printStackTrace()

            } catch (e: Exception) {

                e.printStackTrace()

            } finally {

                _progressBar.value = View.INVISIBLE

            }


        }

    }


}