package com.example.bookxpertapplication.firebase


import android.content.Context
import com.google.auth.oauth2.GoogleCredentials

object GoogleAccessTokenProvider {
    fun getAccessToken(context: Context): String? {
        return try {
            val stream = context.assets.open("services-account.json")
            val credentials = GoogleCredentials.fromStream(stream)
                .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"))

            credentials.refreshIfExpired()
            credentials.accessToken.tokenValue
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
