package com.duoc.principedecolores.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.duoc.principedecolores.ui.screen.* // Importa todas las pantallas
import com.duoc.principedecolores.ui.viewmodel.* // Importa todos los ViewModels

sealed class Screen(val route: String) {
    object Catalog : Screen("catalog")
    object Login : Screen("login") // Login Admin
    object Inventory : Screen("inventory")
    object Carrito : Screen("carrito")
    object Register : Screen("register")
    object LoginCliente : Screen("login_cliente") // <--- 1. FALTABA ESTO
}

@Composable
fun Navigation(
    navController: NavHostController,
    catalogViewModel: CatalogViewModel,
    loginViewModel: LoginViewModel,
    inventoryViewModel: InventoryViewModel,
    carritoViewModel: CarritoViewModel,
    registerViewModel: RegisterViewModel,
    loginClienteViewModel: LoginClienteViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Catalog.route
    ) {
        // --- PANTALLA CATÁLOGO ---
        composable(Screen.Catalog.route) {
            CatalogScreen(
                viewModel = catalogViewModel,
                // CAMBIO IMPORTANTE: El botón de login/comprar debe llevar al Cliente, no al Admin
                onNavigateToLogin = {
                    navController.navigate(Screen.LoginCliente.route)
                },
                onNavigateToCart = {
                    navController.navigate(Screen.Carrito.route)
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        // --- PANTALLA LOGIN ADMIN ---
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = loginViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onLoginSuccess = {
                    navController.navigate(Screen.Inventory.route) {
                        popUpTo(Screen.Catalog.route)
                    }
                }
            )
        }

        // --- PANTALLA LOGIN CLIENTE ---
        composable(Screen.LoginCliente.route) {
            LoginClienteScreen(
                viewModel = loginClienteViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onLoginSuccess = {
                    // Al loguearse, volvemos al catálogo para que pueda seguir comprando
                    navController.popBackStack()},
                onNavigateToAdmin = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }

        // --- PANTALLA REGISTRO (FALTABA ESTE BLOQUE) ---
        composable(Screen.Register.route) { // <--- 2. FALTABA ESTO
            RegisterScreen(
                viewModel = registerViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // --- PANTALLA INVENTARIO (ADMIN) ---
        composable(Screen.Inventory.route) {
            InventoryScreen(
                viewModel = inventoryViewModel,
                onNavigateBack = {
                    navController.navigate(Screen.Catalog.route) {
                        popUpTo(Screen.Catalog.route) { inclusive = true }
                    }
                }
            )
        }

        // --- PANTALLA CARRITO ---
        composable(Screen.Carrito.route) {
            CarritoScreen(
                viewmodel = carritoViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}