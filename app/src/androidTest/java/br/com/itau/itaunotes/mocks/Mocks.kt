package br.com.itau.itaunotes.mocks

import br.com.itau.itaunotes.login.data.datasource.auth.AuthContract
import br.com.itau.itaunotes.login.data.datasource.baseCallBack
import org.koin.dsl.module

internal val mockedAuth = module {
    factory<AuthContract> { MockAuth() }
}

class MockAuth: AuthContract{
    override fun loginByEmail(
        email: String,
        password: String,
        success: baseCallBack?,
        error: baseCallBack?
    ) {
        when(email){
            "notaspessoais@desafioitau.com" ->success?.invoke()
            else -> error?.invoke()
        }
    }
}

