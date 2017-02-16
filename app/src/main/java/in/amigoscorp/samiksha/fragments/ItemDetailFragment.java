package in.amigoscorp.samiksha.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import in.amigoscorp.samiksha.R;
import in.amigoscorp.samiksha.models.Review;
import in.amigoscorp.samiksha.models.Reviewer;
import in.amigoscorp.samiksha.utils.Config;

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
            List<Reviewer> reviewers = review.getReviewers();
            TextView noTrailerTextView = (TextView) activity.findViewById(R.id.no_trailer_text_view);
            if (StringUtils.isNotBlank(review.getTrailerId())) {
                noTrailerTextView.setVisibility(View.GONE);
                YouTubePlayerSupportFragment youTubePlayerSupportFragment = (YouTubePlayerSupportFragment) getFragmentManager().findFragmentById(R.id.trailer_youtube_fragment);
                youTubePlayerSupportFragment.initialize(Config.YOUTUBE_API_KEY, new YouTubeListener(review.getTrailerId()));
            } else {
                noTrailerTextView.setVisibility(View.VISIBLE);
            }
            ListView listView = (ListView) activity.findViewById(R.id.list_item);
            MySimpleArrayAdapter arrayAdapter = new MySimpleArrayAdapter(getContext(), reviewers);
            listView.setAdapter(arrayAdapter);
        }
    }

    class MySimpleArrayAdapter extends ArrayAdapter<Reviewer> {
        private final Context context;
        private final List<Reviewer> reviewers;

        public MySimpleArrayAdapter(Context context, List<Reviewer> reviewers) {
            super(context, -1, reviewers);
            this.context = context;
            this.reviewers = reviewers;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.review_item, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.label);
            TextView punchLineTextView = (TextView) rowView.findViewById(R.id.punch_line);
            RatingBar ratingBar = (RatingBar) rowView.findViewById(R.id.rating_bar);
            textView.setText(reviewers.get(position).getSource());
            ratingBar.setRating(reviewers.get(position).getActualRating());
            punchLineTextView.setText(reviewers.get(position).getPunchLine());
            return rowView;
        }
    }
}
