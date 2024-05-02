package com.soumyajit.mavhealth;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityFlowTest {

    @Before
    public void setUp() throws Exception {
        ActivityScenario.launch(MainActivity.class);
    }

    @Test
    public void testMainActivityFlow() {
        Espresso.onView(ViewMatchers.withId(R.id.CreateAccount)).perform(ViewActions.click());
        // Fill up the form in MainActivity
        Espresso.onView(ViewMatchers.withId(R.id.editText))
                .perform(ViewActions.typeText("testnew@gmail.com"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editText2))
                .perform(ViewActions.typeText("sxm1234"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editText3))
                .perform(ViewActions.typeText("sxm1234"), ViewActions.closeSoftKeyboard());

        // Click the login button
        Espresso.onView(ViewMatchers.withId(R.id.SignUpBtn)).perform(ViewActions.click());
    }
}