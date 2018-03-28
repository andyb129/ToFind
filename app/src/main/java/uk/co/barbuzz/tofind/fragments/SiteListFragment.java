package uk.co.barbuzz.tofind.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import uk.co.barbuzz.tofind.R;
import uk.co.barbuzz.tofind.adapters.LocationRecyclerViewAdapter;
import uk.co.barbuzz.tofind.model.Site;

public class SiteListFragment extends Fragment {

    private static final String TAG = "SiteListFragment";
    private static final String KEY_LOCATION_LIST = TAG + "_key_location";

    Unbinder unbinder;
    private ArrayList<Site> siteList;
    private LocationRecyclerViewAdapter adapter;

    @BindView(R.id.site_recycler_view)
    RecyclerView recyclerView;

    public SiteListFragment() {
    }

    public static SiteListFragment newInstance(ArrayList<Site> sitesList) {
        SiteListFragment reviewsFragment = new SiteListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_LOCATION_LIST, sitesList);
        reviewsFragment.setArguments(bundle);
        return reviewsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sites_list, container, false);
        initViews(rootView);
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //refresh recyclerview when fragment is displayed to user so animation is played
            if (recyclerView != null && adapter != null) {
                adapter.notifyDataSetChanged();
                recyclerView.scheduleLayoutAnimation();
            }
        }
    }

    private void initViews(View rootView) {
        unbinder = ButterKnife.bind(this, rootView);

        if (getArguments() != null) {
            siteList = getArguments().getParcelableArrayList(KEY_LOCATION_LIST);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation_from_bottom);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        adapter = new LocationRecyclerViewAdapter(getActivity(), siteList);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setLayoutAnimation(controller);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
