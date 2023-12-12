package org.thoughtcrime.securesms.home.api

import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.thoughtcrime.securesms.home.listeners.MosqueResponseListener
import org.thoughtcrime.securesms.home.views.NearbyMosqueModel
import java.io.IOException

object NearbyMosqueApi {

    private var client: OkHttpClient? = null
    private const val API_ENDPOINT = "https://api.prayersconnect.com/mosques.json"
    private const val TOKEN = "wGYkQ5mjUNEZfgBbCy3sGW9GoG3QGuw7QEv2DPiC2FU"
    fun initialize() {
        client ?: run {
            client = OkHttpClient()
        }
    }

    fun getNearbyMosque(latitude: Float, longitude: Float, responseHandler: MosqueResponseListener) {
        val urlBuilder = HttpUrl.parse(API_ENDPOINT)?.newBuilder()
        urlBuilder?.addQueryParameter("lat", latitude.toString())
        urlBuilder?.addQueryParameter("long", longitude.toString())

        val url = urlBuilder?.build().toString()

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $TOKEN")
            .build()

        client?.newCall(request)?.enqueue(object : okhttp3.Callback {
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val responseData = response.body()?.string()
                responseHandler.onSuccess(responseData)
                println("Response: $responseData")
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                // Handle failure/error
                responseHandler.onFailure(e.message)
                println("Request failed: ${e.message}")
            }
        })
    }
}