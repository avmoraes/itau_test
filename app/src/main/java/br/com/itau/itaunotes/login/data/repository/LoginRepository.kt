package br.com.itau.itaunotes.login.data.repository

import br.com.itau.itaunotes.login.data.datasource.CacheDataSourceContract
import br.com.itau.itaunotes.login.data.datasource.LoginDataSourceContract
import br.com.itau.itaunotes.login.data.datasource.baseCallBack
import br.com.itau.itaunotes.login.data.model.User

interface LoginRepositoryContract{
    fun login(request: User,
              successCallBack: baseCallBack?,
              errorCallBack: baseCallBack?
    )
    fun getLogged(): User
    fun containsLoggedUser(): Boolean
    fun saveLoggedUser(user: User)
}

class LoginRepository(
    private val loginDataSource: LoginDataSourceContract,
    private val cacheDataSource: CacheDataSourceContract
): LoginRepositoryContract {
    override fun login(
        request: User,
        successCallBack: baseCallBack?,
        errorCallBack: baseCallBack?
    ) {
        loginDataSource.login(request, {
            successCallBack?.invoke()
        }, {
            errorCallBack?.invoke()
        })
    }

    override fun getLogged(): User = cacheDataSource.getUser()

    override fun containsLoggedUser(): Boolean = cacheDataSource.containUserLogin()

    override fun saveLoggedUser(user: User) = cacheDataSource.saveOrUpdateUser(user)
}