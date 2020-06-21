package br.com.itau.itaunotes.login.data.datasource

import android.content.SharedPreferences
import br.com.itau.itaunotes.login.data.model.User

const val USER_SHARED = "user_login"
const val USER_EMAIL_KEY = "user_email"
const val USER_PASSWORD_KEY = "user_password"

interface CacheDataSourceContract{
    fun getUser(): User
    fun saveOrUpdateUser(user: User)
    fun containUserLogin(): Boolean
}

class CacheDataSource(
    private val sharedPreferences: SharedPreferences
): CacheDataSourceContract {

    override fun getUser(): User {
        val email = sharedPreferences.getString(USER_EMAIL_KEY, "") ?: ""
        val password = sharedPreferences.getString(USER_PASSWORD_KEY, "") ?: ""

        return User(email, password)
    }

    override fun saveOrUpdateUser(user: User) {
        sharedPreferences.edit().putString(USER_EMAIL_KEY, user.email).apply()
        sharedPreferences.edit().putString(USER_PASSWORD_KEY, user.password).apply()
    }

    override fun containUserLogin(): Boolean {
        return sharedPreferences.contains(USER_EMAIL_KEY) && sharedPreferences.contains(USER_PASSWORD_KEY)
    }
}