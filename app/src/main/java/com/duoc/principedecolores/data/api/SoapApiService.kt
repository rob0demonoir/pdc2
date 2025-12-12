package com.duoc.principedecolores.data.api

import com.duoc.principedecolores.data.model.Product
import com.duoc.principedecolores.data.remote.dto.AddToCartRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface SoapApiService {

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ResponseBody>


    @GET("api/products")
    suspend fun getProducts(): Response<List<Product>>

    @POST("api/products")
    suspend fun createProduct(@Body product: Product): Response<Product>

    @PUT("api/products/{id}")
    suspend fun updateProduct(@Path("id") id: Int, @Body product: Product): Response<Product>

    @DELETE("api/products/{id}")
    suspend fun deleteProduct(@Path("id") id: Int): Response<Void>

    @GET("api/carrito")
    suspend fun getCarrito(): Response<List<CarritoResponse>> // Ojo: devuelve CarritoResponse

    @POST("api/carrito/agregar")
    suspend fun addToCart(@Body request: AddToCartRequest): Response<CarritoResponse>

    @DELETE("api/carrito/eliminar/{id}")
    suspend fun deleteCartItem(@Path("id") id: Int): Response<Void>

    @DELETE("api/carrito")
    suspend fun clearCart(): Response<Void>

    @POST("api/clientes/registro")
    suspend fun registrarCliente(@Body request: RegistroClienteRequest): Response<ResponseBody>

    @GET("api/carrito")
    suspend fun getCarrito(@Query("clienteId") clienteId: Int): Response<CarritoCompletoResponse>


    @POST("api/carrito/agregar")
    suspend fun addToCart(@Body request: AnadirAlCarritoRequest): Response<ResponseBody>

    @POST("api/carrito/pagar")
    suspend fun procesarPago(@Query("clienteId") clienteId: Int): Response<ResponseBody>

    @POST("api/clientes/login")
    suspend fun loginCliente(@Body request: LoginClienteRequest): Response<LoginClienteResponse>
}