/**
 *
 * package com.duoc.principedecolores.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add // Importado para el botón de añadir
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.duoc.principedecolores.data.model.ItemCarrito
import com.duoc.principedecolores.ui.viewmodel.CarritoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    viewmodel: CarritoViewModel,
    onNavigateBack: () -> Unit
){
    val estadoIu by viewmodel.estadoIu.collectAsState()
    val total = remember(estadoIu.itemCarritoItems) { viewmodel.calcularTotal() }

    Scaffold (
        topBar = {
            TopAppBar(
                title = {Text("Carrito de Compras")},
                navigationIcon = {
                    IconButton(onClick = onNavigateBack){
                        Icon(Icons.AutoMirrored.Filled.ArrowBack,"Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        bottomBar = {
            if(estadoIu.itemCarritoItems.isNotEmpty()){
                CarritoBottomBar(
                    total = total,
                    aPagar = { viewmodel.pagar()}
                )
            }
        }

    ){ padding ->
        if (estadoIu.carga){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator()
            }
        } else if (estadoIu.itemCarritoItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    Text(
                        "🛒",
                        style = MaterialTheme.typography.displayLarge
                    )
                    Text(
                        "Tu carrito está vacío",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        "Agrega productos para continuar",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ){
                items(estadoIu.itemCarritoItems) { itemCarrito ->
                    TarjetaItemCarrito(
                        itemCarrito = itemCarrito,
                        cambiandoCantidad = { nuevaCantidad ->
                            viewmodel.actualizaCantidad( itemCarrito, nuevaCantidad)
                        },
                        borrando = { viewmodel.borrarJabonCarrito(itemCarrito)}
                    )
                }
                item{
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }

    if(estadoIu.pago){
        AlertDialog(
            onDismissRequest = {
                viewmodel.finalizarpago()
                onNavigateBack()
            },
            title = { Text("¡Pago Exitoso! 🎉") },
            text = {
                Text("Tu carrito ha sido pagado exitosamente. Gracias por tu compra.")
            },
            confirmButton = {
                Button(onClick = {
                    viewmodel.finalizarpago()
                    onNavigateBack()
                }){
                    Text("Aceptar")
                }
            }
        )
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun TarjetaItemCarrito(
    itemCarrito: ItemCarrito,
    cambiandoCantidad: (Int) -> Unit,
    borrando: () -> Unit
){
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically // Centra verticalmente los elementos de la fila
        ){
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ){
                if (itemCarrito.uriImagenProducto != null){
                    AsyncImage(
                        model = itemCarrito.uriImagenProducto,
                        contentDescription = itemCarrito.nombreProducto,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box (
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        Text("🧼", style = MaterialTheme.typography.headlineMedium)
                    }
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp) // Espacio entre los textos
            ){
                Text(
                    text = itemCarrito.nombreProducto,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "$${String.format("%d", itemCarrito.precioProducto)} c/u", // Añadido "c/u" para mayor claridad
                    style = MaterialTheme.typography.bodyMedium, // Ligeramente más pequeño
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // --- INICIO DE LA SECCIÓN CORREGIDA ---
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    IconButton(
                        onClick = { cambiandoCantidad(itemCarrito.cantidad - 1) },
                        enabled = itemCarrito.cantidad > 1, // Se deshabilita si la cantidad es 1
                        modifier = Modifier.size(32.dp)
                    ){
                        Icon(Icons.Default.Remove, "Disminuir", tint = MaterialTheme.colorScheme.primary)
                    }

                    Text(
                        text = itemCarrito.cantidad.toString(),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(
                        onClick = { cambiandoCantidad(itemCarrito.cantidad + 1) },
                        modifier = Modifier.size(32.dp)
                    ){
                        Icon(Icons.Default.Add, "Aumentar", tint = MaterialTheme.colorScheme.primary)
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = "$${String.format("%d", itemCarrito.subtotal)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                // --- FIN DE LA SECCIÓN CORREGIDA ---
            }

            IconButton(onClick = borrando) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

// El Composable CarritoBottomBar no necesita cambios, está perfecto.
@SuppressLint("DefaultLocale")
@Composable
fun CarritoBottomBar(
    total: Int,
    aPagar: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total:",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$${String.format("%d", total)}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = aPagar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    "Pagar",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
 **/

