package com.devicedev.socialwavekt.ui.main.fragments.profile

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devicedev.socialwavekt.R
import com.devicedev.socialwavekt.data.retrofit.ServiceGenerator
import com.devicedev.socialwavekt.data.retrofit.requests.bodies.EditBody
import com.devicedev.socialwavekt.data.retrofit.responses.ErrorMessage
import com.devicedev.socialwavekt.data.retrofit.responses.UserResponse
import com.devicedev.socialwavekt.data.retrofit.services.UserService
import com.devicedev.socialwavekt.data.room.SocialWaveDatabase
import com.devicedev.socialwavekt.ui.BaseRepository
import com.devicedev.socialwavekt.utils.onError
import com.devicedev.socialwavekt.utils.onSuccess
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import java.lang.Exception

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext


    private val repository = ProfileRepository(
        SocialWaveDatabase.getDatabase(context, viewModelScope).userDao(),
        ServiceGenerator.create(UserService::class.java)
    )

    val user = repository.get()


    private val _isRefreshing = MutableLiveData<Boolean>()

    val isRefreshing: LiveData<Boolean> = _isRefreshing


    private val _progressBar = MutableLiveData<Int>()

    val progressBar: LiveData<Int> = _progressBar


    fun refresh(token: String) =

        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    repository.getCurrent(token)
                }

                if (!response.isSuccessful) {

                    context.onError(
                        Gson().fromJson(response.errorBody()?.string(), ErrorMessage::class.java).message
                    )
                    _isRefreshing.value = false

                    return@launch
                }

                val userResponse = response.body()

                withContext(Dispatchers.IO) {

                    repository.store(userResponse!!.user)

                }

                context.onSuccess(R.string.successful_refresh)


            } catch (e: IOException) {

                context.onError(R.string.err_no_network)

            } catch (e: HttpException) {

                e.printStackTrace()

            } catch (e: Exception) {

                e.printStackTrace()

            } finally {

                _isRefreshing.value = false

            }


        }


    fun edit(token: String, name: String, email: String, password: String, gender: String, image: File?) =

        viewModelScope.launch {

            try {

                val response = withContext(Dispatchers.IO) {
                    repository.edit(token, name, email, password, gender, image)
                }

                if (!response.isSuccessful) {

                    context.onError(
                        Gson().fromJson(response.errorBody()?.string(), ErrorMessage::class.java).message
                    )
                    _progressBar.value = View.INVISIBLE

                    return@launch
                }

                runBlocking(Dispatchers.IO) {
                    repository.delete()
                }

                val userResponse = response.body()

                withContext(Dispatchers.IO) {

                    repository.store(userResponse!!.user)

                }

                context.onSuccess(R.string.successful_edit)


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