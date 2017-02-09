package in.amigoscorp.samiksha.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.android.gms.ads.InterstitialAd;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import in.amigoscorp.samiksha.R;
import in.amigoscorp.samiksha.activities.ItemDetailActivity;
import in.amigoscorp.samiksha.fragments.ItemDetailFragment;
import in.amigoscorp.samiksha.models.Review;

/**
 * Created by sriny on 28/01/17.
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private List<Review> reviews;
    private Context context;
    private FragmentManager fragmentManager;
    private InterstitialAd interstitialAd;

    public CardAdapter(List<Review> reviews, Context context, FragmentManager fragmentManager) {
        this.reviews = reviews;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_card_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(rootView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(position == 0) {
            List<String> urls = new ArrayList<>();
            urls.add("http://in.bmscdn.com/iedb/movies/images/mobile/listing/large/shatamanam_bhavati_et00044995_10-08-2016_03-32-09.jpg");
            urls.add("http://in.bmscdn.com/iedb/movies/images/mobile/listing/large/khaidi-no-150-et00045498-22-08-2016-04-33-37.jpg");
            urls.add("http://in.bmscdn.com/iedb/movies/images/mobile/listing/large/xxx_the_return_of_xander_cage_et00039285_20-07-2016_02-42-03.jpg");
            urls.add("http://in.bmscdn.com/iedb/movies/images/mobile/listing/large/lukkunnodu-et00052593-24-01-2017-05-43-31.jpg");
            holder.imageViewFlipper.setInAnimation(context, R.anim.fade_in);
            holder.imageViewFlipper.setOutAnimation(context, R.anim.fade_out);
            for (String url : urls) {
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setAdjustViewBounds(true);
                Picasso.with(imageView.getContext()).load(url).into(imageView);
                holder.imageViewFlipper.addView(imageView);
            }
        }
        final Review review = reviews.get(position);
        Picasso.with(holder.imageView.getContext()).load(review.getImageUrl()).into(holder.imageView);
        holder.textViewName.setText(review.getName());
        holder.textViewUrl.setText(review.getLanguage());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ItemDetailActivity.class);
                intent.putExtra("REVIEW", review);
                intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, review.getName());
                /*interstitialAd = new InterstitialAd(view.getContext());
                interstitialAd.setAdUnitId(view.getContext().getString(R.string.interstitial_ad_unit_id));
                //AdRequest adRequest = new AdRequest.Builder().build();
                AdRequest adRequest = new AdRequest.Builder().addTestDevice("5536D1B05104BEAE29A3AC347F5E2160").build();
                interstitialAd.loadAd(adRequest);
                interstitialAd.setAdListener(new AdListener() {
                    public void onAdLoaded() {
                        showInterstitial();
                    }
                });*/
                context.startActivity(intent);
            }
        });
    }

    private void showInterstitial() {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        }
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void clear() {
        reviews.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Review> reviews) {
        this.reviews.addAll(reviews);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout relativeLayout;
        public ImageView imageView;
        public TextView textViewName;
        public TextView textViewUrl;
        public ViewFlipper imageViewFlipper;

        public ViewHolder(View itemView) {
            super(itemView);

            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.reviewCardDetailsLayout);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            textViewUrl = (TextView) itemView.findViewById(R.id.textViewLanguage);
            imageViewFlipper = (ViewFlipper) itemView.findViewById(R.id.image_view_flipper);

        }
    }
}
