package com.example.apiapplication.networking

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.apiapplication.data.api.RaspberryAPI
import com.example.apiapplication.data.models.DotaUserRaspberry
import com.example.apiapplication.data.models.MatchStats
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException

class RaspberryPiProvider {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val _matchStats = MutableStateFlow<MatchStats?>(null)
    val matchStats: StateFlow<MatchStats?> = _matchStats

    private val _players = MutableStateFlow<List<MatchStats.Player>>(emptyList())
    val players: StateFlow<List<MatchStats.Player>> = _players

    private val _steamIDProfile = MutableStateFlow<DotaUserRaspberry?>(null)
    val steamIDProfile: StateFlow<DotaUserRaspberry?> = _steamIDProfile

    private val _steamComments = MutableStateFlow<List<DotaUserRaspberry.Comment>>(emptyList())
    val steamComments: StateFlow<List<DotaUserRaspberry.Comment>> = _steamComments

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://176.99.158.188:50993/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = try {
        retrofit.create(RaspberryAPI::class.java)
    } catch (e: Exception) {
        when (e) {
            is SocketTimeoutException -> {
                Log.d("zxc", "timed out")
                null
            }

            else -> {
                Log.d("zxc", "some exception")
                null
            }
        }
    }

    fun fetchSteamIDProfile(id: CharSequence) {
        if (api != null) coroutineScope.launch(Dispatchers.IO) {
            val steamIDProfileDeferred = async { api.getSteamIDProfile("dotaProfile", id) }

            val steamIDProfile = steamIDProfileDeferred.await()

            _steamIDProfile.value = steamIDProfile //null if error
            _steamComments.value = steamIDProfile.data //null if error
        }
    }

    fun postComment(id: String, author: String, content: String, callback: (Boolean) -> Unit) {
        if (api != null) {
            coroutineScope.launch(Dispatchers.IO) {
                val newComment = DotaUserRaspberry.Comment(content, author)

                if (steamIDProfile.value?._steam_id != null) {
                    val currentProfile = steamIDProfile.value

                    val updatedData = currentProfile?.data?.toMutableList()?.apply { add(newComment) } ?: listOf(newComment)

                    val updatedProfile = currentProfile?.copy(data = updatedData)

                    val response = api.postSteamIDProfile(updatedProfile!!)

                    if (response.isSuccessful) {
                        _steamIDProfile.value = updatedProfile
                        _steamComments.value = updatedData
                        Log.d("zxc", "new comment posted to the current profile $response")
                        callback(true)
                    } else {
                        callback(false)
                    }
                } else {
                    val newProfile = DotaUserRaspberry(id, listOf(newComment))

                    val response = api.postSteamIDProfile(newProfile)

                    if (response.isSuccessful) {
                        _steamIDProfile.value = newProfile
                        _steamComments.value = listOf(newComment)
                        Log.d("zxc", "new comment posted to a new profile $response")
                        callback(true)
                    } else {
                        callback(false)
                    }
                }
            }
        }
    }
}