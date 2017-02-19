package in.amigoscorp.samiksha.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import in.amigoscorp.samiksha.R;
import in.amigoscorp.samiksha.models.Reviewer;

/**
 * Created by sriny on 17/02/17.
 */

public class ReviewersAdapter extends RecyclerView.Adapter<ReviewersAdapter.ViewHolder> {

    private List<Reviewer> reviewers;
    private Context context;

    public ReviewersAdapter(List<Reviewer> reviewers, Context context) {
        this.reviewers = reviewers;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(rootView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.sourceTextView.setText(reviewers.get(position).getSource());
        if (StringUtils.isNotBlank(reviewers.get(position).getVerdict())) {
            holder.verdictTextView.setText(reviewers.get(position).getVerdict());
        }
        holder.ratingBar.setRating(reviewers.get(position).getActualRating());
    }

    @Override
    public int getItemCount() {
        if (reviewers != null && reviewers.size() > 0) {
            return reviewers.size();
        } else {
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView sourceTextView;
        public TextView verdictTextView;
        public RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            sourceTextView = (TextView) itemView.findViewById(R.id.label);
            verdictTextView = (TextView) itemView.findViewById(R.id.punch_line);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rating_bar);
        }
    }
}
