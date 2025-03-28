package com.i220965_i222427_rehantariq_arshmankhawar

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.arshman_rehan.i222427_i220965.*
import org.hamcrest.CoreMatchers.containsString
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ConnectMeEspressoTests {

    // Test 1: Validate Login Functionality
    @get:Rule
    val loginActivityRule = ActivityScenarioRule(LogInPage::class.java)

    @Test
    fun testLoginSuccess() {
        // Type in email and password then click login
        onView(withId(R.id.etEmail)).perform(typeText("test@example.com"))
        onView(withId(R.id.etPassword)).perform(typeText("password123"))
        closeSoftKeyboard()
        onView(withId(R.id.myBtn)).perform(click())
        // Check that HomeActivity is launched (assume chatRecyclerView exists on HomeActivity)
        onView(withId(R.id.postRecyclerView))
            .check(matches(isDisplayed()))
    }

    // Test 2: Validate Message Sending in ChatActivity
    @Test
    fun testMessageSending() {
        ActivityScenario.launch(ChatPage::class.java).use {
            // Type a message into the message input
            onView(withId(R.id.messageInput)).perform(typeText("Hello Espresso Test"))
            closeSoftKeyboard()
            // Click the send button
            onView(withId(R.id.sendButton)).perform(click())
            // Verify the message appears in the RecyclerView
            onView(withId(R.id.messagesRecyclerView))
                .check(matches(hasDescendant(withText(containsString("Hello Espresso Test")))))
        }
    }

    // Test 3: Validate Follow Request in ProfileActivity
//    @Test
//    fun testFollowRequest() {
//        ActivityScenario.launch(ProfilePage::class.java).use {
//            // Click the follow button (assumed ID: btnFollow)
//            onView(withId(R.id.btnFollow)).perform(click())
//            // Verify that a toast with "Follow request sent" is shown
//            onView(withText(containsString("Follow request sent")))
//                .inRoot(ToastMatcher())
//                .check(matches(isDisplayed()))
//        }
//    }
}