package com.duoc.principedecolores.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // <--- ¡ESTE ERA EL ERROR 1! Faltaba este import
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.duoc.principedecolores.data.model.ItemCarrito
import com.duoc.principedecolores.ui.viewmodel.CarritoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    viewmodel: CarritoViewModel,
    onNavigateBack: () -> Unit
){
    val estadoIu by viewmodel.estadoIu.collectAsState()

    // Calculamos el total (O usamos el del backend si lo implementaste en el ViewModel)
    // Cambié 'itemCarritoItems' por 'items' para que sea más limpio
    val total = remember(estadoIu.items) { viewmodel.calcularTotal() }

    // --- RECARGA AUTOMÁTICA ---
    // Esto asegura que al entrar veas el carrito actualizado
    LaunchedEffect(Unit) {
        viewmodel.cargaCarrito()
    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("Carrito de Compras") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack){
                        Icon(Icons.AutoMirrored.Filled.ArrowBack,"Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        bottomBar = {
            // Error 2: Cambiado 'itemCarritoItems' por 'items'
            if(estadoIu.items.isNotEmpty()){
                CarritoBottomBar(
                    total = total,
                    aPagar = { viewmodel.pagar()}
                )
            }
        }

    ){ padding ->
        if (estadoIu.carga){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator()
            }
            // Error 2: Cambiado 'itemCarritoItems' por 'items'
        } else if (estadoIu.items.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    Text(
                        "🛒",
                        style = MaterialTheme.typography.displayLarge
                    )
                    Text(
                        "Tu carrito está vacío",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        } else {
            LazyColumn (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ){
                // Error 2: Cambiado 'itemCarritoItems' por 'items'
                items(estadoIu.items) { itemCarrito ->
                    TarjetaItemCarrito(
                        itemCarrito = itemCarrito,
                        cambiandoCantidad = { nuevaCantidad ->
                            viewmodel.actualizaCantidad(itemCarrito, nuevaCantidad)
                        },
                        borrando = { viewmodel.borrarJabonCarrito(itemCarrito)}
                    )
                }
                item{
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }

    if(estadoIu.pagoExitoso){
        AlertDialog(
            onDismissRequest = {
                viewmodel.finalizarPago()
                onNavigateBack()
            },
            title = { Text("¡Pago Exitoso! 🎉") },
            text = {
                Text("Tu carrito ha sido pagado exitosamente.")
            },
            confirmButton = {
                Button(onClick = {
                    viewmodel.finalizarPago()
                    onNavigateBack()
                }){
                    Text("Aceptar")
                }
            }
        )
    }
}

// ... (El resto de tus composables TarjetaItemCarrito y CarritoBottomBar están bien) ...
@SuppressLint("DefaultLocale")
@Composable
fun TarjetaItemCarrito(
    itemCarrito: ItemCarrito,
    cambiandoCantidad: (Int) -> Unit,
    borrando: () -> Unit
){
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ){
                if (itemCarrito.uriImagenProducto != null){
                    AsyncImage(
                        model = itemCarrito.uriImagenProducto,
                        contentDescription = itemCarrito.nombreProducto,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box (
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        Text("🧼", style = MaterialTheme.typography.headlineMedium)
                    }
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ){
                Text(
                    text = itemCarrito.nombreProducto,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "$${String.format("%d", itemCarrito.precioProducto)} c/u",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    IconButton(
                        onClick = { cambiandoCantidad(itemCarrito.cantidad - 1) },
                        enabled = itemCarrito.cantidad > 1,
                        modifier = Modifier.size(32.dp)
                    ){
                        Icon(Icons.Default.Remove, "Disminuir", tint = MaterialTheme.colorScheme.primary)
                    }

                    Text(
                        text = itemCarrito.cantidad.toString(),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(
                        onClick = { cambiandoCantidad(itemCarrito.cantidad + 1) },
                        modifier = Modifier.size(32.dp)
                    ){
                        Icon(Icons.Default.Add, "Aumentar", tint = MaterialTheme.colorScheme.primary)
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = "$${String.format("%d", itemCarrito.subtotal)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            IconButton(onClick = borrando) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun CarritoBottomBar(
    total: Int,
    aPagar: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total:",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$${String.format("%d", total)}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = aPagar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    "Pagar",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}