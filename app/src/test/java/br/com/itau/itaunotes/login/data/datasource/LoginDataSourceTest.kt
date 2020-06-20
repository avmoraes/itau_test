package br.com.itau.itaunotes.login.data.datasource

import br.com.itau.itaunotes.login.data.model.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
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
    private lateinit var authResultCaptor: ArgumentCaptor<OnCompleteListener<AuthResult>>

    @Mock
    private lateinit var firebaseAuthMock: FirebaseAuth

    @Before
    fun setUp(){
        loginDataSourceContract = FirebaseDataSource(firebaseAuthMock)
    }

    @Test
    fun `test login success`(){
        val user = User("test@test.com", "12345")

        val successCallBackMock = mock<baseCallBack>()
        val errorCallBackMock = mock<baseCallBack>()
        val authTaskMock = mock<Task<AuthResult>>()

        whenever(firebaseAuthMock.signInWithEmailAndPassword(any(), any())).doReturn(authTaskMock)
        whenever(authTaskMock.isSuccessful).doReturn(true)

        loginDataSourceContract.login(user, successCallBackMock, errorCallBackMock)

        verify(authTaskMock).addOnCompleteListener(authResultCaptor.capture())
        authResultCaptor.value.onComplete(authTaskMock)

        verify(successCallBackMock).invoke()
    }

    @Test
    fun `test login Error`(){
        val user = User("test@test.com", "12345")

        val successCallBackMock = mock<baseCallBack>()
        val errorCallBackMock = mock<baseCallBack>()
        val authTaskMock = mock<Task<AuthResult>>()

        whenever(firebaseAuthMock.signInWithEmailAndPassword(any(), any())).doReturn(authTaskMock)
        whenever(authTaskMock.isSuccessful).doReturn(false)

        loginDataSourceContract.login(user, successCallBackMock, errorCallBackMock)

        verify(authTaskMock).addOnCompleteListener(authResultCaptor.capture())
        authResultCaptor.value.onComplete(authTaskMock)

        verify(errorCallBackMock).invoke()
    }
}