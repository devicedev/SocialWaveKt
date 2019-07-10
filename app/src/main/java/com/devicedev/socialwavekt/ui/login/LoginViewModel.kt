package com.devicedev.socialwavekt.ui.login

import android.app.Application
import android.content.Context
import android.util.Log
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
import com.devicedev.socialwavekt.data.room.entities.User
import com.devicedev.socialwavekt.utils.onError
import com.devicedev.socialwavekt.utils.onSuccess
import com.google.gson.Gson
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.lang.Exception

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        private const val TAG = "LoginViewModel"
    }

    private val context: Context = application.applicationContext

    private val repository = LoginRepository(
        SocialWaveDatabase.getDatabase(context, viewModelScope).userDao(),
        ServiceGenerator.create(UserService::class.java)
    )

    private val _progressBar = MutableLiveData<Int>()

    val progressBar: LiveData<Int> = _progressBar


    private val _loginResponse = MutableLiveData<UserTokenResponse>()

    val loginResponse: LiveData<UserTokenResponse> = _loginResponse


    fun login(email: String, password: String) {

        viewModelScope.launch {

            try {
                val response = withContext(Dispatchers.IO) {
                    repository.login(email, password)
                }
                if (!response.isSuccessful) {

                    context.onError(
                        Gson().fromJson(response.errorBody()?.string(), ErrorMessage::class.java).message
                    )
                    _progressBar.value = View.INVISIBLE

                    return@launch
                }

                runBlocking(Dispatchers.IO){
                    repository.delete()
                }

                val userTokenResponse = response.body()

                withContext(Dispatchers.IO) {

                    repository.store(userTokenResponse!!.user)

                }

                context.onSuccess(R.string.successful_login)

                _loginResponse.value = userTokenResponse

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

