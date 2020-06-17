package br.com.itau.itaunotes.login.data.repository

import br.com.itau.itaunotes.login.data.datasource.CacheDataSourceContract
import br.com.itau.itaunotes.login.data.datasource.LoginDataSourceContract
import br.com.itau.itaunotes.login.data.datasource.baseBaseCallBack
import br.com.itau.itaunotes.login.domain.User

interface LoginRepositoryContract{
    fun login(request: User,
              successCallBack: baseBaseCallBack,
              errorCallBack: baseBaseCallBack
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
        successCallBack: baseBaseCallBack,
        errorCallBack: baseBaseCallBack
    ) {
        loginDataSource.login(request, successCallBack, errorCallBack)
    }

    override fun getLogged(): User = cacheDataSource.getUser()

    override fun containsLoggedUser(): Boolean = cacheDataSource.containUserLogin()

    override fun saveLoggedUser(user: User) = cacheDataSource.saveOrUpdateUser(user)
}