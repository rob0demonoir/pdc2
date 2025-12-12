package com.duoc.principedecolores.data.repository

import android.util.Log
import com.duoc.principedecolores.data.model.Cliente
import com.duoc.principedecolores.data.api.RegistroClienteRequest
import com.duoc.principedecolores.data.api.LoginClienteRequest
import com.duoc.principedecolores.data.api.RetrofitClient



class ClienteRepository {

    suspend fun registrarCliente(nombre: String, email: String, pass: String): Result<String> {
        return try {
            val request = RegistroClienteRequest(nombre, email, pass)
            val response = RetrofitClient.apiService.registrarCliente(request)

            if (response.isSuccessful) {
                Result.success("Registro exitoso")
            } else {

                val errorMsg = response.errorBody()?.string() ?: "Error en el registro"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e("Registro", "Error conexión", e)
            Result.failure(e)
        }
    }

    suspend fun loginCliente(email: String, pass: String): Result<Cliente> {
        return try {
            val request = LoginClienteRequest(email, pass)
            val response = RetrofitClient.apiService.loginCliente(request)

            if (response.isSuccessful && response.body() != null) {


                val loginResponse = response.body()!!

                Result.success(loginResponse.cliente)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Credenciales inválidas"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e("Login", "Error conexión", e)
            Result.failure(e)
        }
    }
}

