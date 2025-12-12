/**

REPOSITORY PARA ALMACENAMIENTO DE DATOS EN LOCAL

package com.duoc.principedecolores.data.repository

import com.duoc.principedecolores.data.dao.ProductDao
import com.duoc.principedecolores.data.model.Product
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val productDao: ProductDao) {
    
    val allProducts: Flow<List<Product>> = productDao.getAllProducts()
    
    val availableProducts: Flow<List<Product>> = productDao.getAvailableProducts()
    
    suspend fun getProductById(id: Int): Product? {
        return productDao.getProductById(id)
    }
    
    suspend fun insertProduct(product: Product): Long {
        return productDao.insertProduct(product)
    }
    
    suspend fun updateProduct(product: Product) {
        productDao.updateProduct(product)
    }
    
    suspend fun deleteProduct(product: Product) {
        productDao.deleteProduct(product)
    }
}

**/

package com.duoc.principedecolores.data.repository

import android.util.Log
import com.duoc.principedecolores.data.api.RetrofitClient
import com.duoc.principedecolores.data.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProductRepository { // Ya no recibe ProductDao

    // Creamos un Flow que llama a la API cada vez que se observa
    val allProducts: Flow<List<Product>> = flow {
        try {
            val response = RetrofitClient.apiService.getProducts()
            if (response.isSuccessful) {
                emit(response.body() ?: emptyList())
            } else {
                Log.e("API", "Error productos: ${response.code()}")
                emit(emptyList())
            }
        } catch (e: Exception) {
            Log.e("API", "Error red productos", e)
            emit(emptyList())
        }
    }

    // Devolvemos lista vacía o filtramos en memoria si la API tuviera endpoint de "disponibles"
    val availableProducts: Flow<List<Product>> = allProducts

    suspend fun getProductById(id: Int): Product? {
        // En una app real, llamaríamos a api.getProductById(id)
        // Por ahora retornamos null o implementa el endpoint si lo necesitas
        return null
    }

    suspend fun insertProduct(product: Product): Long {
        return try {
            val response = RetrofitClient.apiService.createProduct(product)
            if (response.isSuccessful) 1 else -1
        } catch (e: Exception) { -1 }
    }

    suspend fun updateProduct(product: Product) {
        try {
            RetrofitClient.apiService.updateProduct(product.id, product)
        } catch (e: Exception) { Log.e("API", "Error update", e) }
    }

    suspend fun deleteProduct(product: Product) {
        try {
            RetrofitClient.apiService.deleteProduct(product.id)
        } catch (e: Exception) { Log.e("API", "Error delete", e) }
    }
}