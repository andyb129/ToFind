package uk.co.barbuzz.tofind.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import uk.co.barbuzz.tofind.R;
import uk.co.barbuzz.tofind.adapters.ReviewsRecyclerViewAdapter;
import uk.co.barbuzz.tofind.model.Site;
import uk.co.barbuzz.tofind.utils.Utils;

public class ReviewsFragment extends Fragment {

    private static final String TAG = "ReviewsFragment";
    private static final String KEY_SITE_POSITION = TAG + "_key_position";
    private static final String KEY_SITE = TAG + "_key_site";

    private int siteListPosition;
    private Site site;
    private Unbinder unbinder;
    private ReviewFragmentDisplayListener reviewFragmentDisplayListener;

    @BindView(R.id.reviews_site_image)
    ImageView siteImage;
    @BindView(R.id.reviews_site_image_name)
    TextView siteImageName;
    @BindView(R.id.reviews_recycler_view)
    RecyclerView reviewsRecyclerView;

    public interface ReviewFragmentDisplayListener {
        void onReviewFragmentDisplayStatusChanged(boolean isVisible);
    }

    public ReviewsFragment() {
    }

    public static ReviewsFragment newInstance(int position, Site site) {
        ReviewsFragment reviewsFragment = new ReviewsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_SITE_POSITION, position);
        bundle.putParcelable(KEY_SITE, site);
        reviewsFragment.setArguments(bundle);
        return reviewsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reviews, container, false);
        initViews(rootView);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof ReviewFragmentDisplayListener) {
            reviewFragmentDisplayListener = (ReviewFragmentDisplayListener) context;
            reviewFragmentDisplayListener.onReviewFragmentDisplayStatusChanged(true);
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ReviewFragmentDisplayListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (reviewFragmentDisplayListener != null) {
            reviewFragmentDisplayListener.onReviewFragmentDisplayStatusChanged(false);
        }
    }

    private void initViews(View rootView) {
        unbinder = ButterKnife.bind(this, rootView);

        postponeEnterTransition();

        //get data
        if (getArguments() != null) {
            siteListPosition = getArguments().getInt(KEY_SITE_POSITION);
            site = getArguments().getParcelable(KEY_SITE);
        }

        //setup recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(reviewsRecyclerView.getContext(),
                linearLayoutManager.getOrientation());
        reviewsRecyclerView.addItemDecoration(dividerItemDecoration);
        reviewsRecyclerView.setLayoutManager(linearLayoutManager);
        reviewsRecyclerView.setAdapter(new ReviewsRecyclerViewAdapter(getActivity(), site.getReviews()));

        //load name & image
        int imageResourceIdSite = Utils.getResourceId(getActivity(),
                site.getImagelargedrawable(),
                "drawable", getActivity().getPackageName());
        siteImage.setImageResource(imageResourceIdSite);
        siteImageName.setText(site.getName());

        prepareTransitions();

        startPostponedEnterTransition();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * Prepares the shared element transition
     */
    private void prepareTransitions() {
        Transition transition =
                TransitionInflater.from(getContext())
                        .inflateTransition(R.transition.image_shared_element_transition);
        setExitTransition(transition);

        // A similar mapping is set at the ImagePagerFragment with a setEnterSharedElementCallback.
        setEnterSharedElementCallback(
                new SharedElementCallback() {
                    @Override
                    public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {

                        // Map the first shared element name to the ImageView.
                        sharedElements.put(names.get(0), siteImage);
                    }
                });
    }
}
