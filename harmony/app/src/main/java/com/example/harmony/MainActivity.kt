package com.example.harmony

import android.os.Bundle
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.harmony.api.RetrofitClient
import com.example.harmony.api.TracksResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var searchView: SearchView
    private lateinit var tvResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchView = findViewById(R.id.searchView)
        tvResult = findViewById(R.id.tvResult)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchSong(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun searchSong(query: String) {
        val call = RetrofitClient.instance.searchTrack(query)
        call.enqueue(object : Callback<TracksResponse> {
            override fun onResponse(call: Call<TracksResponse>, response: Response<TracksResponse>) {
                if (response.isSuccessful) {
                    val tracks = response.body()?.data ?: emptyList()
                    if (tracks.isNotEmpty()) {
                        val resultText = tracks.joinToString("\n") { "${it.title} - ${it.artist}" }
                        tvResult.text = resultText
                    } else {
                        tvResult.text = "No se encontraron canciones"
                    }
                } else {
                    tvResult.text = "Error al obtener datos"
                }
            }

            override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                tvResult.text = "Error: ${t.message}"
            }
        })
    }
}

