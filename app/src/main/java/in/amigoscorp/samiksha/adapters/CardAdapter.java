package in.amigoscorp.samiksha.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.android.gms.ads.InterstitialAd;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import in.amigoscorp.samiksha.R;
import in.amigoscorp.samiksha.activities.ItemDetailActivity;
import in.amigoscorp.samiksha.fragments.ItemDetailFragment;
import in.amigoscorp.samiksha.models.Review;

import static in.amigoscorp.samiksha.constants.Constants.upcoming;

/**
 * Created by sriny on 28/01/17.
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private List<Review> reviews;
    private Context context;
    private FragmentManager fragmentManager;
    private InterstitialAd interstitialAd;
    private boolean isViewFlipperAdded = false;

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
        if (position == 0 && !isViewFlipperAdded) {
            holder.imageViewFlipper.setInAnimation(context, R.anim.fade_in);
            holder.imageViewFlipper.setOutAnimation(context, R.anim.fade_out);
            for (Review upcomingReview : upcoming) {
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setAdjustViewBounds(true);
                Picasso.with(imageView.getContext()).load(upcomingReview.getImageUrl()).into(imageView);
                holder.imageViewFlipper.addView(imageView);
            }
            isViewFlipperAdded = true;
        }
        final Review review = reviews.get(position);
        if (StringUtils.isNotBlank(review.getImageUrl())) {
            Picasso.with(holder.imageView.getContext()).load(review.getImageUrl()).into(holder.imageView);
        } else {
            holder.title.setTypeface(Typeface.createFromAsset(context.getAssets(), "pacifico.ttf"));
            holder.title.setText(review.getName());
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
        public LinearLayout linearLayout;
        public ImageView imageView;
        public TextView textViewName;
        public TextView textViewUrl;
        public ViewFlipper imageViewFlipper;
        public TextView title;

        public ViewHolder(View itemView) {
            super(itemView);

            linearLayout = (LinearLayout) itemView.findViewById(R.id.review_card_linear_layout);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.reviewCardDetailsLayout);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            textViewUrl = (TextView) itemView.findViewById(R.id.textViewLanguage);
            imageViewFlipper = (ViewFlipper) itemView.findViewById(R.id.image_view_flipper);
            title = (TextView) itemView.findViewById(R.id.title);

        }
    }
}
