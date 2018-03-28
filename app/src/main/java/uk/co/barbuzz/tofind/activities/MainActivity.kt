package uk.co.barbuzz.tofind.activities

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import butterknife.ButterKnife
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import uk.co.barbuzz.tofind.R
import uk.co.barbuzz.tofind.adapters.TabsPagerAdapter
import uk.co.barbuzz.tofind.customviews.noswipeviewpager.NonSwipeableViewPager
import uk.co.barbuzz.tofind.customviews.tablayoutwitharrow.TabLayoutWithArrow
import uk.co.barbuzz.tofind.fragments.MapFragment
import uk.co.barbuzz.tofind.fragments.ReviewsFragment
import uk.co.barbuzz.tofind.fragments.SiteListFragment
import uk.co.barbuzz.tofind.fragments.SitesFragment
import uk.co.barbuzz.tofind.model.Site
import uk.co.barbuzz.tofind.utils.Utils
import java.util.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback, ReviewsFragment.ReviewFragmentDisplayListener {

    private var sitesList: ArrayList<Site>? = null
    private var googleMap: GoogleMap? = null
    private val tabIcons = intArrayOf(R.drawable.ic_tab_reviews, R.drawable.ic_tab_map, R.drawable.ic_tab_person)

    var toolbar: Toolbar? = null
    lateinit var viewPager: NonSwipeableViewPager
    lateinit var tabLayout: TabLayoutWithArrow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        getData()

        initViews()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.dark_gmap_style))
        //uk center - 54.844430, -3.916004 - zoom 5.5f
        googleMap.moveCamera(CameraUpdateFactory
                .newLatLngZoom(LatLng(54.844430, -3.916004), 5.5f))
        googleMap.uiSettings.isMapToolbarEnabled = false

        if (sitesList != null) {
            for (site in sitesList!!) {
                googleMap.addMarker(
                        MarkerOptions()
                                .position(site.position)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                .title(site.name))
            }
        }
    }

    override fun onReviewFragmentDisplayStatusChanged(isVisible: Boolean) {
        toolbar!!.visibility = if (isVisible) View.GONE else View.VISIBLE
    }

    private fun getData() {
        val siteJson = Utils.loadJSONFromAsset(this, JSON_FILENAME)
        sitesList = Utils.parseLocationJsonString(siteJson) as ArrayList<Site>
    }

    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.elevation = 0f

        viewPager = findViewById(R.id.activity_main_view_pager)
        tabLayout = findViewById(R.id.activity_main_tab_layout)

        val adapter = TabsPagerAdapter(supportFragmentManager)
        adapter.addFragment(SitesFragment.newInstance(sitesList))
        adapter.addFragment(MapFragment.newInstance())
        adapter.addFragment(SiteListFragment.newInstance(sitesList))

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

        tabLayout.getTabAt(0)!!.setIcon(tabIcons[0])
        tabLayout.getTabAt(1)!!.setIcon(tabIcons[1])
        tabLayout.getTabAt(2)!!.setIcon(tabIcons[2])

        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                if (position == 1) {
                    //Bristol waterfront
                    googleMap!!.animateCamera(CameraUpdateFactory
                            .newLatLngZoom(LatLng(51.440316, -2.606949), 12f))
                    toolbar!!.setBackgroundColor(resources.getColor(android.R.color.transparent))
                } else if (position == 2) {
                    //uk center - 54.844430, -3.916004 - zoom 5.5f
                    googleMap!!.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(LatLng(54.844430, -3.916004), 5.5f))
                    toolbar!!.setBackgroundColor(resources.getColor(R.color.toolbar_top_colour))
                } else {
                    //uk center - 54.844430, -3.916004 - zoom 5.5f
                    googleMap!!.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(LatLng(54.844430, -3.916004), 5.5f))
                    toolbar!!.setBackgroundColor(resources.getColor(android.R.color.transparent))
                }
            }
        })
    }

    companion object {

        private val TAG = "MainActivity"
        private val JSON_FILENAME = "json/sites.json"
        var currentPosition: Int = 0
    }

}
