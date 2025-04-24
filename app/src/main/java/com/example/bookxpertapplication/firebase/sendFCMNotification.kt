package com.example.bookxpertapplication.firebase

import android.content.Context
import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

fun sendFCMNotification(context: Context, token: String, title: String, body: String) {
    val accessToken = GoogleAccessTokenProvider.getAccessToken(context) ?: return

    val jsonBody = JSONObject().apply {
        put("message", JSONObject().apply {
            put("token", token)
            put("notification", JSONObject().apply {
                put("title", title)
                put("body", body)
            })
        })
    }

    val requestBody = jsonBody.toString()
        .toRequestBody("application/json; charset=utf-8".toMediaType())

    val request = Request.Builder()
        .url("https://fcm.googleapis.com/v1/projects/bookxpertapplication-459b4/messages:send")
        .addHeader("Authorization", "Bearer $accessToken")
        .addHeader("Content-Type", "application/json")
        .post(requestBody)
        .build()

    OkHttpClient().newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("FCM", "Notification failed: ${e.message}")
        }

        override fun onResponse(call: Call, response: Response) {
            Log.d("FCM", "Notification success: ${response.body?.string()}")
        }
    })
}
