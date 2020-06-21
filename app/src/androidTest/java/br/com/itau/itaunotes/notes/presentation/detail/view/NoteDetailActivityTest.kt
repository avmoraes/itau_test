package br.com.itau.itaunotes.notes.presentation.detail.view

import android.app.Activity
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.ActivityResultMatchers.hasResultCode
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import br.com.itau.itaunotes.R
import br.com.itau.itaunotes.tools.matchers.ToastMatcher
import br.com.itau.itaunotes.tools.mocks.createNote
import org.hamcrest.CoreMatchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class NoteDetailActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(NoteDetailActivity::class.java)

    @Test
    fun test_if_activity_is_visible(){
        onView(ViewMatchers.withId(R.id.noteTitle)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.noteSummary)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.notePrioritySpinner)).check(
            ViewAssertions.matches(
                ViewMatchers.withSpinnerText(CoreMatchers.containsString("1"))
            )
        )
        onView(ViewMatchers.withId(R.id.noteContent)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun check_empty_title_note(){
        val newNote = createNote(3)

        onView(ViewMatchers.withId(R.id.noteSummaryText)).perform(ViewActions.replaceText(newNote.summary))
        onView(ViewMatchers.withId(R.id.noteContentText)).perform(ViewActions.replaceText(newNote.description))

        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
        onView(ViewMatchers.withText(R.string.notes_detail_save)).perform(ViewActions.click())

        onView(ViewMatchers.withText(R.string.notes_detail_empty_title)).inRoot(ToastMatcher()).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
    }
}