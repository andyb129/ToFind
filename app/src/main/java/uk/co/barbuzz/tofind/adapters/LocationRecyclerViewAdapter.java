package uk.co.barbuzz.tofind.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.barbuzz.tofind.R;
import uk.co.barbuzz.tofind.model.Site;
import uk.co.barbuzz.tofind.utils.Utils;

/**
 * Created by andy.barber on 19/02/2018.
 */

public class LocationRecyclerViewAdapter
        extends RecyclerView.Adapter<LocationRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Site> siteList = new ArrayList<>();

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    public LocationRecyclerViewAdapter(Context context, List<Site> siteList) {
        this.context = context;
        this.siteList = siteList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.element_site_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Site site = siteList.get(position);
        holder.image.setImageResource(Utils.getResourceId(context, site.getImagelargedrawable(),
                "drawable", context.getPackageName()));
        holder.text.setText(site.getName());
        holder.reviewCount.setText(String.valueOf(site.getReviews().size()));
    }

    @Override
    public int getItemCount() {
        return siteList == null ? 0 : siteList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.element_site_image)
        ImageView image;
        @BindView(R.id.element_site_text)
        TextView text;
        @BindView(R.id.element_site_review_count)
        TextView reviewCount;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
