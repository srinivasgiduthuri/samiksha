package in.amigoscorp.samiksha.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import in.amigoscorp.samiksha.R;

/**
 * Created by sriny on 17/02/17.
 */

public class PointsAdapter extends RecyclerView.Adapter<PointsAdapter.ViewHolder> {

    private String type;
    private List<String> points;
    private Context context;

    public PointsAdapter(String type, List<String> points, Context context) {
        this.type = type;
        this.points = points;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.points, parent, false);
        ViewHolder viewHolder = new ViewHolder(rootView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (StringUtils.isNotBlank(type) && StringUtils.equalsIgnoreCase(type, "POSITIVE")) {
            Picasso.with(context).load(R.drawable.ic_thumb_up_black_24dp).into(holder.bulletImageView);
        } else if (StringUtils.isNotBlank(type) && StringUtils.equalsIgnoreCase(type, "NEGATIVE")) {
            Picasso.with(context).load(R.drawable.ic_thumb_down_black_24dp).into(holder.bulletImageView);
        }
        holder.pointTextView.setText(points.get(position));
    }

    @Override
    public int getItemCount() {
        if (points != null && points.size() > 0) {
            return points.size();
        } else {
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView pointTextView;
        public ImageView bulletImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            pointTextView = (TextView) itemView.findViewById(R.id.point_text_view);
            bulletImageView = (ImageView) itemView.findViewById(R.id.bullet_image_view);
        }
    }
}
