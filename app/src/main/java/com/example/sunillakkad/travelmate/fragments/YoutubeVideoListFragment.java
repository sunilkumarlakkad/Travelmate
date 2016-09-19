package com.example.sunillakkad.travelmate.fragments;


import android.content.Context;
import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sunillakkad.travelmate.R;
import com.example.sunillakkad.travelmate.adapters.VideoListAdapter;
import com.example.sunillakkad.travelmate.application.TravelmateApp;
import com.example.sunillakkad.travelmate.model.VideoItem;
import com.example.sunillakkad.travelmate.utils.YoutubeConnector;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class YoutubeVideoListFragment extends Fragment {

    private VideoListAdapter mVideoListAdapter;
    private OnVideoItemSelectCallbacks mCallbacks;
    private Unbinder unbinder;

    @BindView(R.id.video_list_recyclerView)
    RecyclerView mRecyclerView;

    public static YoutubeVideoListFragment newInstance() {
        return new YoutubeVideoListFragment();
    }

    public YoutubeVideoListFragment() {
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        mCallbacks = (OnVideoItemSelectCallbacks) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_youtube_video_list, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        getVideoList();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Videos");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void getVideoList() {
        Address address = TravelmateApp.getInstance().getCurrentAddress();
        if (address != null) {
            String searchTerm = address.getLocality() + " " + address.getAdminArea();
            YoutubeConnector yc = new YoutubeConnector(getContext());
            List<VideoItem> videoItemList = yc.search(searchTerm + " attractions");

            setupRecyclerView(videoItemList);
        }
    }

    private void setupRecyclerView(List<VideoItem> videoItems) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mVideoListAdapter = new VideoListAdapter(getActivity(), videoItems);

        mVideoListAdapter.setOnItemClickListener(videoItem -> mCallbacks.onItemSelected(videoItem.getId()));
    }

    public interface OnVideoItemSelectCallbacks {
        void onItemSelected(String videoID);
    }
}
