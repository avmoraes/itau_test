package br.com.itau.itaunotes.login.data.datasource

import android.content.SharedPreferences
import br.com.itau.itaunotes.login.data.model.User
import com.nhaarman.mockitokotlin2.*
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PrefsDataSourceTest{
    private lateinit var cacheDataSource: CacheDataSourceContract

    @Mock
    private lateinit var prefs: SharedPreferences

    @Before
    fun setUp(){
        cacheDataSource = CacheDataSource(prefs)
    }

    @Test
    fun `Test Cache has User`(){
        whenever(prefs.contains("user_email")).doReturn(true)
        whenever(prefs.contains("user_password")).doReturn(true)

        assertTrue(cacheDataSource.containUserLogin())
    }

    @Test
    fun `Test Cache doesn't has User`(){
        whenever(prefs.contains("user_email")).doReturn(true)
        whenever(prefs.contains("user_password")).doReturn(false)

        assertFalse(cacheDataSource.containUserLogin())
    }

    @Test
    fun `Test save User`(){

        val user = User("email@test.com", "123")
        val editMock = mock<SharedPreferences.Editor>()

        whenever(editMock.putString(any(), any())).doReturn(editMock)
        whenever(prefs.edit()).doReturn(editMock)

        cacheDataSource.saveOrUpdateUser(user)

        verify(editMock).putString("user_email", user.email)
        verify(editMock).putString("user_password", user.password)
    }

    @Test
    fun `Test get User`(){

        val mockEmail = "email@test.com"
        val mockPassword = "123"

        whenever(prefs.getString("user_email", "")).doReturn(mockEmail)
        whenever(prefs.getString("user_password", "")).doReturn(mockPassword)

        val user = cacheDataSource.getUser()

        assertEquals(mockEmail, user.email)
        assertEquals(mockPassword, user.password)

    }
}