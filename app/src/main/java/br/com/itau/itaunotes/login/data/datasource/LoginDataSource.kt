package br.com.itau.itaunotes.login.data.datasource

import br.com.itau.itaunotes.login.data.datasource.auth.AuthContract
import br.com.itau.itaunotes.login.data.model.User

typealias baseCallBack = () -> Unit

interface LoginDataSourceContract{
    fun login(
        loginRequest: User,
        successCallBack: baseCallBack?,
        errorCallBack: baseCallBack?
    )
}

class FirebaseDataSource(
    private val auth: AuthContract
): LoginDataSourceContract {

   override fun login(
        loginRequest: User,
        successCallBack: baseCallBack?,
        errorCallBack: baseCallBack?
    ) {
        auth.loginByEmail(
            loginRequest.email,
            loginRequest.password,{
                successCallBack?.invoke()
            },{
                errorCallBack?.invoke()
            })
    }
}