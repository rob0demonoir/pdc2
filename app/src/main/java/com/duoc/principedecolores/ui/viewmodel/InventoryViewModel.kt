package com.duoc.principedecolores.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.principedecolores.data.model.Product
import com.duoc.principedecolores.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class InventoryUiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = true,
    val showAddDialog: Boolean = false,
    val editingProduct: Product? = null
)

data class ProductFormState(
    val name: String = "",
    val description: String = "",
    val price: String = "",
    val stock: String = "",
    val imageUri: Uri? = null
)

class InventoryViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(InventoryUiState())
    val uiState: StateFlow<InventoryUiState> = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(ProductFormState())
    val formState: StateFlow<ProductFormState> = _formState.asStateFlow()

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            productRepository.allProducts.collect { products ->
                _uiState.value = _uiState.value.copy(
                    products = products,
                    isLoading = false
                )
            }
        }
    }

    fun showAddDialog() {
        _formState.value = ProductFormState()
        _uiState.value = _uiState.value.copy(showAddDialog = true, editingProduct = null)
    }

    fun showEditDialog(product: Product) {
        _formState.value = ProductFormState(
            name = product.name,
            description = product.description,
            price = product.price.toString(),
            stock = product.stock.toString(),
            imageUri = product.imageUri?.let { Uri.parse(it) }
        )
        _uiState.value = _uiState.value.copy(showAddDialog = true, editingProduct = product)
    }

    fun hideDialog() {
        _uiState.value = _uiState.value.copy(showAddDialog = false, editingProduct = null)
        _formState.value = ProductFormState()
    }

    fun updateFormName(name: String) {
        _formState.value = _formState.value.copy(name = name)
    }

    fun updateFormDescription(description: String) {
        _formState.value = _formState.value.copy(description = description)
    }

    fun updateFormPrice(price: String) {
        _formState.value = _formState.value.copy(price = price)
    }

    fun updateFormStock(stock: String) {
        _formState.value = _formState.value.copy(stock = stock)
    }

    fun updateFormImage(uri: Uri?) {
        _formState.value = _formState.value.copy(imageUri = uri)
    }

    fun saveProduct() {
        viewModelScope.launch {
            val form = _formState.value
            val price: Int = form.price.toIntOrNull() ?: 0
            val stock = form.stock.toIntOrNull() ?: 0

            val editingProduct = _uiState.value.editingProduct
            if (editingProduct != null) {

                productRepository.updateProduct(
                    editingProduct.copy(
                        name = form.name,
                        description = form.description,
                        price = price,
                        stock = stock,
                        imageUri = form.imageUri?.toString()
                    )
                )
            } else {

                productRepository.insertProduct(
                    Product(
                        name = form.name,
                        description = form.description,
                        price = price,
                        stock = stock,
                        imageUri = form.imageUri?.toString()
                    )
                )
            }
            hideDialog()
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            productRepository.deleteProduct(product)
        }
    }
}