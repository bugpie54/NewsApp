package com.example.android.newsapp;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class NewsActivity extends AppCompatActivity implements LoaderCallbacks<List<News>> {

    /** URL for news data from the Guardian dataset */
    private static final String GUARDIAN_REQUEST_URL =
            "http://content.guardianapis.com/search?";

    // Constant for the API Key
    private static final String API_KEY = "api-key";
    // Constant value for the API Key
    private static final String KEY = "test";

    // Constant value for ordering by date
    private static final String ORDER = "order-by";
    private static final String DATE = "newest";

    // Constant value for adding article author
    private static final String TAGS = "show-tags";
    private static final String AUTHOR = "contributor";

    // Constant value for No's of articles
    private static final String PAGE = "page-size";
    private static final String PAGES = "12";

    // Constant value for section
    private static final String SECTION = "section";

    /**
     * Constant value for the news loader ID.
     */
    private static final int NEWS_LOADER_ID = 1;

    /** Adapter for the list of articles */
    private NewsAdapter mAdapter;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);

        // Find a reference to the {@link ListView} in the layout
        ListView newsListView = findViewById(R.id.list);

        mEmptyTextView = findViewById(R.id.empty_view);
        newsListView.setEmptyView(mEmptyTextView);

        // Create a new adapter that takes an empty list of articles as input
        mAdapter = new NewsAdapter(this, new ArrayList<News>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsListView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with the full article.
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current article that was clicked on
                News currentArticle = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                assert currentArticle != null;
                Uri articleUri = Uri.parse(currentArticle.getUrl());

                // Create a new intent to view the article URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        assert cm != null;
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String searchTag = sharedPreferences.getString(
                getString(R.string.choose_tag),
                getString(R.string.all));

        //extract news category name from list menu
        String[] categorySep = searchTag.split(" ");
        String categoryCap = categorySep[0];
        String category = categoryCap.toLowerCase();


        // Create an URI and an URI Builder
        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        //for all news tag is not apply
        if (!category.equals("all")) {
            uriBuilder.appendQueryParameter(SECTION, category);
        }

        uriBuilder.appendQueryParameter(ORDER, DATE);
        uriBuilder.appendQueryParameter(TAGS, AUTHOR);
        uriBuilder.appendQueryParameter(PAGE, PAGES);
        uriBuilder.appendQueryParameter(API_KEY, KEY);

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> articles) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No articles found."
        mEmptyTextView.setText(R.string.no_articles);

        // Clear the adapter of previous news data
        mAdapter.clear();

        // If there is a valid list of {@link News}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (articles != null && !articles.isEmpty()) {
            mAdapter.addAll(articles);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    @Override
    // This method initialize the contents of the Activity's options menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    // This method is called whenever an item in the options menu is selected.
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
