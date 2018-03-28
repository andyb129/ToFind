package uk.co.barbuzz.tofind.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.transition.ChangeBounds;
import android.transition.ChangeClipBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import uk.co.barbuzz.tofind.R;
import uk.co.barbuzz.tofind.activities.MainActivity;
import uk.co.barbuzz.tofind.adapters.CarouselPagerAdapter;
import uk.co.barbuzz.tofind.customviews.carousel.CarouselEffectTransformer;
import uk.co.barbuzz.tofind.customviews.kenburnsview.KenBurnsView;
import uk.co.barbuzz.tofind.customviews.ticker.TickerView;
import uk.co.barbuzz.tofind.model.Site;
import uk.co.barbuzz.tofind.utils.Utils;

public class SitesFragment extends Fragment
        implements CarouselPagerAdapter.OnCarouselItemClickListener, OnMapReadyCallback {

    private static final String TAG = "SitesFragment";
    private static final String KEY_LOCATION_LIST = TAG + "_key_location";

    public static final int ADAPTER_TYPE_TOP = 1;
    public static final int ADAPTER_TYPE_BOTTOM = 2;
    public static final String EXTRA_IMAGE = "image";
    public static final String EXTRA_TRANSITION_IMAGE = "image";
    private static final long FADE_DEFAULT_TIME = 300;
    private static final long MOVE_DEFAULT_TIME = 500;

    private ViewPager viewPagerTop;
    private TextView viewPagerTotalText;
    private TickerView viewPagerPageNumberTicker;
    private KenBurnsView kenBurnsImageView;
    private int prevPosition;
    private ArrayList<Site> siteList;
    private int[] siteImageList;
    //private ParallaxViewPager viewPagerBackground;
    //private ViewPager.SimpleOnPageChangeListener viewPagerBackgroundOnPageChangeListener;

    public SitesFragment() {
    }

    public static SitesFragment newInstance(ArrayList<Site> sitesList) {
        SitesFragment sitesFragment = new SitesFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_LOCATION_LIST, sitesList);
        sitesFragment.setArguments(bundle);
        return sitesFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sites, container, false);
        getData();
        initViews(rootView);
        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //TODO - implement markers here this when the top map is working - see addTopMapFragment() below
        googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        getActivity(), R.raw.dark_gmap_style));
        //Bristol waterfront
        googleMap.animateCamera(CameraUpdateFactory
                .newLatLngZoom(new LatLng(51.440316, -2.606949), 12f));
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        /*googleMap.addMarker(
                new MarkerOptions()
                        .position(site.getPosition())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        .title(site.getName()));*/
    }

    @Override
    public void onCarouselItemClick(int position, View siteView) {

        //TODO - Shared transition not currently working, so need to spend some more time to get this working

        //another fragment shared transition technique
        Fragment reviewsFragment = ReviewsFragment.newInstance(position, siteList.get(position));
        Fragment locationsFragment = this;

        ((TransitionSet) locationsFragment.getExitTransition()).excludeTarget(siteView, true);

        ImageView transitioningView = siteView.findViewById(R.id.site_image);
        locationsFragment.getFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true) // Optimize for shared element transition
                .addSharedElement(transitioningView, transitioningView.getTransitionName())
                .replace(R.id.activity_main_content_layout, reviewsFragment, ReviewsFragment.class
                        .getSimpleName())
                .addToBackStack(null)
                .commit();

        //Detail transition with Activity
        /*Intent intent=new Intent(getActivity(), FullScreenActivity.class);
        intent.putExtra(EXTRA_IMAGE, siteImageList[position]);

        ImageView siteImageView = siteView.findViewById(R.id.site_image);
        String name = ViewCompat.getTransitionName(siteImageView);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                siteImageView, name);
        ActivityCompat.startActivity(getActivity(), intent, options.toBundle());*/


        //detail transition with Fragments
        //showReviewsFragment(position, siteView);
    }

    private void initViews(View rootView) {
        postponeEnterTransition();

        addTopMapFragment();

        setupViewPagerAndBackground(rootView);

        prepareSharedElementTransition();

        startPostponedEnterTransition();
    }

    private void getData() {
        if (getArguments() != null) {
            siteList = getArguments().getParcelableArrayList(KEY_LOCATION_LIST);
        }

        siteImageList = new int[siteList.size()];
        for (int i = 0; i < siteList.size(); i++) {
            Site site = siteList.get(i);
            siteImageList[i] = Utils.getResourceId(getActivity(),
                    site.getImagelargedrawable(),
                    "drawable", getActivity().getPackageName());
        }
    }

    private void addTopMapFragment() {
        //TODO - experiment with this and get map moving between places

        /*FragmentManager fm = getActivity().getSupportFragmentManager();
        SupportMapFragment fragment = (SupportMapFragment) fm.findFragmentById(R.id.location_map_container);
        if (fragment == null) {
            fragment = new SupportMapFragment();
            fm.beginTransaction().replace(R.id.location_map_container, fragment).commit();
        }
        fragment.getMapAsync(this);*/
    }

    /**
     * Setup viewpager and it's events
     */
    private void setupViewPagerAndBackground(View rootView) {
        //TODO - old background with parallax view pager.  link this up with top view pager possibly to get moving background with swipe
        /*viewPagerBackground = rootView.findViewById(R.id.location_view_pager_back);
        viewPagerBackgroundOnPageChangeListener = viewPagerBackground.getOnPageChangeListener();
        CarouselPagerAdapter adapterBackground = new CarouselPagerAdapter(getActivity(), listItems, ADAPTER_TYPE_BOTTOM);
        viewPagerBackground.setAdapter(adapterBackground);*/

        kenBurnsImageView = rootView.findViewById(R.id.location_ken_burns_image);
        kenBurnsImageView.setResourceIds(siteImageList);

        viewPagerTop = rootView.findViewById(R.id.location_view_pager_top);
        viewPagerTop.setClipChildren(false);
        viewPagerTop.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
        viewPagerTop.setOffscreenPageLimit(3);
        viewPagerTop.setPageTransformer(false, new CarouselEffectTransformer(getActivity())); // Set transformer
        viewPagerTop.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //viewPagerBackgroundOnPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                MainActivity.Companion.setCurrentPosition(position);

                kenBurnsImageView.swapImage(position, prevPosition);
                viewPagerPageNumberTicker.setText(String.valueOf(position+1));
                prevPosition = position;
            }
        });

        viewPagerPageNumberTicker = rootView.findViewById(R.id.view_pager_page_number_ticker);
        viewPagerTotalText = rootView.findViewById(R.id.view_pager_ticker_total_text);

        viewPagerTotalText.setText(String.format(getString(R.string.view_pager_total), siteList.size()));
        viewPagerPageNumberTicker.setText("1");

        CarouselPagerAdapter adapter = new CarouselPagerAdapter(getActivity(), siteList,
                ADAPTER_TYPE_TOP, this);
        viewPagerTop.setAdapter(adapter);

    }

    /**
     * Prepares the shared element transition from and back to the review fragment.
     */
    private void prepareSharedElementTransition() {
        setExitTransition(TransitionInflater.from(getContext())
                .inflateTransition(R.transition.grid_exit_transition));

        setExitSharedElementCallback(
                new SharedElementCallback() {
                    @Override
                    public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                        Fragment currentFragment = (Fragment) viewPagerTop.getAdapter()
                                .instantiateItem(viewPagerTop, MainActivity.Companion.getCurrentPosition());
                        View view = currentFragment.getView();
                        if (view == null) {
                            return;
                        }
                        sharedElements.put(names.get(0), view.findViewById(R.id.site_image));
                    }
                });
    }

    private void showReviewsFragment(int position, View siteView) {
        Fragment locationsFragment = this;
        Fragment reviewsFragment = ReviewsFragment.newInstance(position, siteList.get(position));//listItems[position]);

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

        // 1. Exit for Previous Fragment
        /*Fade exitFade = new Fade();
        exitFade.setDuration(FADE_DEFAULT_TIME);
        locationsFragment.setExitTransition(exitFade);

        // 2. Shared Elements Transition
        TransitionSet enterTransitionSet = new TransitionSet();
        enterTransitionSet.addTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.move));
        enterTransitionSet.setDuration(MOVE_DEFAULT_TIME);
        enterTransitionSet.setStartDelay(FADE_DEFAULT_TIME);
        reviewsFragment.setSharedElementEnterTransition(enterTransitionSet);

        // 3. Enter Transition for New Fragment
        Fade enterFade = new Fade();
        enterFade.setStartDelay(MOVE_DEFAULT_TIME + FADE_DEFAULT_TIME);
        enterFade.setDuration(FADE_DEFAULT_TIME);
        reviewsFragment.setEnterTransition(enterFade);*/

        TransitionSet enterTransition = new TransitionSet();
        enterTransition.addTransition(new ChangeBounds());
        enterTransition.addTransition(new ChangeClipBounds());
        enterTransition.addTransition(new ChangeImageTransform());
        enterTransition.addTransition(new ChangeTransform());

        TransitionSet returnTransition = new TransitionSet();
        returnTransition.addTransition(new ChangeBounds());
        returnTransition.addTransition(new ChangeClipBounds());
        returnTransition.addTransition(new ChangeImageTransform());
        returnTransition.addTransition(new ChangeTransform());

        reviewsFragment.setSharedElementEnterTransition(enterTransition);
        reviewsFragment.setSharedElementReturnTransition(returnTransition);

        ImageView siteImageView = siteView.findViewById(R.id.site_image);
        String name = ViewCompat.getTransitionName(siteImageView);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.addSharedElement(siteImageView, name);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.activity_main_content_layout, reviewsFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

}
