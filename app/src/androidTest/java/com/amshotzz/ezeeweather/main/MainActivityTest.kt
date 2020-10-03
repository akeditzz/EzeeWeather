package com.amshotzz.ezeeweather.main


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.amshotzz.ezeeweather.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private var mIdlingResource: IdlingResource? = null

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setUp() {
        mIdlingResource = mActivityTestRule.activity.getIdlingResource()
        IdlingRegistry.getInstance().register(mIdlingResource)
    }

    @Test
    fun mainActivityTest() {
        val appCompatEditText = onView(
            allOf(
                withId(R.id.etSearchCity),
                childAtPosition(
                    allOf(
                        withId(R.id.search_layout),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText.perform(replaceText("Pune"), closeSoftKeyboard())

        val appCompatButton = onView(
            allOf(
                withId(R.id.btnSearch), withText("Search"),
                childAtPosition(
                    allOf(
                        withId(R.id.search_layout),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatButton.perform(click())

        Thread.sleep(3000)

        val actionMenuItemView = onView(
            allOf(
                withId(R.id.action_search), withContentDescription("Search"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.action_bar),
                        2
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        actionMenuItemView.perform(click())

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.etSearchCity), withText("Pune"),
                childAtPosition(
                    allOf(
                        withId(R.id.search_layout),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText2.perform(replaceText("Mumbai"))

        val appCompatEditText3 = onView(
            allOf(
                withId(R.id.etSearchCity), withText("Mumbai"),
                childAtPosition(
                    allOf(
                        withId(R.id.search_layout),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText3.perform(closeSoftKeyboard())

        val appCompatEditText4 = onView(
            allOf(
                withId(R.id.etSearchCity), withText("Mumbai"),
                childAtPosition(
                    allOf(
                        withId(R.id.search_layout),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText4.perform(pressImeActionButton())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
