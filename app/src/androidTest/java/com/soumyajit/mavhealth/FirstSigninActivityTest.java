package com.soumyajit.mavhealth;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class FirstSigninActivityTest {

    @Before
    public void setUp() throws Exception {
        ActivityScenario.launch(FirstSigninActivity.class);
    }

    @Test
    public void testFirstSigninActivity() {
        Espresso.onView(ViewMatchers.withId(R.id.firstSignFullName))
                .perform(ViewActions.typeText("John Doe"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.firstSignBirthDay))
                .perform(ViewActions.typeText("01/01/1990"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.firstSignTel))
                .perform(ViewActions.typeText("1234567890"), ViewActions.closeSoftKeyboard());

        // Click the confirm button
        Espresso.onView(ViewMatchers.withId(R.id.confirmeBtn)).perform(ViewActions.click());
    }
}