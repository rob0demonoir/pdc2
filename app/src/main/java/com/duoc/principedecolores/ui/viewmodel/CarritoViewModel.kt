/**package com.duoc.principedecolores.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.principedecolores.data.model.ItemCarrito
import com.duoc.principedecolores.data.repository.CarritoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CarritoState(
    val itemCarritoItems: List<ItemCarrito> = emptyList(),
    val carga: Boolean = true,
    val pago: Boolean = false

)

class CarritoViewModel (
    private val carritorepository: CarritoRepository
) : ViewModel(){
    private val _estadoIu = MutableStateFlow(CarritoState())
    val estadoIu: StateFlow<CarritoState> = _estadoIu.asStateFlow()

    val contarJabonesCarrito = carritorepository.contarJabonesCarrito

    init{
        cargaCarrito()
    }

    private fun cargaCarrito(){
        viewModelScope.launch {
            carritorepository.obtenerItemCarrito.collect { items ->
                _estadoIu.value = _estadoIu.value.copy(
                    itemCarritoItems = items,
                    carga = false
                )
            }
        }
    }

    fun actualizaCantidad(itemCarrito: ItemCarrito, nuevaCantidad: Int){
        if (nuevaCantidad <=0){
            borrarJabonCarrito(itemCarrito)
            return
        }

        viewModelScope.launch{
            carritorepository.actualizarJabonCarrito(
                itemCarrito.copy(cantidad = nuevaCantidad)
            )
        }
    }

    fun borrarJabonCarrito(itemCarrito: ItemCarrito){
        viewModelScope.launch{
            carritorepository.borrarJabonCarrito(itemCarrito)
        }
    }

    fun pagar() {
        viewModelScope.launch {
            carritorepository.eliminarCarrito()
            _estadoIu.value = _estadoIu.value.copy(pago = true)
        }
    }

    fun finalizarpago() {
        _estadoIu.value = _estadoIu.value.copy(pago = false)
    }

    fun calcularTotal(): Int {
        return _estadoIu.value.itemCarritoItems.sumOf { it.subtotal }
    }
}
**/

package com.duoc.principedecolores.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.principedecolores.data.model.ItemCarrito
import com.duoc.principedecolores.data.repository.CarritoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CarritoState(
    val items: List<ItemCarrito> = emptyList(), // Usamos 'items' simple
    val carga: Boolean = true,
    val pagoExitoso: Boolean = false // Renombrado para claridad
)

class CarritoViewModel(
    private val repository: CarritoRepository
) : ViewModel() {

    private val _estadoIu = MutableStateFlow(CarritoState())
    val estadoIu: StateFlow<CarritoState> = _estadoIu.asStateFlow()

    fun cargaCarrito() {
        viewModelScope.launch {
            _estadoIu.value = _estadoIu.value.copy(carga = true)

            repository.obtenerCarrito.collect { itemsRecibidos ->
                _estadoIu.value = _estadoIu.value.copy(
                    items = itemsRecibidos,
                    carga = false
                )
            }
        }
    }

    fun actualizaCantidad(item: ItemCarrito, nuevaCantidad: Int) {
        if (nuevaCantidad <= 0) {
            borrarJabonCarrito(item)
            return
        }

        viewModelScope.launch {




            val diferencia = nuevaCantidad - item.cantidad

            repository.anadirAlCarrito(
                item.copy(cantidad = diferencia)
            )

            cargaCarrito()
        }
    }

    fun borrarJabonCarrito(item: ItemCarrito) {
        viewModelScope.launch {
            repository.borrarJabonCarrito(item)
            cargaCarrito()
        }
    }

    fun pagar() {
        viewModelScope.launch {
            _estadoIu.value = _estadoIu.value.copy(carga = true)
            val exito = repository.procesarCompra()
            if (exito) {
                _estadoIu.value = _estadoIu.value.copy(
                    items = emptyList(),
                    pagoExitoso = true,
                    carga = false
                )
            } else {
                _estadoIu.value = _estadoIu.value.copy(carga = false)

            }
        }
    }

    fun finalizarPago() {
        _estadoIu.value = _estadoIu.value.copy(pagoExitoso = false)
    }

    fun calcularTotal(): Int {
        return _estadoIu.value.items.sumOf { it.subtotal }
    }
}