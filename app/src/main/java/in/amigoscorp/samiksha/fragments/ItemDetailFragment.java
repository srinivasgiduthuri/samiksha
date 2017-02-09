package in.amigoscorp.samiksha.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import in.amigoscorp.samiksha.R;
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
            final CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                review = getArguments().getParcelable("REVIEW");
                appBarLayout.setTitle(review.getName());

                List<Reviewer> reviewers = review.getReviewers();
                //((TextView) rootView.findViewById(R.id.item_detail)).setText(review.getLanguage());
                ListView listView = (ListView) activity.findViewById(R.id.list_item);
                listView.setNestedScrollingEnabled(true);
                MySimpleArrayAdapter arrayAdapter = new MySimpleArrayAdapter(getContext(), reviewers);
                listView.setAdapter(arrayAdapter);
                final ImageView background = (ImageView) appBarLayout.findViewById(R.id.image_id);
                Picasso.with(appBarLayout.getContext()).load(review.getImageUrl()).into(background, new Callback() {
                    @Override
                    public void onSuccess() {
                        appBarLayout.setBackground(background.getDrawable());
                    }

                    @Override
                    public void onError() {

                    }
                });
                /*ListView listView = (ListView) activity.findViewById(R.id.list_item);
                listView.setNestedScrollingEnabled(true);
                listView.setAdapter(ArrayAdapter.createFromResource(getActivity(),
                        R.array.adobe_products, android.R.layout.simple_list_item_1));*/
                /*List<String> list = new ArrayList<>();
                list.add("Hello");
                list.add("World");
                // Binding resources Array to ListAdapter

                //this.setListAdapter(new ArrayAdapter<String>(this, R.layout.review_item, R.id.label, adobe_products));

                /*final ImageView background = new ImageView(appBarLayout.getContext());
                Picasso.with(appBarLayout.getContext()).load(review.getImageUrl()).into(background, new Callback() {
                    @Override
                    public void onSuccess() {
                        appBarLayout.setBackground(background.getDrawable());
                    }

                    @Override
                    public void onError() {

                    }
                });*/
            }
        }
    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (review != null) {
            System.out.println(review.getLanguage());
            ((TextView) rootView.findViewById(R.id.item_detail)).setText(review.getLanguage());
        }

        return rootView;
    }*/

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
            TextView textView2 = (TextView) rowView.findViewById(R.id.punch_line);
            RatingBar ratingBar = (RatingBar) rowView.findViewById(R.id.rating_bar);
            textView.setText(reviewers.get(position).getSource());
            ratingBar.setRating(reviewers.get(position).getActualRating());
            //textView2.setText(reviewers.get(position).getSource());
            return rowView;
        }
    }
}
