package com.udacity.gradle.builditbigger;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.javajokes.JavaJoke;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)

public class JokeBackendTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testVerifyJokeNoEmpty() {
        onView(withId(R.id.btn_tell_joke)).perform(click());
        onView(withId(R.id.tv_joke)).check(matches(not(withText(""))));
    }

    //Just for testing at this stage, right now there is only 1 joke so it should work!
    @Test
    public void testVerifyJoke() {
        onView(withId(R.id.btn_tell_joke)).perform(click());
        onView(withId(R.id.tv_joke)).check(matches(withText(JavaJoke.tell())));
    }

}