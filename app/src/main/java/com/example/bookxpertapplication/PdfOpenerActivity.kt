package com.example.bookxpertapplication

import android.app.Activity
import android.os.Bundle
import com.rajat.pdfviewer.PdfViewerActivity


class PdfOpenerActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PdfViewerActivity.launchPdfFromUrl(
            context = this,
            pdfUrl = "https://fssservices.bookxpert.co/GeneratedPDF/Companies/nadc/2024-2025/BalanceSheet.pdf",
            pdfTitle = "Balance Sheet",
            enableDownload = true,
            directoryName = "BookXpertPdfs"
        )
//        finish()
    }
}
