package br.com.itau.itaunotes.login.data.repository

import br.com.itau.itaunotes.login.data.datasource.CacheDataSourceContract
import br.com.itau.itaunotes.login.data.datasource.LoginDataSourceContract
import br.com.itau.itaunotes.login.data.datasource.baseCallBack
import br.com.itau.itaunotes.login.data.model.User
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginRepositoryTest{

    private lateinit var repository: LoginRepositoryContract

    @Mock
    private lateinit var loginDataSource: LoginDataSourceContract
    @Mock
    private lateinit var cacheDataSource: CacheDataSourceContract
    @Captor
    private lateinit var loginDSSuccessCaptor: ArgumentCaptor<baseCallBack>
    @Captor
    private lateinit var loginDSErrorCaptor: ArgumentCaptor<baseCallBack>

    @Before
    fun setUp(){
        repository = LoginRepository(loginDataSource, cacheDataSource)
    }

    @Test
    fun `Test Execute Login with Success Response`(){
        val mockSuccessCallback = mock<baseCallBack>()
        val mockErrorCallback = mock<baseCallBack>()

        val user = User("email@test.com", "123")

        repository.login(user, mockSuccessCallback, mockErrorCallback)

        verify(loginDataSource).login(any(), loginDSSuccessCaptor.capture(), any())
        loginDSSuccessCaptor.value.invoke()

        verify(mockSuccessCallback).invoke()
    }

    @Test
    fun `Test Execute Login with Error Response`(){
        val mockSuccessCallback = mock<baseCallBack>()
        val mockErrorCallback = mock<baseCallBack>()

        val user = User("email@test.com", "123")

        repository.login(user, mockSuccessCallback, mockErrorCallback)

        verify(loginDataSource).login(any(), any(), loginDSErrorCaptor.capture())
        loginDSErrorCaptor.value.invoke()

        verify(mockErrorCallback).invoke()
    }

    @Test
    fun `Test get Logged User`(){
        val mockUser = User("email@test.com", "123")

        whenever(cacheDataSource.getUser()).doReturn(mockUser)

        val user = repository.getLogged()

        assertNotNull(user)
        assertEquals(mockUser.email, user.email)
        assertEquals(mockUser.password, user.password)
    }

    @Test
    fun `Test Contains Logged User`(){
        whenever(cacheDataSource.containUserLogin()).doReturn(true)
        assertTrue(repository.containsLoggedUser())
    }

    @Test
    fun `Test doesn't Contains Logged User`(){
        whenever(cacheDataSource.containUserLogin()).doReturn(false)
        assertFalse(repository.containsLoggedUser())
    }

    @Test
    fun `Test Save Logged User`(){
        val mockUser = User("email@test.com", "123")
        repository.saveLoggedUser(mockUser)

        verify(cacheDataSource).saveOrUpdateUser(mockUser)
    }
}