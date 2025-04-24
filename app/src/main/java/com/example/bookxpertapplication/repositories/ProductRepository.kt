package com.example.bookxpertapplication.repositories

import com.example.bookxpertapplication.interfaces.ApiService
import com.example.bookxpertapplication.interfaces.ProductDao
import com.example.bookxpertapplication.roomdatabase.ProductEntity
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val api: ApiService,
    private val dao: ProductDao
) {
    suspend fun fetchAndStoreProducts() {
        val response = api.getProducts()
        val entities = response.map {
            ProductEntity(
                id = it.id,
                name = it.name,
                dataJson = it.data?.toString()
            )
        }
        dao.insertAll(entities)
    }

    suspend fun getProductsFromDb(): List<ProductEntity> = dao.getAll()

    suspend fun updateProduct(product: ProductEntity) {
        dao.updateProduct(product)
    }

    suspend fun deleteProductById(id: Int) {
        dao.deleteProductById(id)
    }
}
