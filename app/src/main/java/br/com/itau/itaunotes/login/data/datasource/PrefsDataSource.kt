package br.com.itau.itaunotes.login.data.datasource

import android.content.SharedPreferences
import br.com.itau.itaunotes.login.domain.User

const val USER_KEY = "user_login"

interface CacheDataSourceContract{
    fun getUser():User
    fun saveOrUpdateUser(user: User)
    fun containUserLogin(): Boolean
}

class CacheDataSource(
    private val sharedPreferences: SharedPreferences
): CacheDataSourceContract {

    private val userEmail = "user_email"
    private val userPassword = "user_password"

    override fun getUser(): User {
        val email = sharedPreferences.getString(userEmail, "") ?: ""
        val password = sharedPreferences.getString(userPassword, "") ?: ""

        return User(email, password)
    }

    override fun saveOrUpdateUser(user: User) {
        sharedPreferences.edit().putString(userEmail, user.email).apply()
        sharedPreferences.edit().putString(userPassword, user.password).apply()
    }

    override fun containUserLogin(): Boolean {
        return sharedPreferences.contains(userEmail) && sharedPreferences.contains(userPassword)
    }
}