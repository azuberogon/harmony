package com.example.v2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse

class MainActivity : AppCompatActivity() {

    private var spotifyAppRemote: SpotifyAppRemote? = null
    private val CLIENT_ID = "989fed99249b486f872dcee08f6410f3"  // Usa tu CLIENT_ID aquí
    private val REDIRECT_URI = "http://localhost:3000"  // Usa tu REDIRECT_URI aquí
    private val REQUEST_CODE = 1337

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        // Asegúrate de que el usuario esté autenticado antes de intentar conectar a SpotifyAppRemote
        authenticateSpotify()
    }

    private fun authenticateSpotify() {
        val authRequest = AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI)
            .setScopes(arrayOf("user-library-read", "playlist-modify-public", "streaming")) // Define los permisos que necesitas
            .build()

        // Llama a la actividad de autenticación de Spotify
        AuthorizationClient.openLoginActivity(this, REQUEST_CODE, authRequest)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        // Verifica si la respuesta proviene de la actividad de autenticación
        if (requestCode == REQUEST_CODE) {
            val response = AuthorizationClient.getResponse(resultCode, intent)

            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    Log.d("MainActivity", "Autenticación exitosa, token recibido: ${response.accessToken}")
                    // Usar el token para conectar a SpotifyAppRemote
                    connectToSpotifyAppRemote(response.accessToken)
                }
                AuthorizationResponse.Type.ERROR -> {
                    Log.e("MainActivity", "Error en el flujo de autenticación: ${response.error}")
                }
                else -> {
                    Log.d("MainActivity", "Autenticación cancelada o no exitosa")
                }
            }
        }
    }

    private fun connectToSpotifyAppRemote(accessToken: String) {
        val connectionParams = ConnectionParams.Builder(CLIENT_ID)
            .setRedirectUri(REDIRECT_URI)
            .showAuthView(true)
            .build()

        // Intenta conectar a SpotifyAppRemote
        SpotifyAppRemote.connect(this, connectionParams, object : Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                spotifyAppRemote = appRemote
                Log.d("MainActivity", "Conectado exitosamente con Spotify!")
                // Aquí ya puedes interactuar con la app de Spotify
                println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
                connected()
            }

            override fun onFailure(throwable: Throwable) {
                Log.e("MainActivity", "Error al intentar conectar con SpotifyAppRemote", throwable)
                println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
                throwable.printStackTrace()
                if (throwable is com.spotify.android.appremote.api.error.UserNotAuthorizedException) {
                    Log.e("MainActivity", "No autorizado para usar Spotify. El usuario debe completar el flujo de autorización.")
                } else if (throwable.message?.contains("AUTHENTICATION_SERVICE_UNAVAILABLE") == true) {
                    Log.e("MainActivity", "El servicio de autenticación no está disponible. Verifica la conexión a internet y la configuración de la app de Spotify.")
                } else {
                    throwable.printStackTrace()
                }
            }
        })
    }

    private fun connected() {
        spotifyAppRemote?.let {
            // Ejemplo de reproducción de una playlist
            val playlistURI = "spotify:playlist:37i9dQZF1DX2sUQwD7tbmL"
            it.playerApi.play(playlistURI)

            // Subscribirse al estado del reproductor
            it.playerApi.subscribeToPlayerState().setEventCallback {
                val track = it.track
                Log.d("MainActivity", "Reproduciendo: ${track.name} por ${track.artist.name}")
            }
        }
    }

    override fun onStop() {
        super.onStop()
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        }
    }
}
