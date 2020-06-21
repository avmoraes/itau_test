package br.com.itau.itaunotes.login.di

import br.com.itau.itaunotes.login.data.datasource.CacheDataSourceContract
import br.com.itau.itaunotes.login.data.datasource.LoginDataSourceContract
import br.com.itau.itaunotes.login.data.repository.LoginRepositoryContract
import br.com.itau.itaunotes.mocks.firebaseMockModule
import br.com.itau.itaunotes.mocks.prefsMockModule
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.test.AutoCloseKoinTest
import org.koin.test.inject

class LoginModuleTest: AutoCloseKoinTest(){

    @Before
    fun setup(){
        startKoin{
            modules(prefsMockModule, firebaseMockModule, loginDependenciesModule)
        }
    }

    @Test
    fun `Assert If LoginDataSourceContract Is provided by DI`(){
        val loginDataSource by inject<LoginDataSourceContract>()
        assertNotNull(loginDataSource)
    }

    @Test
    fun `Assert If CacheDataSourceContract Is provided by DI`(){
        val cacheDataSource by inject<CacheDataSourceContract>()
        assertNotNull(cacheDataSource)
    }

    @Test
    fun `Assert If LoginRepositoryContract Is provided by DI`(){
        val repository by inject<LoginRepositoryContract>()
        assertNotNull(repository)
    }
}