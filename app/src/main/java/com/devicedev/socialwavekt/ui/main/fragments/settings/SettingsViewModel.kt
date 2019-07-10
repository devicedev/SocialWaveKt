package com.devicedev.socialwavekt.ui.main.fragments.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.devicedev.socialwavekt.data.retrofit.ServiceGenerator
import com.devicedev.socialwavekt.data.retrofit.services.UserService
import com.devicedev.socialwavekt.data.room.SocialWaveDatabase
import com.devicedev.socialwavekt.ui.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application): AndroidViewModel(application) {

    private val repository = BaseRepository(
        SocialWaveDatabase.getDatabase(application.applicationContext,viewModelScope).userDao(),
        ServiceGenerator.create(UserService::class.java)
    )

    private val context = application.applicationContext

    fun logout(){
        viewModelScope.launch(Dispatchers.IO) {

            repository.delete()

        }
    }


}