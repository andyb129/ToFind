package uk.co.barbuzz.tofind.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.barbuzz.tofind.R;
import uk.co.barbuzz.tofind.customviews.circleimageview.CircleImageView;
import uk.co.barbuzz.tofind.model.Review;
import uk.co.barbuzz.tofind.utils.Utils;

public class ReviewsRecyclerViewAdapter
        extends RecyclerView.Adapter<ReviewsRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Review> reviewList;

    public ReviewsRecyclerViewAdapter(Context context, List<Review> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.element_review_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.profileImage.setImageResource(Utils.getResourceId(context, review.getProfileimagedrawable(),
                "drawable", context.getPackageName()));
        holder.profileName.setText(review.getName());
        holder.profileDetails.setText(review.getReview());
        holder.reviewViews.setText(review.getViews());
        holder.reviewComments.setText(review.getComments());
        holder.reviewLikes.setText(review.getLikes());
    }

    @Override
    public int getItemCount() {
        return reviewList == null ? 0 : reviewList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.reviews_profile_image)
        CircleImageView profileImage;
        @BindView(R.id.reviews_profile_name)
        TextView profileName;
        @BindView(R.id.reviews_profile_details)
        TextView profileDetails;
        @BindView(R.id.reviews_views)
        TextView reviewViews;
        @BindView(R.id.reviews_comments)
        TextView reviewComments;
        @BindView(R.id.reviews_likes)
        TextView reviewLikes;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
