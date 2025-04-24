package com.example.bookxpertapplication

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookxpertapplication.models.ProductViewModel
import com.example.bookxpertapplication.roomdatabase.ProductEntity
import com.example.bookxpertapplication.ui.theme.BookXpertApplicationTheme
import com.example.bookxpertapplication.firebase.TokenManager
import com.example.bookxpertapplication.firebase.sendFCMNotification
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductListScreen : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BookXpertApplicationTheme {
                val cameraPermissionState = rememberPermissionState(permission = android.Manifest.permission.POST_NOTIFICATIONS)

                LaunchedEffect(Unit) {
                    cameraPermissionState.launchPermissionRequest()
                }
                ProductList(context = this)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductList(viewModel: ProductViewModel = hiltViewModel(), context: Context) {
    val products by viewModel.products.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadProducts()
    }

    LazyColumn() {
        items(products) { product ->
            ProductItem(
                product = product,
                onUpdateClick = { updatedProduct ->
                    // Call your ViewModel method to update product price

                    viewModel.updateProduct(updatedProduct)
                },
                onDeleteClick = { productId ->
                    // Call your ViewModel method to delete product by ID
                    viewModel.deleteProductById(productId)
                    val deviceFcmToken = TokenManager.getToken(context)
                    if (deviceFcmToken != null) {
                        CoroutineScope(Dispatchers.Default).launch {
                            sendFCMNotification(
                                context = context,
                                token = deviceFcmToken, // Store this from MyFirebaseService
                                title = "Product Deleted",
                                body = "Product ${product.name} with ID ${product.id} was deleted"
                            )
                        }

                    }

                }
            )
        }
    }


}


@Composable
fun ProductItem(
    product: ProductEntity,
    onUpdateClick: (ProductEntity) -> Unit,
    onDeleteClick: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .background(Color(0xFFEFEFEF), shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "ID: ${product.id}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Name: ${product.name}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                Text(text = product.dataJson ?: "No Data", style = MaterialTheme.typography.bodySmall)
            }


            Column(horizontalAlignment = Alignment.End) {
                IconButton(
                    onClick = {
                        product.name = "${product.name} updated"
                        onUpdateClick(product) },

                    ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Update Price"
                    )
                }

                // Delete icon (bottom right corner)
                IconButton(
                    onClick = { onDeleteClick(product.id.toInt()) },

                    ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Product"
                    )
                }
            }
        }
        // Display product details



        // "+" icon for update (top right corner)

    }
}

