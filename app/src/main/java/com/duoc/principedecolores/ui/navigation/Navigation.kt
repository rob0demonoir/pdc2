package com.duoc.principedecolores.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.duoc.principedecolores.ui.screen.*
import com.duoc.principedecolores.ui.viewmodel.*

sealed class Screen(val route: String) {
    object Catalog : Screen("catalog")
    object Login : Screen("login")
    object Inventory : Screen("inventory")
    object Carrito : Screen("carrito")
    object Register : Screen("register")
    object LoginCliente : Screen("login_cliente")

    object Horoscopo : Screen("horoscopo")
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

        composable(Screen.Catalog.route) {
            CatalogScreen(
                viewModel = catalogViewModel,

                onNavigateToLogin = {
                    navController.navigate(Screen.LoginCliente.route)
                },
                onNavigateToCart = {
                    navController.navigate(Screen.Carrito.route)
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onNavigateToHoroscopo = {
                    navController.navigate(Screen.Horoscopo.route)
                }
            )
        }

        composable(Screen.Horoscopo.route) {
            HoroscopeScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

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

        composable(Screen.LoginCliente.route) {
            LoginClienteScreen(
                viewModel = loginClienteViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onLoginSuccess = {

                    navController.popBackStack()},
                onNavigateToAdmin = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }

        composable(Screen.Register.route) { // <--- 2. FALTABA ESTO
            RegisterScreen(
                viewModel = registerViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

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

        composable(Screen.Carrito.route) {
            CarritoScreen(
                viewmodel = carritoViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}