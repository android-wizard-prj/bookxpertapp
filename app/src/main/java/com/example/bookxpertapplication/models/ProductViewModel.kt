package com.example.bookxpertapplication.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookxpertapplication.repositories.ProductRepository
import com.example.bookxpertapplication.roomdatabase.ProductEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _products = MutableStateFlow<List<ProductEntity>>(emptyList())
    val products = _products.asStateFlow()

    fun loadProducts() {
        viewModelScope.launch {
            repository.fetchAndStoreProducts()
            _products.value = repository.getProductsFromDb()
        }
    }

    // Update a product in the database
    fun updateProduct(product: ProductEntity) {
        viewModelScope.launch {
            repository.updateProduct(product) // Call repository to update product
            _products.value = repository.getProductsFromDb() // Refresh the product list after update
        }
    }

    // Delete a product by ID
    fun deleteProductById(id: Int) {
        viewModelScope.launch {
            repository.deleteProductById(id) // Call repository to delete product by ID
            _products.value = repository.getProductsFromDb() // Refresh the product list after deletion
        }
    }

}

