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

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import in.amigoscorp.samiksha.R;
import in.amigoscorp.samiksha.adapters.CardAdapter;
import in.amigoscorp.samiksha.models.Review;
import in.amigoscorp.samiksha.utils.AwsUtils;

import static in.amigoscorp.samiksha.constants.Constants.reviews;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate
                (new Runnable() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AwsUtils().execute();
                            }
                        });
                    }
                }, 0, 4, TimeUnit.HOURS);
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
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "ALL";
                case 1:
                    return "TELUGU";
                case 2:
                    return "HINDI";
                case 3:
                    return "ENGLISH";
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

            layoutManager = new LinearLayoutManager(rootView.getContext());

            recyclerView.setLayoutManager(layoutManager);
            getData(rootView.getContext(), getArguments().getString(ARG_SECTION_NAME));

            /*List<String> urls = new ArrayList<>();
            urls.add("http://in.bmscdn.com/iedb/movies/images/mobile/listing/large/shatamanam_bhavati_et00044995_10-08-2016_03-32-09.jpg");
            urls.add("http://in.bmscdn.com/iedb/movies/images/mobile/listing/large/khaidi-no-150-et00045498-22-08-2016-04-33-37.jpg");
            urls.add("http://in.bmscdn.com/iedb/movies/images/mobile/listing/large/xxx_the_return_of_xander_cage_et00039285_20-07-2016_02-42-03.jpg");
            urls.add("http://in.bmscdn.com/iedb/movies/images/mobile/listing/large/lukkunnodu-et00052593-24-01-2017-05-43-31.jpg");
            ViewFlipper imageViewFlipper = (ViewFlipper) rootView.findViewById(R.id.image_view_flipper);
            imageViewFlipper.setInAnimation(rootView.getContext(), R.anim.fade_in);
            imageViewFlipper.setOutAnimation(rootView.getContext(), R.anim.fade_out);
            for (String url : urls) {
                ImageView imageView = new ImageView(rootView.getContext());
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setAdjustViewBounds(true);
                Picasso.with(imageView.getContext()).load(url).into(imageView);
                imageViewFlipper.addView(imageView);
            }*/

            swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
            // Setup refresh listener which triggers new data loading
            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    // Your code to refresh the list here.
                    // Make sure you call swipeContainer.setRefreshing(false)
                    // once the network request has completed successfully.
                    fetchReviewsAsync(rootView.getContext(), getArguments().getString(ARG_SECTION_NAME));
                }
            });
            // Configure the refreshing colors
            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);


            return rootView;
        }

        private void fetchReviewsAsync(Context context, String sectionName) {
            // Send the network request to fetch the updated data
            // `client` here is an instance of Android Async HTTP
            // getHomeTimeline is an example endpoint.

            // Remember to CLEAR OUT old items before appending in the new ones
            new AwsUtils().execute();
            getData(context, sectionName);
            swipeContainer.setRefreshing(false);
        }


        private void getData(final Context context, final String sectionName) {
            class ReviewsFetcher extends AsyncTask<Void, Void, List<Review>> {
                private ProgressDialog progressDialog;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressDialog = ProgressDialog.show(context, "Loading reviews", "Please wait...", false, false);
                }

                @Override
                protected void onPostExecute(List<Review> reviews) {
                    super.onPostExecute(reviews);
                    progressDialog.dismiss();
                    loadReviews(reviews, context, getFragmentManager());
                }

                @Override
                protected List<Review> doInBackground(Void... params) {
                    try {
                        /*ObjectMapper objectMapper = new ObjectMapper();
                        InputStream fileInputStream = context.getResources().openRawResource(R.raw.reviews);
                        List<Review> reviews = objectMapper.readValue(fileInputStream, new TypeReference<List<Review>>() {
                        });*/
                        Collections.sort(reviews, new Comparator<Review>() {
                            @Override
                            public int compare(Review review1, Review review2) {
                                return review1.getRank() - review2.getRank();
                            }
                        });
                        switch (sectionName) {
                            case "ALL":
                                return reviews;
                            case "TELUGU":
                                List<Review> teluguReviews = new ArrayList<>();
                                for (Review review : reviews) {
                                    if (StringUtils.equalsIgnoreCase(review.getLanguage(), "TELUGU")) {
                                        teluguReviews.add(review);
                                    }
                                }
                                return teluguReviews;
                            case "HINDI":
                                List<Review> hindiReviews = new ArrayList<>();
                                for (Review review : reviews) {
                                    if (StringUtils.equalsIgnoreCase(review.getLanguage(), "HINDI")) {
                                        hindiReviews.add(review);
                                    }
                                }
                                return hindiReviews;
                            case "ENGLISH":
                                List<Review> englishReviews = new ArrayList<>();
                                for (Review review : reviews) {
                                    if (StringUtils.equalsIgnoreCase(review.getLanguage(), "ENGLISH")) {
                                        englishReviews.add(review);
                                    }
                                }
                                return englishReviews;
                        }
                        return reviews;
                    } catch (Exception e) {
                        return null;
                    }
                }
            }
            ReviewsFetcher fetcher = new ReviewsFetcher();
            fetcher.execute();
        }

        public void loadReviews(List<Review> reviews, Context context, FragmentManager fragmentManager) {
            adapter = new CardAdapter(reviews, context, fragmentManager);
            recyclerView.setAdapter(adapter);
        }
    }
}
