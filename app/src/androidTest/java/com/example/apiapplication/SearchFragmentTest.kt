package com.example.apiapplication

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.apiapplication.presentation.ui.activity.MainActivity

import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchFragmentTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun checkSearchFragmentDisplayed() {
        onView(withId(R.id.searchFragment))
            .check(matches(isDisplayed()))
    }

    @Test
    fun checkNavigationToPlayerStatsFragment() {
        onView(withId(R.id.search_bar))
            .perform(typeText("1068042013"), closeSoftKeyboard())
        onView(withId(R.id.search_player_btn)).perform(click())
        val waitingTime: Long = 400
        val idlingResource = ElapsedTimeIdlingResource(waitingTime)
        try {
            IdlingRegistry.getInstance().register(idlingResource)
            onView(withId(R.id.playerstats))
                .check(matches(isDisplayed()))
            onView(withId(R.id.matches_recycler_view))
                .check(matches(isDisplayed()))
            onView(withId(R.id.comments_recycler_view))
                .check(matches(isDisplayed()))
        } finally {
            IdlingRegistry.getInstance().unregister(idlingResource)
        }
    }

    @Test
    fun checkNavigationToMatchStatsFragment() {
        onView(withId(R.id.search_bar))
            .perform(typeText("7137640948"), closeSoftKeyboard())
        onView(withId(R.id.search_match_btn)).perform(click())
        val waitingTime: Long = 400
        val idlingResource = ElapsedTimeIdlingResource(waitingTime)
        try {
            IdlingRegistry.getInstance().register(idlingResource)
            onView(withId(R.id.match_briefing))
                .check(matches(isDisplayed()))
            onView(withId(R.id.radiant_team_recycler_view))
                .check(matches(isDisplayed()))
        } finally {
            IdlingRegistry.getInstance().unregister(idlingResource)
        }
    }
}