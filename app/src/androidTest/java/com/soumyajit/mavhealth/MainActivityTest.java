package com.soumyajit.mavhealth;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testLoginWithValidCredentials() {
        // Type valid email
        onView(withId(R.id.editText)).perform(typeText("test@gmail.com"), closeSoftKeyboard());

        // Type valid password
        onView(withId(R.id.editText2)).perform(typeText("sxm1234"), closeSoftKeyboard());

        // Click the login button
        onView(withId(R.id.LoginBtn)).perform(click());
    }

    @Test
    public void testLoginWithDoctorValidCredentials() {
        // Type valid email
        onView(withId(R.id.editText)).perform(typeText("testdocotor@gmail.com"), closeSoftKeyboard());

        // Type valid password
        onView(withId(R.id.editText2)).perform(typeText("test123"), closeSoftKeyboard());

        // Click the login button
        onView(withId(R.id.LoginBtn)).perform(click());
    }

    @Test
    public void testLoginWithInvalidCredentials() {
        // Type invalid email
        onView(withId(R.id.editText)).perform(typeText("invalid@gmail.com"), closeSoftKeyboard());

        // Type invalid password
        onView(withId(R.id.editText2)).perform(typeText("invalidpassword"), closeSoftKeyboard());

        // Click the login button
        onView(withId(R.id.LoginBtn)).perform(click());


    }
}
