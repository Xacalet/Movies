package com.alexbarcelo.movies.searchMovies;

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

import junit.framework.Assert;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.v4.util.Preconditions.checkArgument;
import static com.alexbarcelo.movies.di.FakeMovieRepositoryImpl.LATENCY_IN_MS;
import static com.alexbarcelo.movies.di.FakeMovieRepositoryImpl.QUERY_WITH_NO_NETWORK;
import static com.alexbarcelo.movies.di.FakeMovieRepositoryImpl.QUERY_WITH_NO_RESULTS;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SearchMoviesScreenTest {

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

    @Rule
    public ActivityTestRule<SearchMoviesActivity> mActivityTestRule = new ActivityTestRule(SearchMoviesActivity.class);

    private void enterValidSearchTextAndWait(String searchText) {
        // Click on search view and write the search text
        onView(withId(R.id.search_view)).perform(click());
        onView(withId(R.id.search_src_text)).perform(typeText(searchText), closeSoftKeyboard());

        IdlingResource idlingResource = new ElapsedTimeIdlingResource(LATENCY_IN_MS + 2000);
        IdlingRegistry.getInstance().register(idlingResource);

        if (searchText.equalsIgnoreCase(QUERY_WITH_NO_NETWORK)) {
//            // Check that error dialog has been opened
//            onView(withId(R.id.error_dialog_message)).check(matches(isDisplayed()));
//            // Closes dialog
//            onView(withId(R.id.error_dialog_positive_button)).perform(click());
//            // Check that retry button is displayed
//            onView(withId(R.id.retry_button)).check(matches(isDisplayed()));
        } else if (searchText.equalsIgnoreCase(QUERY_WITH_NO_RESULTS)) {
            // Check that retry button is displayed
            onView(withId(R.id.no_results_text_view)).check(matches(isDisplayed()));
        } else if (searchText.isEmpty()) {
            // Check that retry button is displayed
            RecyclerView rv = mActivityTestRule.getActivity().findViewById(R.id.movie_list_view);
            Assert.assertEquals(rv.getAdapter().getItemCount(), 0);
        } else {
            // Wait for the first result to be loaded
            onView(withItemText("SEARCHED MOVIE #1 (1999)")).check(matches(isDisplayed()));
        }


        IdlingRegistry.getInstance().unregister(idlingResource);
    }

    @Test
    public void typingOnSearchView_startsSearch() {
        String query = "f";
        enterValidSearchTextAndWait(query);
    }

    @Test
    public void scrollDownToTheBottomOfTheList_loadsMoreItems() {
        String query = "f";
        enterValidSearchTextAndWait(query);

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
        onView(withItemText("SEARCHED MOVIE #21 (1999)")).check(matches(isDisplayed()));

        IdlingRegistry.getInstance().unregister(idlingResource);
    }

    @Test
    public void onError_dialogIsShownAndRetryButtonDisplayed() {
        String query = QUERY_WITH_NO_NETWORK;

        onView(withId(R.id.search_view)).perform(click());
        onView(withId(R.id.search_src_text)).perform(typeText(query));

        IdlingResource idlingResource = new ElapsedTimeIdlingResource(LATENCY_IN_MS + 5000);
        IdlingRegistry.getInstance().register(idlingResource);

        // Check that error dialog has been opened
        onView(withId(R.id.error_dialog_message)).check(matches(isDisplayed()));
        // Closes dialog
        onView(withId(R.id.error_dialog_positive_button)).perform(click());
        // Check that retry button is displayed
        onView(withId(R.id.retry_button)).check(matches(isDisplayed()));

        IdlingRegistry.getInstance().unregister(idlingResource);
    }

    @Test
    public void onNoMatchingResults_noResultsMessageIsDisplayed() {
        String query = QUERY_WITH_NO_RESULTS;
        enterValidSearchTextAndWait(query);
    }

    @Test
    public void clearSearchText_searchResultsAreCleared() {
        // Make a regular search
        String query = "f";
        enterValidSearchTextAndWait(query);
        // Check that the list is not empty (except for the footer item)
        RecyclerView rv = mActivityTestRule.getActivity().findViewById(R.id.movie_list_view);
        Assert.assertNotSame(rv.getAdapter().getItemCount(), 1);
        // Clear search view
        onView(withId(R.id.search_src_text)).perform(clearText());
        // Check that the list has been cleared
        Assert.assertEquals(rv.getAdapter().getItemCount(), 1);
    }
}
