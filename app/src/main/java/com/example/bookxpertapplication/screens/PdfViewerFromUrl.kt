package com.example.bookxpertapplication.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun PdfViewerFromUrl(url: String) {
    val context = LocalContext.current
    val pdfFile = remember { mutableStateOf<File?>(null) }

    LaunchedEffect(url) {
        withContext(Dispatchers.IO) {
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.connect()

                val file = File(context.cacheDir, "temp.pdf")
                val output = FileOutputStream(file)
                connection.inputStream.use { input ->
                    output.use { out -> input.copyTo(out) }
                }
                pdfFile.value = file
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    pdfFile.value?.let { file ->
        AndroidView(
            factory = {
                com.github.barteksc.pdfviewer.PDFView(it, null).apply {
                    fromFile(file)
                        .enableSwipe(true)
                        .swipeHorizontal(false)
                        .enableDoubletap(true)
                        .defaultPage(0)
                        .spacing(10)
                        .load()
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    } ?: Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}