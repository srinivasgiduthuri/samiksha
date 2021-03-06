package in.amigoscorp.samiksha.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import in.amigoscorp.samiksha.R;
import in.amigoscorp.samiksha.adapters.CardAdapter;
import in.amigoscorp.samiksha.models.Review;
import in.amigoscorp.samiksha.utils.CommonUtils;
import in.amigoscorp.samiksha.utils.SamikshaContentDownloader;
import io.fabric.sdk.android.Fabric;

import static in.amigoscorp.samiksha.constants.Constants.reviews;
import static in.amigoscorp.samiksha.constants.Constants.upcoming;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.no_network_relative_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        relativeLayout.setVisibility(View.GONE);
        if (CommonUtils.isNetworkAvailable(this)) {
            Fabric.with(this, new Crashlytics());
            Fabric.with(this, new Answers());
            CommonUtils.androidId(getContentResolver());
            ScheduledExecutorService scheduler =
                    Executors.newSingleThreadScheduledExecutor();

            scheduler.scheduleAtFixedRate
                    (new Runnable() {
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new SamikshaContentDownloader().execute();
                                }
                            });
                        }
                    }, 0, 4, TimeUnit.HOURS);

            sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
            viewPager = (ViewPager) findViewById(R.id.container);
            viewPager.setAdapter(sectionsPagerAdapter);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);

            CommonUtils.subscribeTopicViaFirebase();
            CommonUtils.firebaseToken();
            CommonUtils.sendRegistrationToServer(getApplicationContext());
            Bundle bundle = this.getIntent().getExtras();
            if (bundle != null && StringUtils.isNotBlank(bundle.getString("language"))) {
                String language = bundle.getString("language");
                for (int tabCount = 0; tabCount < sectionsPagerAdapter.getCount(); tabCount++) {
                    if (StringUtils.equalsIgnoreCase(sectionsPagerAdapter.getPageTitle(tabCount), language)) {
                        tabLayout.getTabAt(tabCount).select();
                        break;
                    }
                }
            }
        } else {
            relativeLayout.setVisibility(View.VISIBLE);
        }
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1, getPageTitle(position).toString());
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            /*for (Tab tab : tabs) {
                if (tab.getRank() == position + 1) {
                    return tab.getName();
                }
            }*/
            switch (position) {
                case 0:
                    return "ALL";
                case 1:
                    return "TELUGU";
                case 2:
                    return "HINDI";
                case 3:
                    return "ENGLISH";
                case 4:
                    return "TAMIL";
            }
            return null;
        }
    }

    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_SECTION_NAME = "section_name";
        private RecyclerView recyclerView;
        private RecyclerView.LayoutManager layoutManager;
        private CardAdapter adapter;
        private SwipeRefreshLayout swipeContainer;
        private ViewFlipper imageViewFlipper;
        private TextView upcomingHeaderTextView;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, String sectionName) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString(ARG_SECTION_NAME, sectionName);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);

            final TextView noContentTextView = (TextView) rootView.findViewById(R.id.no_content_text_view);
            noContentTextView.setVisibility(View.GONE);

            layoutManager = new LinearLayoutManager(rootView.getContext());

            recyclerView.setLayoutManager(layoutManager);
            getData(rootView.getContext(), getArguments().getString(ARG_SECTION_NAME), noContentTextView);
            swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
            // Setup refresh listener which triggers new data loading
            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    // Your code to refresh the list here.
                    // Make sure you call swipeContainer.setRefreshing(false)
                    // once the network request has completed successfully.
                    fetchReviewsAsync(rootView.getContext(), getArguments().getString(ARG_SECTION_NAME), noContentTextView);
                }
            });
            // Configure the refreshing colors
            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);


            return rootView;
        }

        private void fetchReviewsAsync(Context context, String sectionName, TextView noContentTextView) {
            // Send the network request to fetch the updated data
            // `client` here is an instance of Android Async HTTP
            // getHomeTimeline is an example endpoint.

            // Remember to CLEAR OUT old items before appending in the new ones
            new SamikshaContentDownloader().execute();
            getData(context, sectionName, noContentTextView);
            swipeContainer.setRefreshing(false);
        }


        private void getData(final Context context, final String sectionName, final TextView noContentTextView) {
            class ReviewsFetcher extends AsyncTask<Void, Void, Map<String, List<Review>>> {
                private ProgressDialog progressDialog;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressDialog = ProgressDialog.show(context, "Loading reviews", "Please wait...", false, false);
                }

                @Override
                protected void onPostExecute(Map<String, List<Review>> mapOfReviews) {
                    super.onPostExecute(mapOfReviews);
                    progressDialog.dismiss();
                    loadReviews(mapOfReviews, context, getFragmentManager(), noContentTextView);
                }

                @Override
                protected Map<String, List<Review>> doInBackground(Void... params) {
                    try {
                        Map<String, List<Review>> reviewsMap = new HashMap<>();
                        reviewsMap.put("REVIEWS", reviews);
                        reviewsMap.put("UPCOMING", upcoming);
                        Collections.sort(reviews, new Comparator<Review>() {
                            @Override
                            public int compare(Review review1, Review review2) {
                                return review1.getRank() - review2.getRank();
                            }
                        });

                        switch (sectionName) {
                            case "ALL":
                                return reviewsMap;
                            case "TELUGU":
                                List<Review> teluguReviews = new ArrayList<>();
                                for (Review review : reviews) {
                                    if (StringUtils.equalsIgnoreCase(review.getLanguage(), "TELUGU")) {
                                        teluguReviews.add(review);
                                    }
                                }
                                List<Review> teluguUpcoming = new ArrayList<>();
                                for (Review review : upcoming) {
                                    if (StringUtils.equalsIgnoreCase(review.getLanguage(), "TELUGU")) {
                                        teluguUpcoming.add(review);
                                    }
                                }
                                Map<String, List<Review>> teluguReviewsMap = new HashMap<>();
                                teluguReviewsMap.put("REVIEWS", teluguReviews);
                                teluguReviewsMap.put("UPCOMING", teluguUpcoming);
                                return teluguReviewsMap;
                            case "HINDI":
                                List<Review> hindiReviews = new ArrayList<>();
                                for (Review review : reviews) {
                                    if (StringUtils.equalsIgnoreCase(review.getLanguage(), "HINDI")) {
                                        hindiReviews.add(review);
                                    }
                                }
                                List<Review> hindiUpcoming = new ArrayList<>();
                                for (Review review : upcoming) {
                                    if (StringUtils.equalsIgnoreCase(review.getLanguage(), "HINDI")) {
                                        hindiUpcoming.add(review);
                                    }
                                }
                                Map<String, List<Review>> hindiReviewsMap = new HashMap<>();
                                hindiReviewsMap.put("REVIEWS", hindiReviews);
                                hindiReviewsMap.put("UPCOMING", hindiUpcoming);
                                return hindiReviewsMap;
                            case "ENGLISH":
                                List<Review> englishReviews = new ArrayList<>();
                                for (Review review : reviews) {
                                    if (StringUtils.equalsIgnoreCase(review.getLanguage(), "ENGLISH")) {
                                        englishReviews.add(review);
                                    }
                                }
                                List<Review> englishUpcoming = new ArrayList<>();
                                for (Review review : upcoming) {
                                    if (StringUtils.equalsIgnoreCase(review.getLanguage(), "ENGLISH")) {
                                        englishUpcoming.add(review);
                                    }
                                }
                                Map<String, List<Review>> englishReviewsMap = new HashMap<>();
                                englishReviewsMap.put("REVIEWS", englishReviews);
                                englishReviewsMap.put("UPCOMING", englishUpcoming);
                                return englishReviewsMap;
                            case "TAMIL":
                                List<Review> tamilReviews = new ArrayList<>();
                                for (Review review : reviews) {
                                    if (StringUtils.equalsIgnoreCase(review.getLanguage(), "TAMIL")) {
                                        tamilReviews.add(review);
                                    }
                                }
                                List<Review> tamilUpcoming = new ArrayList<>();
                                for (Review review : upcoming) {
                                    if (StringUtils.equalsIgnoreCase(review.getLanguage(), "TAMIL")) {
                                        tamilUpcoming.add(review);
                                    }
                                }
                                Map<String, List<Review>> tamilReviewsMap = new HashMap<>();
                                tamilReviewsMap.put("REVIEWS", tamilReviews);
                                tamilReviewsMap.put("UPCOMING", tamilUpcoming);
                                return tamilReviewsMap;
                        }
                        return reviewsMap;
                    } catch (Exception e) {
                        return null;
                    }
                }
            }
            ReviewsFetcher fetcher = new ReviewsFetcher();
            fetcher.execute();
        }

        public void loadReviews(Map<String, List<Review>> reviewsMap, Context context, FragmentManager fragmentManager, TextView noContentTextView) {
            if (reviewsMap == null || reviewsMap.size() <= 0) {
                noContentTextView.setVisibility(View.VISIBLE);
                return;
            }
            List<Review> reviews = reviewsMap.get("REVIEWS");
            List<Review> upcoming = reviewsMap.get("UPCOMING");
            adapter = new CardAdapter(reviews, upcoming, context, fragmentManager);
            recyclerView.setAdapter(adapter);
            recyclerView.setVerticalScrollbarPosition(0);
        }
    }
}
