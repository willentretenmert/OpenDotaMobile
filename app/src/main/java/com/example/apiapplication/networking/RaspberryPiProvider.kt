package com.example.apiapplication.networking

import android.util.Log
import com.example.apiapplication.data.api.RaspberryAPI
import com.example.apiapplication.data.models.DotaUserRaspberry
import com.example.apiapplication.data.models.FirebaseUserRaspberry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException

class RaspberryPiProvider {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val _steamIDProfile = MutableStateFlow<DotaUserRaspberry?>(null)
    val steamIDProfile: StateFlow<DotaUserRaspberry?> = _steamIDProfile

    private val _steamComments = MutableStateFlow<List<DotaUserRaspberry.Comment>>(emptyList())
    val steamComments: StateFlow<List<DotaUserRaspberry.Comment>> = _steamComments

    private val _firebaseProfile = MutableStateFlow<FirebaseUserRaspberry?>(null)
    val firebaseProfile: StateFlow<FirebaseUserRaspberry?> = _firebaseProfile

    private val _favouritesPlayers =
        MutableStateFlow<List<FirebaseUserRaspberry.FavouritePlayers>>(emptyList())
    val favouritesPlayers: StateFlow<List<FirebaseUserRaspberry.FavouritePlayers>> =
        _favouritesPlayers

    private val _favouritesMatches =
        MutableStateFlow<List<FirebaseUserRaspberry.FavouriteMatches>>(emptyList())
    val favouritesMatches: StateFlow<List<FirebaseUserRaspberry.FavouriteMatches>> =
        _favouritesMatches

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

    fun fetchFirebaseProfile(mail: CharSequence) {
        if (api != null) coroutineScope.launch(Dispatchers.IO) {
            val firebaseProfileDeferred = async { api.getFirebaseProfile("firebaseProfile", mail) }

            val firebaseProfile = firebaseProfileDeferred.await()

            _firebaseProfile.value = firebaseProfile //null if error
            _favouritesPlayers.value = firebaseProfile.players //null if error
            _favouritesMatches.value = firebaseProfile.matches //null if error
        }
    }

    fun postComment(id: String, author: String, content: String, callback: (Boolean) -> Unit) {
        if (api != null) {
            coroutineScope.launch(Dispatchers.IO) {
                val newComment = DotaUserRaspberry.Comment(content, author)

                if (steamIDProfile.value?._steam_id != null) {
                    val currentProfile = steamIDProfile.value

                    val updatedData =
                        currentProfile?.data?.toMutableList()?.apply { add(newComment) } ?: listOf(
                            newComment
                        )

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

    fun postFavouritePlayer(
        userMail: String,
        nickname: String,
        playerId: String,
        callback: (Boolean) -> Unit
    ) {
        if (api != null) {
            coroutineScope.launch(Dispatchers.IO) {
                val newFavourite = FirebaseUserRaspberry.FavouritePlayers(playerId)

                if (firebaseProfile.value?._firebase_id != null) {
                    val currentProfile = firebaseProfile.value

                    val updatedData =
                        currentProfile?.players?.toMutableList()?.apply { add(newFavourite) }
                            ?: listOf(newFavourite)

                    val updatedProfile = currentProfile?.copy(players = updatedData)

                    val response = api.postFirebaseProfile(updatedProfile!!)

                    if (response.isSuccessful) {
                        _firebaseProfile.value = updatedProfile
                        _favouritesPlayers.value = updatedData
                        Log.d("zxc", "$playerId player added to favs in the current profile $userMail $response")
                        callback(true)
                    } else {
                        callback(false)
                    }
                } else {
                    val newProfile =
                        FirebaseUserRaspberry(userMail, nickname, listOf(newFavourite), emptyList())

                    val response = api.postFirebaseProfile(newProfile)

                    if (response.isSuccessful) {
                        _firebaseProfile.value = newProfile
                        _favouritesPlayers.value = listOf(newFavourite)
                        Log.d("zxc", "$playerId player added to favs in the current profile $userMail 222 $response")
                        callback(true)
                    } else {
                        callback(false)
                    }
                }
            }
        }
    }

    fun deleteFavouritePlayer(
        userMail: String,
        nickname: String,
        playerId: String,
        callback: (Boolean) -> Unit
    ) {
        if (api != null) {
            coroutineScope.launch(Dispatchers.IO) {
                val currentProfile = firebaseProfile.value

                val updatedData = currentProfile?.players?.toMutableList()?.apply {
                    removeIf { it.steam_id == playerId }
                }

                if (updatedData != null) {
                    val updatedProfile = currentProfile.copy(players = updatedData)

                    val response = api.postFirebaseProfile(updatedProfile)

                    withContext(Dispatchers.Main) { // switch to main thread
                        if (response.isSuccessful) {
                            _firebaseProfile.value = updatedProfile
                            _favouritesPlayers.value = updatedData
                            Log.d("zxc", "$playerId player removed from the current profile $response")
                            callback(true)
                        } else {
                            callback(false)
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) { // switch to main thread
                        callback(false)
                    }
                }
            }
        }
    }


    fun postFavouriteMatch(
        userMail: String,
        nickname: String,
        matchId: String,
        callback: (Boolean) -> Unit
    ) {
        if (api != null) {
            coroutineScope.launch(Dispatchers.IO) {
                val newFavourite = FirebaseUserRaspberry.FavouriteMatches(matchId)

                if (firebaseProfile.value?._firebase_id != null) {
                    val currentProfile = firebaseProfile.value

                    val updatedData =
                        currentProfile?.matches?.toMutableList()?.apply { add(newFavourite) }
                            ?: listOf(newFavourite)

                    val updatedProfile = currentProfile?.copy(matches = updatedData)

                    val response = api.postFirebaseProfile(updatedProfile!!)

                    if (response.isSuccessful) {
                        _firebaseProfile.value = updatedProfile
                        _favouritesMatches.value = updatedData
                        Log.d("zxc", "$matchId match added to favs in the current profile $userMail $response")
                        callback(true)
                    } else {
                        callback(false)
                    }
                } else {
                    val newProfile =
                        FirebaseUserRaspberry(userMail, nickname, emptyList(), listOf(newFavourite))

                    val response = api.postFirebaseProfile(newProfile)

                    if (response.isSuccessful) {
                        _firebaseProfile.value = newProfile
                        _favouritesMatches.value = listOf(newFavourite)
                        Log.d("zxc", "$matchId player added to favs in the current profile $userMail 222 $response")
                        callback(true)
                    } else {
                        callback(false)
                    }
                }
            }
        }
    }

    fun deleteFavouriteMatch(
        userMail: String,
        nickname: String,
        matchId: String,
        callback: (Boolean) -> Unit
    ) {
        if (api != null) {
            coroutineScope.launch(Dispatchers.IO) {
                val currentProfile = firebaseProfile.value

                val updatedData = currentProfile?.matches?.toMutableList()?.apply {
                    removeIf { it.match_id == matchId }
                }

                if (updatedData != null) {
                    val updatedProfile = currentProfile.copy(matches = updatedData)

                    val response = api.postFirebaseProfile(updatedProfile)

                    withContext(Dispatchers.Main) { // switch to main thread
                        if (response.isSuccessful) {
                            _firebaseProfile.value = updatedProfile
                            _favouritesMatches.value = updatedData
                            Log.d("zxc", "$matchId player removed from the current profile $response")
                            callback(true)
                        } else {
                            callback(false)
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) { // switch to main thread
                        callback(false)
                    }
                }
            }
        }
    }

}
