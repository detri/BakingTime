package com.detri.bakingtime;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.detri.bakingtime.network.InitialRequestIdlingResource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RecipeActivityInstrumentedTest {
    @Rule
    public ActivityScenarioRule<RecipeActivity> activityScenarioRule = new ActivityScenarioRule<>(RecipeActivity.class);

    @Before
    public void setUp() {
        IdlingRegistry.getInstance().register(InitialRequestIdlingResource.getIdlingResource());
    }

    @After
    public void tearDown() {
        IdlingRegistry.getInstance().unregister(InitialRequestIdlingResource.getIdlingResource());
    }

    @Test
    public void clickRecipe_RecipeStepsAreDisplayed() {
        clickRecipe();

        Espresso.onView(ViewMatchers.withId(R.id.rv_recipe_step_list))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void clickRecipeStep_VideoIsDisplayed() {
        clickRecipe();
        clickRecipeStep();

        Espresso.onView(ViewMatchers.withId(R.id.exo_player))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    public void clickRecipe() {
        Espresso.onView(ViewMatchers.withId(R.id.rv_recipe_list))
                .perform(RecyclerViewActions.scrollToPosition(0))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.click()));
    }

    public void clickRecipeStep() {
        Espresso.onView(ViewMatchers.withId(R.id.rv_recipe_step_list))
                .perform(RecyclerViewActions.scrollToPosition(0))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.click()));
    }
}
