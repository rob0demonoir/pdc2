/**
 *
 PARA LOCAL
 package com.duoc.principedecolores

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.duoc.principedecolores.data.repository.AdminRepository
import com.duoc.principedecolores.data.repository.ProductRepository
import com.duoc.principedecolores.data.repository.CarritoRepository
import com.duoc.principedecolores.ui.viewmodel.CatalogViewModel
import com.duoc.principedecolores.ui.viewmodel.InventoryViewModel
import com.duoc.principedecolores.ui.viewmodel.LoginViewModel
import com.duoc.principedecolores.ui.viewmodel.CarritoViewModel


class CatalogViewModelFactory(
    private val productRepository: ProductRepository,
    private val carritoRepository: CarritoRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CatalogViewModel::class.java)) {
            return CatalogViewModel(productRepository, carritoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class LoginViewModelFactory(
    private val adminRepository: AdminRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(adminRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class InventoryViewModelFactory(
    private val productRepository: ProductRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            return InventoryViewModel(productRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class CarritoViewModelFactory(
    private val carritoRepository: CarritoRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CarritoViewModel::class.java)) {
            return CarritoViewModel(carritoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

**/

package com.duoc.principedecolores

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.duoc.principedecolores.data.repository.*
import com.duoc.principedecolores.ui.viewmodel.*


class CatalogViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return CatalogViewModel(ProductRepository(), CarritoRepository()) as T
    }
}

class LoginViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(AdminRepository()) as T
    }
}

class InventoryViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InventoryViewModel(ProductRepository()) as T
    }
}

class CarritoViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CarritoViewModel(CarritoRepository()) as T
    }
}

class LoginClienteViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginClienteViewModel::class.java)) {
            return LoginClienteViewModel(ClienteRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class RegisterViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {

            return RegisterViewModel(ClienteRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}