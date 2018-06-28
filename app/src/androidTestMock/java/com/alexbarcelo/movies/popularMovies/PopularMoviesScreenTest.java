package com.alexbarcelo.movies.popularMovies;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.alexbarcelo.movies.R;
import com.sqisland.espresso.idling_resource.elapsed_time.ElapsedTimeIdlingResource;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.v4.util.Preconditions.checkArgument;
import static org.hamcrest.Matchers.allOf;
import static com.alexbarcelo.movies.di.FakeMovieRepositoryImpl.LATENCY_IN_MS;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class PopularMoviesScreenTest {

    @Rule
    public ActivityTestRule<PopularMoviesActivity> mActivityTestRule =
            new ActivityTestRule(PopularMoviesActivity.class);

    private Matcher<View> withItemText(final String itemText) {
        checkArgument(!TextUtils.isEmpty(itemText), "itemText cannot be null or empty");
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View item) {
                return allOf(
                        isDescendantOfA(isAssignableFrom(RecyclerView.class)),
                        withText(itemText)).matches(item);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is isDescendantOfA RV with text " + itemText);
            }
        };
    }

    private void waitForFirstPageToBeLoaded() {
        //Wait 4 seconds for initial load
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(LATENCY_IN_MS + 1000);
        IdlingRegistry.getInstance().register(idlingResource);

        //Check that first item has been loaded
        onView(withItemText("POPULAR MOVIE #1 (1999)")).check(matches(isDisplayed()));

        //Clean up
        IdlingRegistry.getInstance().unregister(idlingResource);
    }

    @Test
    public void clickOnSearchMenuItem_opensMovieSearchScreen() {
        // Click on the search icon in the action bar
        onView(withId(R.id.open_movie_search_menu_item)).perform(click());

        // Check if the search view is visible
        onView(withId(R.id.search_view)).check(matches(isDisplayed()));
    }

    @Test
    public void onStartPopularMovies_itemsAreDisplayed() {
        waitForFirstPageToBeLoaded();
    }

    @Test
    public void scrollDownToTheBottomOfTheList_loadsMoreItems() {
        waitForFirstPageToBeLoaded();

        IdlingResource idlingResource = new ElapsedTimeIdlingResource(2000);
        IdlingRegistry.getInstance().register(idlingResource);

        // Scroll down to the last item on the list
        RecyclerView rv = mActivityTestRule.getActivity().findViewById(R.id.movie_list_view);
        onView(withId(R.id.movie_list_view)).perform(RecyclerViewActions.scrollToPosition(
                rv.getAdapter().getItemCount() - 1));

        IdlingRegistry.getInstance().unregister(idlingResource);

        idlingResource = new ElapsedTimeIdlingResource(LATENCY_IN_MS + 1000);
        IdlingRegistry.getInstance().register(idlingResource);

        // Check that first item of the next page has been loaded
        onView(withItemText("POPULAR MOVIE #21 (1999)")).check(matches(isDisplayed()));

        IdlingRegistry.getInstance().unregister(idlingResource);
    }
}
