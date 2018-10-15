package pe.com.lycsoftware.cibertecproject;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class MenuActivityTest
{
    @Rule
    public ActivityTestRule<MenuActivity> activityTestRule = new ActivityTestRule<>(MenuActivity.class);
    @Rule
    public IntentsTestRule<MenuActivity> intentsTestRule = new IntentsTestRule<>((MenuActivity.class));

    @Before
    public void setUp()
            throws Exception
    {
    }

    public void testUserResult() {
        //onView()
    }
}