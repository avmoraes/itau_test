package br.com.itau.itaunotes.login.presentation.view

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import br.com.itau.itaunotes.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class LoginActivityTest{
    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

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
    fun test_login_button_press(){
        onView(withId(R.id.loginEditText)).check(matches(isDisplayed()))
        onView(withId(R.id.passwordEditText)).check(matches(isDisplayed()))

        onView(withId(R.id.loginEditText)).perform(replaceText(""))
        onView(withId(R.id.passwordEditText)).perform(replaceText(""))

        onView(withId(R.id.loginEditText)).perform(typeText("n@desafioitau.com"))
        onView(withId(R.id.passwordEditText)).perform(typeText("admin123"))

        onView(withId(R.id.loginButton)).check(matches(isDisplayed()))
        onView(withId(R.id.loginButton)).check(matches(withText(R.string.login_bt_text)))

        onView(withId(R.id.loginButton)).perform(click())

        onView(withId(R.id.loadingView)).check(matches(isDisplayed()))
    }
}