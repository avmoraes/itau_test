package br.com.itau.itaunotes.commons.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.itau.itaunotes.login.data.datasource.CacheDataSource
import br.com.itau.itaunotes.login.data.datasource.FirebaseDataSource
import br.com.itau.itaunotes.login.data.datasource.USER_KEY
import br.com.itau.itaunotes.login.data.repository.LoginRepository
import br.com.itau.itaunotes.login.presentation.viewmodel.LoginViewModel

class ViewModelFactory(
    private val context: Context
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return with(modelClass) {
            when {
                isAssignableFrom(LoginViewModel::class.java) -> {

                    val sharedPrefs = context.getSharedPreferences(USER_KEY, Context.MODE_PRIVATE)
                    val fireBaseDataSource = FirebaseDataSource()
                    val cacheDataSource = CacheDataSource(sharedPrefs)

                    val repository = LoginRepository(fireBaseDataSource, cacheDataSource)
                    LoginViewModel(repository)
                }
                else ->
                    throw IllegalArgumentException("Classe desconhecida.")
            }
        } as T
    }
}