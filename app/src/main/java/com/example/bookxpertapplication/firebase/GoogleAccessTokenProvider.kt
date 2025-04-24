package com.example.bookxpertapplication.firebase


import android.content.Context
import com.google.auth.oauth2.GoogleCredentials

object GoogleAccessTokenProvider {
    fun getAccessToken(context: Context): String? {
        val stream = context.assets.open("bookxpertapplication-service-account.json")
        val credentials = GoogleCredentials.fromStream(stream)
            .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"))

        credentials.refreshIfExpired()
        return credentials.accessToken.tokenValue
    }
}
