package com.example.bookxpertapplication.screens


import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.bookxpertapplication.ImagePickerActivity
import com.example.bookxpertapplication.ProductListScreen
import com.google.firebase.auth.FirebaseUser

@Composable
fun HomeScreen(user: FirebaseUser) {
    val context = LocalContext.current
    var showPdf by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Welcome, ${user.displayName}", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Email: ${user.email}")

            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = {
                showPdf = true
                }) {
                Text("Open PDF")
            }

            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = {
                context.startActivity(Intent(context, ImagePickerActivity::class.java))

            }) {
                Text("Go for media images")
            }

            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = {
                context.startActivity(Intent(context, ProductListScreen::class.java))

            }) {
                Text("Go for API Integration")
            }
        }
    }
    if (showPdf) {
        PdfViewerFromUrl("https://fssservices.bookxpert.co/GeneratedPDF/Companies/nadc/2024-2025/BalanceSheet.pdf")
    }
}




