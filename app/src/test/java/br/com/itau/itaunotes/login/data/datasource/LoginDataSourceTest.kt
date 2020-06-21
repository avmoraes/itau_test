package br.com.itau.itaunotes.login.data.datasource

import br.com.itau.itaunotes.login.data.datasource.auth.AuthContract
import br.com.itau.itaunotes.login.data.model.User
import com.nhaarman.mockitokotlin2.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginDataSourceTest: AutoCloseKoinTest(){

    private lateinit var loginDataSourceContract: LoginDataSourceContract

    @Captor
    private lateinit var authSuccessCaptor: ArgumentCaptor<baseCallBack>

    @Captor
    private lateinit var authErrorCaptor: ArgumentCaptor<baseCallBack>

    @Mock
    private lateinit var firebaseAuthMock: AuthContract

    @Before
    fun setUp(){
        loginDataSourceContract = FirebaseDataSource(firebaseAuthMock)
    }

    @Test
    fun `test login success`(){
        val user = User("test@test.com", "12345")

        val successCallBackMock = mock<baseCallBack>()
        val errorCallBackMock = mock<baseCallBack>()

        loginDataSourceContract.login(user, successCallBackMock, errorCallBackMock)
        verify(firebaseAuthMock).loginByEmail(any(), any(), authSuccessCaptor.capture(), any())
        authSuccessCaptor.value.invoke()

        verify(successCallBackMock).invoke()
    }

    @Test
    fun `test login Error`(){
        val user = User("test@test.com", "12345")

        val successCallBackMock = mock<baseCallBack>()
        val errorCallBackMock = mock<baseCallBack>()

        loginDataSourceContract.login(user, successCallBackMock, errorCallBackMock)
        verify(firebaseAuthMock).loginByEmail(any(), any(), any(), authErrorCaptor.capture())
        authErrorCaptor.value.invoke()

        verify(errorCallBackMock).invoke()
    }
}