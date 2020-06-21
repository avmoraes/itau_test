package br.com.itau.itaunotes.login.presentation.view

import android.content.Context
import android.content.SharedPreferences
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import br.com.itau.itaunotes.R
import br.com.itau.itaunotes.login.data.datasource.USER_EMAIL_KEY
import br.com.itau.itaunotes.login.data.datasource.USER_PASSWORD_KEY
import br.com.itau.itaunotes.login.data.datasource.USER_SHARED
import br.com.itau.itaunotes.tools.matchers.ToastMatcher
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class LoginActivityTest{
    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)
    lateinit var sharedPreferences: SharedPreferences

    @Before
    fun setUp(){
        sharedPreferences = getInstrumentation().targetContext.getSharedPreferences(USER_SHARED, Context.MODE_PRIVATE)
    }

    @After
    fun tearDown(){
        clearShared()
    }

    @Test
    fun test_if_activity_is_visible(){
        onView(withId(R.id.loginTittle)).check(matches(isDisplayed()))
    }

    @Test
    fun test_text_title(){
        onView(withId(R.id.loginTittle)).check(matches(isDisplayed()))
        onView(withId(R.id.loginTittle)).check(matches(withText(R.string.app_name)))
    }

    @Test
    fun test_login_edit_text(){
        onView(withId(R.id.loginEditText)).check(matches(isDisplayed()))
        onView(withId(R.id.loginEditText)).check(matches(withHint(R.string.email_ed_hint_text)))
    }

    @Test
    fun test_password_edit_text(){
        onView(withId(R.id.passwordEditText)).check(matches(isDisplayed()))
        onView(withId(R.id.passwordEditText)).check(matches(withHint(R.string.password_ed_hint_text)))
    }

    @Test
    fun test_login_button(){
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()))
        onView(withId(R.id.loginButton)).check(matches(withText(R.string.login_bt_text)))
    }

    @Test
    fun test_login_Error(){
        setShared()

        onView(withId(R.id.loginEditText)).perform(replaceText("bla@desafioitau.com"))
        onView(withId(R.id.passwordEditText)).perform(replaceText("admin123"))

        onView(withId(R.id.loginButton)).perform(click())

        onView(withText(R.string.login_error_text)).inRoot(ToastMatcher()).check(matches(isDisplayed()))
    }

    @Test
    fun test_empty_email_error(){
        setShared()

        onView(withId(R.id.loginEditText)).perform(replaceText(""))
        onView(withId(R.id.passwordEditText)).perform(replaceText("admin123"))

        onView(withId(R.id.loginButton)).perform(click())

        onView(withText(R.string.login_empty_email_text)).inRoot(ToastMatcher()).check(matches(isDisplayed()))
    }

    @Test
    fun test_empty_password_error(){
        setShared()

        onView(withId(R.id.loginEditText)).perform(replaceText("notaspessoais@desafioitau.com"))
        onView(withId(R.id.passwordEditText)).perform(replaceText(""))

        onView(withId(R.id.loginButton)).perform(click())

        onView(withText(R.string.login_empty_password_text)).inRoot(ToastMatcher()).check(matches(isDisplayed()))
    }

    @Test
    fun test_show_save_user_dialog(){

        clearShared()

        onView(withId(R.id.loginEditText)).perform(replaceText("notaspessoais@desafioitau.com"))
        onView(withId(R.id.passwordEditText)).perform(replaceText("admin123"))

        onView(withId(R.id.loginButton)).perform(click())

        onView(withText(R.string.login_save_info_alert_content)).check(matches(isDisplayed()))
    }

    @Test
    fun test_click_ok_save_user_dialog(){
        clearShared()

        onView(withId(R.id.loginEditText)).perform(replaceText("notaspessoais@desafioitau.com"))
        onView(withId(R.id.passwordEditText)).perform(replaceText("admin123"))

        onView(withId(R.id.loginButton)).perform(click())
        onView(withText(R.string.login_save_info_alert_content)).check(matches(isDisplayed()))
        onView(withId(android.R.id.button1)).perform(click())

        val emailSaved = sharedPreferences.getString(USER_EMAIL_KEY,"")

        assertEquals("notaspessoais@desafioitau.com", emailSaved)
    }

    private fun clearShared(){
        val editor = sharedPreferences.edit()
        editor.clear().apply()
        editor.commit()
    }

    private fun setShared(){
        val editor = sharedPreferences.edit()

        editor.putString(USER_EMAIL_KEY, "notaspessoais@desafioitau.com").apply()
        editor.putString(USER_PASSWORD_KEY, "admin123").apply()

        editor.commit()
    }
}