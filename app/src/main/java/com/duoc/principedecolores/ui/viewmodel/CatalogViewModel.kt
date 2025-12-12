/** package com.duoc.principedecolores.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.principedecolores.data.model.Product
import com.duoc.principedecolores.data.model.ItemCarrito
import com.duoc.principedecolores.data.repository.CarritoRepository
import com.duoc.principedecolores.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.Int
import kotlin.String

data class CatalogUiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = true
)

class CatalogViewModel(
    private val productRepository: ProductRepository,
    private val carritoRepository: CarritoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CatalogUiState())
    val uiState: StateFlow<CatalogUiState> = _uiState.asStateFlow()

    val contarJabonesCarrito = carritoRepository.contarJabonesCarrito

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            productRepository.allProducts.collect { products ->
                _uiState.value = CatalogUiState(
                    products = products,
                    isLoading = false
                )
            }
        }
    }

    fun getShareText(product: Product): String {
        return buildString {
            append("🧼 ${product.name}\n\n")
            append("📝 ${product.description}\n\n")
            append("💰 Precio: $${String.format("%d", product.price)}\n")
            if (product.isOutOfStock) {
                append("❌ AGOTADO\n")
            } else {
                append("✅ Disponible (${product.stock} unidades)\n")
            }
            append("\n¡Contacta para ordenar!")
        }
    }

    fun anadir(product: Product) {
        viewModelScope.launch {
            carritoRepository.anadirAlCarrito(
                ItemCarrito(
                    idProducto = product.id,
                    nombreProducto = product.name,
                    precioProducto = product.price,
                    uriImagenProducto = product.imageUri,
                    cantidad = 1
                )
            )
        }
    }
} **/

package com.duoc.principedecolores.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.principedecolores.data.model.ItemCarrito
import com.duoc.principedecolores.data.model.Product
import com.duoc.principedecolores.data.repository.CarritoRepository
import com.duoc.principedecolores.data.repository.ProductRepository
import com.duoc.principedecolores.utils.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

data class CatalogUiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = true
)

class CatalogViewModel(
    private val productRepository: ProductRepository,
    private val carritoRepository: CarritoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CatalogUiState())
    val uiState: StateFlow<CatalogUiState> = _uiState.asStateFlow()

    // Estado para controlar la redirección al Login si no está autenticado
    private val _navigateToLogin = MutableStateFlow(false)
    val navigateToLogin: StateFlow<Boolean> = _navigateToLogin.asStateFlow()

    private val _cuentaCarrito = MutableStateFlow(0)
    val cuentaCarrito: StateFlow<Int> = _cuentaCarrito.asStateFlow()

    private val _toastMessage = Channel<String>()
    val toastMessage = _toastMessage.receiveAsFlow()

    // El repositorio ya se encarga de pedir el conteo basado en la sesión actual
    val contarJabonesCarrito = carritoRepository.contarJabonesCarrito

    init {
        loadProducts()
        actualizaCuentaCarrito()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            productRepository.allProducts.collect { products ->
                _uiState.value = CatalogUiState(
                    products = products,
                    isLoading = false
                )
            }
        }
    }

    fun actualizaCuentaCarrito() {
        viewModelScope.launch {
            // El repositorio ya sabe si hay usuario o no.
            // Si SessionManager.isLoggedIn() es false, devolverá 0.
            carritoRepository.contarJabonesCarrito.collect { count ->
                _cuentaCarrito.value = count
            }
        }
    }



    // Reseteamos el evento de navegación una vez que la UI lo ha consumido
    fun resetNavigation() {
        _navigateToLogin.value = false
    }

    fun getShareText(product: Product): String {
        return buildString {
            append("🧼 ${product.name}\n\n")
            append("📝 ${product.description}\n\n")
            append("💰 Precio: $${String.format("%d", product.price)}\n")
            if (product.isOutOfStock) {
                append("❌ AGOTADO\n")
            } else {
                append("✅ Disponible (${product.stock} unidades)\n")
            }
            append("\n¡Contacta para ordenar!")
        }
    }

    fun anadir(product: Product) {
        // 1. VALIDACIÓN DE SESIÓN
        if (!SessionManager.isLoggedIn()) {
            _navigateToLogin.value = true // Avisamos a la UI que vaya al login
            return
        }

        // 2. Si está logueado, procedemos
        viewModelScope.launch {
            carritoRepository.anadirAlCarrito(
                ItemCarrito(
                    id = 0, // ID temporal, el backend genera el real
                    idProducto = product.id,
                    nombreProducto = product.name,
                    precioProducto = product.price,
                    uriImagenProducto = product.imageUri,
                    cantidad = 1

                )
            )
            _toastMessage.send("¡${product.name} agregado al carrito!")
            actualizaCuentaCarrito()
        }
    }
}


