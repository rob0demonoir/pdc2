/**
 REPOSITORY PARA ALMACENAMIENTO DE DATOS EN LOCAL

package com.duoc.principedecolores.data.repository

import com.duoc.principedecolores.data.dao.AdminDao
import com.duoc.principedecolores.data.model.Admin

class AdminRepository(private val adminDao: AdminDao) {
    
    suspend fun login(username: String, password: String): Admin? {
        return adminDao.login(username, password)
    }
    
    suspend fun insertAdmin(admin: Admin) {
        adminDao.insertAdmin(admin)
    }
    
    suspend fun hasAdmins(): Boolean {
        return adminDao.getAdminCount() > 0
    }
}**/

package com.duoc.principedecolores.data.repository

import android.util.Log
import com.duoc.principedecolores.data.api.LoginRequest
import com.duoc.principedecolores.data.api.RetrofitClient
import com.duoc.principedecolores.data.model.Admin

class AdminRepository {

    suspend fun login(username: String, pass: String): Admin? {
        return try {
            val request = LoginRequest(username, pass)
            val response = RetrofitClient.apiService.login(request)

            if (response.isSuccessful) {
                Admin(id = 1, username = username, password = "")
            } else {
                Log.e("Login", "Fallo login: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("Login", "Error de red", e)
            null
        }
    }

}