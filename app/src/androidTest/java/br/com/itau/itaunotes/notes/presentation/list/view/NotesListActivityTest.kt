package br.com.itau.itaunotes.notes.presentation.list.view

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import br.com.itau.itaunotes.R
import br.com.itau.itaunotes.tools.assertions.RecyclerViewItemCountAssertion
import br.com.itau.itaunotes.tools.mocks.createNote
import org.hamcrest.CoreMatchers.containsString
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class NotesListActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(NotesListActivity::class.java)

    @Test
    fun test_if_activity_is_visible(){
        onView(withId(R.id.notesList))
            .check(matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun test_click_logout(){
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText(R.string.notes_menu_logout)).perform(click())
        onView(withId(R.id.loginTittle)).check(matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun test_click_delete_all_note(){
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText(R.string.notes_menu_exclude_all)).perform(click())
        onView(withId(R.id.notesList)).check(RecyclerViewItemCountAssertion(0))
    }

    @Test
    fun test_click_add_note(){
        onView(withId(R.id.addNoteButton)).perform(click())
        onView(withId(R.id.noteTitle)).check(matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun test_click_on_list_item(){
        onView(withId(R.id.notesList)).perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        val note = createNote(1)

        onView(withId(R.id.noteTitle)).check(matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.noteTitleText)).check(matches(withText(note.title)))

        onView(withId(R.id.noteSummary)).check(matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.noteSummaryText)).check(matches(withText(note.summary)))

        onView(withId(R.id.notePrioritySpinner)).check(matches(withSpinnerText(containsString("1"))))

       onView(withId(R.id.noteContent)).check(matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.noteContentText)).check(matches(withText(note.description)))
    }
}