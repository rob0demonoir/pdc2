/*
PARA EJECUCION EN LOCAL

package com.duoc.principedecolores



import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.duoc.principedecolores.data.database.AppDatabase
import com.duoc.principedecolores.data.repository.AdminRepository
import com.duoc.principedecolores.data.repository.ProductRepository
import com.duoc.principedecolores.data.repository.CarritoRepository
import com.duoc.principedecolores.ui.navigation.Navigation
import com.duoc.principedecolores.ui.theme.PrincipedeColoresTheme
import com.duoc.principedecolores.ui.viewmodel.CarritoViewModel
import com.duoc.principedecolores.ui.viewmodel.CatalogViewModel
import com.duoc.principedecolores.ui.viewmodel.InventoryViewModel
import com.duoc.principedecolores.ui.viewmodel.LoginViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(applicationContext)
        val productRepository = ProductRepository(database.productDao())
        val adminRepository = AdminRepository(database.adminDao())
        val carritoRepository = CarritoRepository(database.carritoDao())


        setContent {
            PrincipedeColoresTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    val catalogViewModel: CatalogViewModel = viewModel(
                        factory = CatalogViewModelFactory(productRepository, carritoRepository)
                    )
                    val loginViewModel: LoginViewModel = viewModel(
                        factory = LoginViewModelFactory(adminRepository)
                    )
                    val inventoryViewModel: InventoryViewModel = viewModel(
                        factory = InventoryViewModelFactory(productRepository)
                    )

                    val carritoViewModel: CarritoViewModel = viewModel(
                        factory = CarritoViewModelFactory(carritoRepository)
                    )





                    Navigation(
                        navController = navController,
                        catalogViewModel = catalogViewModel,
                        loginViewModel = loginViewModel,
                        inventoryViewModel = inventoryViewModel,
                        carritoViewModel = carritoViewModel

                    )
                }
            }
        }
    }
}

 */

package com.duoc.principedecolores

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.duoc.principedecolores.ui.navigation.Navigation
import com.duoc.principedecolores.ui.theme.PrincipedeColoresTheme
import com.duoc.principedecolores.ui.viewmodel.CarritoViewModel
import com.duoc.principedecolores.ui.viewmodel.CatalogViewModel
import com.duoc.principedecolores.ui.viewmodel.InventoryViewModel
import com.duoc.principedecolores.ui.viewmodel.LoginViewModel
import com.duoc.principedecolores.ui.viewmodel.RegisterViewModel
import com.duoc.principedecolores.ui.viewmodel.LoginClienteViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PrincipedeColoresTheme {
                val navController = rememberNavController()


                val catalogViewModel: CatalogViewModel = viewModel(
                    factory = CatalogViewModelFactory()
                )

                val loginViewModel: LoginViewModel = viewModel(
                    factory = LoginViewModelFactory()
                )

                val inventoryViewModel: InventoryViewModel = viewModel(
                    factory = InventoryViewModelFactory()
                )

                val carritoViewModel: CarritoViewModel = viewModel(
                    factory = CarritoViewModelFactory()
                )

                val registerViewModel: RegisterViewModel = viewModel(
                    factory = RegisterViewModelFactory()
                )

                val loginClienteViewModel: LoginClienteViewModel = viewModel(
                    factory = LoginClienteViewModelFactory()
                )

                Navigation(
                    navController = navController,
                    catalogViewModel = catalogViewModel,
                    loginViewModel = loginViewModel,
                    inventoryViewModel = inventoryViewModel,
                    carritoViewModel = carritoViewModel,
                    registerViewModel = registerViewModel,
                    loginClienteViewModel = loginClienteViewModel
                )
            }
        }
    }
}