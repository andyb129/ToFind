package uk.co.barbuzz.tofind.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.barbuzz.tofind.R;
import uk.co.barbuzz.tofind.customviews.circleimageview.CircleImageView;
import uk.co.barbuzz.tofind.fragments.SitesFragment;
import uk.co.barbuzz.tofind.model.Review;
import uk.co.barbuzz.tofind.model.Site;
import uk.co.barbuzz.tofind.utils.Utils;

public class CarouselPagerAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<Site> listItems;
    private OnCarouselItemClickListener carouselItemClickListener;
    private int adapterType;

    @BindView(R.id.review_main_layout)
    LinearLayout reviewMainLayout;
    @BindView(R.id.site_image)
    ImageView siteImage;
    @BindView(R.id.site_image_name)
    TextView siteImageName;
    @BindView(R.id.review_profile_image)
    CircleImageView reviewProfileImage;
    @BindView(R.id.review_profile_name)
    TextView reviewProfileName;
    @BindView(R.id.review_profile_details)
    TextView reviewProfileDetails;
    @BindView(R.id.review_views)
    TextView reviewViews;
    @BindView(R.id.review_comments)
    TextView reviewComments;
    @BindView(R.id.review_likes)
    TextView reviewLikes;

    public interface OnCarouselItemClickListener {
        void onCarouselItemClick(int position, View siteView);//SiteTransitionView siteTransitionView);
    }

    public CarouselPagerAdapter(Context context, ArrayList<Site> listItems, int adapterType,
                                OnCarouselItemClickListener carouselItemClickListener) {
        this.context = context;
        this.listItems = listItems;
        this.adapterType = adapterType;
        this.carouselItemClickListener = carouselItemClickListener;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.element_cover_row, null);
        ButterKnife.bind(this, view);
        switch (adapterType) {
            case SitesFragment.ADAPTER_TYPE_TOP:
                Site site = listItems.get(position);
                Review review = site.getReviews().get(0);
                int imageResourceIdSite = Utils.getResourceId(context,
                        site.getImagelargedrawable(),
                        "drawable", context.getPackageName());
                int imageResourceIdProfile = Utils.getResourceId(context,
                        review.getProfileimagedrawable(),
                        "drawable", context.getPackageName());
                siteImage.setImageResource(imageResourceIdSite);
                siteImage.setTransitionName(context.getString(R.string.site_image_transition_name)+position);
                siteImageName.setText(site.getName());
                reviewProfileImage.setImageResource(imageResourceIdProfile);
                reviewProfileName.setText(review.getName());
                reviewProfileDetails.setText(review.getReview());
                reviewViews.setText(review.getViews());
                reviewComments.setText(review.getComments());
                reviewLikes.setText(review.getLikes());

                reviewMainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //add all views to a list so we can pass in onClick (& view transition names list)
                        //populateTransitionViewsData();
                        //call listener for onclick
                        carouselItemClickListener.onCarouselItemClick(position, v);
                    }
                });
                reviewMainLayout.setBackgroundResource(R.drawable.shadow);

                container.addView(view);
                break;
            case SitesFragment.ADAPTER_TYPE_BOTTOM:
                view = LayoutInflater.from(context).inflate(R.layout.element_parallax_row, null);
                break;
        }

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

}