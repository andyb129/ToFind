package uk.co.barbuzz.tofind.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.barbuzz.tofind.R;
import uk.co.barbuzz.tofind.adapters.TabsPagerAdapter;
import uk.co.barbuzz.tofind.customviews.noswipeviewpager.NonSwipeableViewPager;
import uk.co.barbuzz.tofind.customviews.tablayoutwitharrow.TabLayoutWithArrow;
import uk.co.barbuzz.tofind.fragments.MapFragment;
import uk.co.barbuzz.tofind.fragments.ReviewsFragment;
import uk.co.barbuzz.tofind.fragments.SiteListFragment;
import uk.co.barbuzz.tofind.fragments.SitesFragment;
import uk.co.barbuzz.tofind.model.Site;
import uk.co.barbuzz.tofind.utils.Utils;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback, ReviewsFragment.ReviewFragmentDisplayListener {

    private static final String TAG = "MainActivity";
    private static final String JSON_FILENAME = "json/sites.json";
    public static int currentPosition;

    private ArrayList<Site> sitesList;
    private GoogleMap googleMap;
    private int[] tabIcons = {
            R.drawable.ic_tab_reviews,
            R.drawable.ic_tab_map,
            R.drawable.ic_tab_person
    };

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.activity_main_view_pager)
    NonSwipeableViewPager viewPager;
    @BindView(R.id.activity_main_tab_layout)
    TabLayoutWithArrow tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getData();

        initViews();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.dark_gmap_style));
        //uk center - 54.844430, -3.916004 - zoom 5.5f
        googleMap.moveCamera(CameraUpdateFactory
                .newLatLngZoom(new LatLng(54.844430, -3.916004), 5.5f));
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        if (sitesList != null) {
            for (Site site : sitesList) {
                googleMap.addMarker(
                        new MarkerOptions()
                                .position(site.getPosition())
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                .title(site.getName()));
            }
        }
    }

    @Override
    public void onReviewFragmentDisplayStatusChanged(boolean isVisible) {
        toolbar.setVisibility(isVisible ? View.GONE : View.VISIBLE);
    }

    private void getData() {
        String siteJson = Utils.loadJSONFromAsset(this, JSON_FILENAME);
        sitesList = (ArrayList<Site>) Utils.parseLocationJsonString(siteJson);
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setElevation(0);

        viewPager = findViewById(R.id.activity_main_view_pager);
        tabLayout = findViewById(R.id.activity_main_tab_layout);

        TabsPagerAdapter adapter = new TabsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(SitesFragment.newInstance(sitesList));
        adapter.addFragment(MapFragment.newInstance());
        adapter.addFragment(SiteListFragment.newInstance(sitesList));

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (position==1) {
                    //Bristol waterfront
                    googleMap.animateCamera(CameraUpdateFactory
                            .newLatLngZoom(new LatLng(51.440316, -2.606949), 12f));
                    toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                } else if (position==2) {
                    //uk center - 54.844430, -3.916004 - zoom 5.5f
                    googleMap.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(new LatLng(54.844430, -3.916004), 5.5f));
                    toolbar.setBackgroundColor(getResources().getColor(R.color.toolbar_top_colour));
                } else {
                    //uk center - 54.844430, -3.916004 - zoom 5.5f
                    googleMap.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(new LatLng(54.844430, -3.916004), 5.5f));
                    toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                }
            }
        });
    }

}
