package br.com.itau.itaunotes.login.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.itau.itaunotes.login.data.datasource.baseCallBack
import br.com.itau.itaunotes.login.data.model.User
import br.com.itau.itaunotes.login.data.repository.LoginRepositoryContract
import com.nhaarman.mockitokotlin2.*
import junit.framework.Assert.assertEquals
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest{
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Captor
    lateinit var loadingCaptor: ArgumentCaptor<Boolean>
    @Captor
    lateinit var validEmailCaptor: ArgumentCaptor<Boolean>
    @Captor
    lateinit var validPasswordCaptor: ArgumentCaptor<Boolean>
    @Captor
    lateinit var loginCaptor: ArgumentCaptor<Boolean>
    @Captor
    lateinit var shouldSaveUserCaptor: ArgumentCaptor<Boolean>
    @Captor
    lateinit var userInfoCaptor: ArgumentCaptor<User>
    @Captor
    private lateinit var repoSuccessCaptor: ArgumentCaptor<baseCallBack>
    @Captor
    private lateinit var repoErrorCaptor: ArgumentCaptor<baseCallBack>

    @Mock
    private lateinit var repository: LoginRepositoryContract

    private lateinit var viewModel: LoginViewModel

    private val loadingObserver: Observer<Boolean> = mock()
    private val validEmailObserver: Observer<Boolean> = mock()
    private val validPasswordObserver: Observer<Boolean> = mock()
    private val loginObserver: Observer<Boolean> = mock()
    private val shouldSaveUserObserver: Observer<Boolean> = mock()
    private val userInfoObserver: Observer<User?> = mock()

    @Before
    fun setUp(){
        viewModel = LoginViewModel(repository)
        viewModel.isLoading.observeForever(loadingObserver)
        viewModel.validEmail.observeForever(validEmailObserver)
        viewModel.validPassword.observeForever(validPasswordObserver)
        viewModel.logged.observeForever(loginObserver)
        viewModel.saveLogin.observeForever(shouldSaveUserObserver)
        viewModel.user.observeForever(userInfoObserver)
    }

    @Test
    fun `test view model properties`(){
        Assert.assertNotNull(viewModel.isLoading)
        Assert.assertNotNull(viewModel.validEmail)
        Assert.assertNotNull(viewModel.validPassword)
        Assert.assertNotNull(viewModel.logged)
        Assert.assertNotNull(viewModel.saveLogin)
        Assert.assertNotNull(viewModel.user)
    }

    @Test
    fun `test load call`(){
        val user = User("test@email.com", "123")

        whenever(repository.containsLoggedUser()).thenReturn(true)
        whenever(repository.getLogged()).thenReturn(user)

        viewModel.load()

        userInfoCaptor.run {
            verify(userInfoObserver, times(1)).onChanged(capture())
            assertEquals(user, value)
        }
    }

    @Test
    fun `test empty email`(){
        val email = ""
        val password = "123"

        viewModel.login(email, password)

        validEmailCaptor.run {
            verify(validEmailObserver, times(1)).onChanged(capture())
            assertEquals(false, value)
        }
    }

    @Test
    fun `test empty password`(){
        val email = "test@email.com"
        val password = ""

        viewModel.login(email, password)

        validPasswordCaptor.run {
            verify(validPasswordObserver, times(1)).onChanged(capture())
            assertEquals(false, value)
        }
    }

    @Test
    fun `test login success with previous logged user`(){
        val user = User("test@email.com", "123")

        whenever(repository.containsLoggedUser()).thenReturn(true)
        whenever(repository.getLogged()).thenReturn(user)

        viewModel.load()
        viewModel.login(user.email, user.password)

        verify(repository).login(any(), repoSuccessCaptor.capture(), any())
        repoSuccessCaptor.value.invoke()

        loadingCaptor.run {
            verify(loadingObserver, times(2)).onChanged(capture())
        }

        loginCaptor.run {
            verify(loginObserver, times(1)).onChanged(capture())
            assertEquals(true, value)
        }
    }

    @Test
    fun `test login success without previous logged user`(){
        val user = User("test@email.com", "123")

        whenever(repository.containsLoggedUser()).thenReturn(false)

        viewModel.load()
        viewModel.login(user.email, user.password)

        verify(repository).login(any(), repoSuccessCaptor.capture(), any())
        repoSuccessCaptor.value.invoke()

        loadingCaptor.run {
            verify(loadingObserver, times(2)).onChanged(capture())
        }

        shouldSaveUserCaptor.run {
            verify(shouldSaveUserObserver, times(1)).onChanged(capture())
            assertEquals(true, value)
        }
    }

    @Test
    fun `test login error`(){
        val user = User("test@email.com", "123")

        whenever(repository.containsLoggedUser()).thenReturn(false)

        viewModel.load()
        viewModel.login(user.email, user.password)

        verify(repository).login(any(), any(), repoErrorCaptor.capture())
        repoErrorCaptor.value.invoke()

        loadingCaptor.run {
            verify(loadingObserver, times(2)).onChanged(capture())
        }

        loginCaptor.run {
            verify(loginObserver, times(1)).onChanged(capture())
            assertEquals(false, value)
        }
    }

    @Test
    fun `test save user`(){
        val user = User("test@email.com", "123")

        whenever(repository.containsLoggedUser()).thenReturn(true)
        whenever(repository.getLogged()).thenReturn(user)

        viewModel.load()
        viewModel.login(user.email, user.password)

        verify(repository).login(any(), repoSuccessCaptor.capture(), any())
        repoSuccessCaptor.value.invoke()

        viewModel.saveLoggedUser()

        verify(repository).saveLoggedUser(user)

    }
}