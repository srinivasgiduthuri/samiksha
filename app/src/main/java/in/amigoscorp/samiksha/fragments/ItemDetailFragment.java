package in.amigoscorp.samiksha.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import in.amigoscorp.samiksha.BuildConfig;
import in.amigoscorp.samiksha.R;
import in.amigoscorp.samiksha.adapters.PointsAdapter;
import in.amigoscorp.samiksha.adapters.ReviewersAdapter;
import in.amigoscorp.samiksha.models.Review;
import in.amigoscorp.samiksha.models.Reviewer;

public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Review review;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            Activity activity = this.getActivity();
            review = getArguments().getParcelable("REVIEW");
            if (review != null) {
                List<Reviewer> reviewers = review.getReviewers();
                TextView noTrailerTextView = (TextView) activity.findViewById(R.id.no_trailer_text_view);
                if (noTrailerTextView != null) {
                    if (StringUtils.isNotBlank(review.getTrailerId())) {
                        noTrailerTextView.setVisibility(View.GONE);
                        YouTubePlayerSupportFragment youTubePlayerSupportFragment = (YouTubePlayerSupportFragment) getFragmentManager().findFragmentById(R.id.trailer_youtube_fragment);
                        youTubePlayerSupportFragment.initialize(BuildConfig.YOUTUBE_API_KEY, new YouTubeListener(review.getTrailerId()));
                    } else {
                        noTrailerTextView.setVisibility(View.VISIBLE);
                    }
                }
                ReviewersAdapter reviewersAdapter = new ReviewersAdapter(reviewers, getContext());
                RecyclerView reviewsRecyclerView = (RecyclerView) activity.findViewById(R.id.reviews_recycler_view);
                reviewsRecyclerView.setHasFixedSize(true);
                reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                reviewsRecyclerView.setAdapter(reviewersAdapter);

                if (review.getPositives() != null && review.getPositives().size() > 0) {
                    PointsAdapter positivePointsAdapter = new PointsAdapter("POSITIVE", review.getPositives(), getContext());
                    RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.positive_points_recycler_view);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(positivePointsAdapter);
                } else {
                    TextView positiveHeadingTextView = (TextView) activity.findViewById(R.id.positive_heading_text_view);
                    positiveHeadingTextView.setVisibility(View.GONE);
                }
                if (review.getNegatives() != null && review.getNegatives().size() > 0) {
                    PointsAdapter negativePointsAdapter = new PointsAdapter("NEGATIVE", review.getNegatives(), getContext());
                    RecyclerView negativeRecyclerView = (RecyclerView) activity.findViewById(R.id.negative_points_recycler_view);
                    negativeRecyclerView.setHasFixedSize(true);
                    negativeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    negativeRecyclerView.setAdapter(negativePointsAdapter);
                } else {
                    TextView negativeHeadingTextView = (TextView) activity.findViewById(R.id.negative_heading_text_view);
                    negativeHeadingTextView.setVisibility(View.GONE);
                }

                TextView descriptionHeaderTextView = (TextView) activity.findViewById(R.id.description_heading_text_view);
                TextView descriptionContentTextView = (TextView) activity.findViewById(R.id.description_content_text_view);
                if (StringUtils.isNotBlank(review.getDescription())) {
                    descriptionHeaderTextView.setVisibility(View.VISIBLE);
                    descriptionContentTextView.setVisibility(View.VISIBLE);
                    descriptionContentTextView.setText(review.getDescription());
                } else {
                    descriptionHeaderTextView.setVisibility(View.GONE);
                    descriptionContentTextView.setVisibility(View.GONE);
                }
            } else {
                TextView ratingHeadingTextView = (TextView) activity.findViewById(R.id.rating_heading_text_view);
                ratingHeadingTextView.setVisibility(View.GONE);
            }
        }
    }
}
