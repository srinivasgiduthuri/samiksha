package in.amigoscorp.samiksha.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

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
    private List<Review> upcoming;
    private Context context;
    private FragmentManager fragmentManager;
    private InterstitialAd interstitialAd;
    private VideoController mVideoController;
    private static final String TAG = CardAdapter.class.getSimpleName();

    public CardAdapter(List<Review> reviews, List<Review> upcoming, Context context, FragmentManager fragmentManager) {
        this.reviews = reviews;
        this.upcoming = upcoming;
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
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder.getLayoutPosition() == 0) {
            for (Review upcomingReview : upcoming) {
                TextSliderView textSliderView = new TextSliderView(context);
                textSliderView.description(upcomingReview.getName()).image(upcomingReview.getImageUrl())
                        .setScaleType(BaseSliderView.ScaleType.Fit);
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle().putString("extra", upcomingReview.getName());
                holder.sliderLayout.addSlider(textSliderView);
            }
            holder.sliderLayout.setPresetTransformer(SliderLayout.Transformer.Fade);
            holder.sliderLayout.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
            holder.sliderLayout.setCustomAnimation(new DescriptionAnimation());
            holder.sliderLayout.setDuration(4000);
            holder.imageViewFlipperRelativeLayout.setVisibility(View.VISIBLE);
        } else {
            holder.imageViewFlipperRelativeLayout.setVisibility(View.GONE);
        }

        if (holder.getLayoutPosition() % 2 == 0) {
            AdRequest request = new AdRequest.Builder().build();
            holder.nativeExpressAdView.setVideoOptions(new VideoOptions.Builder()
                    .setStartMuted(true)
                    .build());

            mVideoController = holder.nativeExpressAdView.getVideoController();
            mVideoController.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    Log.d(TAG, "Video playback is finished.");
                    super.onVideoEnd();
                }
            });

            holder.nativeExpressAdView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    if (mVideoController.hasVideoContent()) {
                        Log.d(TAG, "Received an ad that contains a video asset.");
                    } else {
                        Log.d(TAG, "Received an ad that does not contain a video asset.");
                    }
                }
            });
            holder.nativeExpressAdView.loadAd(request);
            holder.nativeExpressAdView.setVisibility(View.VISIBLE);
        } else {
            holder.nativeExpressAdView.setVisibility(View.GONE);
        }

        final Review review = reviews.get(position);
        holder.samikshaRatingBar.setRating(review.getRating());
        if (StringUtils.isNotBlank(review.getImageUrl())) {
            Picasso.with(holder.imageView.getContext()).load(review.getImageUrl()).placeholder(R.mipmap.feature_graphic_2).into(holder.imageView);
            holder.title.setVisibility(View.GONE);
        } else {
            Picasso.with(holder.imageView.getContext()).load(R.mipmap.feature_graphic_2).into(holder.imageView);
            holder.title.setTypeface(Typeface.createFromAsset(context.getAssets(), "cinzel_regular.otf"));
            holder.title.setText(review.getName());
            holder.title.setVisibility(View.VISIBLE);
        }
        holder.textViewName.setText(review.getName());
        holder.textViewUrl.setText(review.getLanguage());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ItemDetailActivity.class);
                intent.putExtra("REVIEW", review);
                intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, review.getName());
                context.startActivity(intent);
            }
        });
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ItemDetailActivity.class);
                intent.putExtra("REVIEW", review);
                intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, review.getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (reviews != null && reviews.size() > 0) {
            return reviews.size();
        } else {
            return 0;
        }
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
        public LinearLayout linearLayout;
        public ImageView imageView;
        public TextView textViewName;
        public TextView textViewUrl;
        public TextView title;
        public TextView samikshaRatingTextView;
        public RatingBar samikshaRatingBar;
        public ViewFlipper imageViewFlipper;
        public ImageView ribbonImageView;
        public RelativeLayout imageViewFlipperRelativeLayout;
        public NativeExpressAdView nativeExpressAdView;
        public RelativeLayout adHolderRelativeLayout;
        public SliderLayout sliderLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.review_card_linear_layout);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.reviewCardDetailsLayout);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            textViewUrl = (TextView) itemView.findViewById(R.id.textViewLanguage);
            title = (TextView) itemView.findViewById(R.id.title);
            samikshaRatingTextView = (TextView) itemView.findViewById(R.id.samiksha_rating_label);
            samikshaRatingBar = (RatingBar) itemView.findViewById(R.id.samiksha_rating_bar);
            //imageViewFlipper = (ViewFlipper) itemView.findViewById(R.id.image_view_flipper);
            ribbonImageView = (ImageView) itemView.findViewById(R.id.ribbon_image_view);
            imageViewFlipperRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.image_view_flipper_relative_layout);
            adHolderRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.ad_holder_relative_layout);
            nativeExpressAdView = new NativeExpressAdView(context);
            float density = Resources.getSystem().getDisplayMetrics().density;
            int width = (int) (Resources.getSystem().getDisplayMetrics().widthPixels / density) - 18;
            nativeExpressAdView.setAdSize(new AdSize(width, 80));
            nativeExpressAdView.setAdUnitId(context.getString(R.string.native_ad));
            adHolderRelativeLayout.addView(nativeExpressAdView);
            sliderLayout = (SliderLayout) itemView.findViewById(R.id.image_slider);
        }
    }
}
