package com.example.sunillakkad.travelmate.fragments;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.example.sunillakkad.travelmate.R;
import com.example.sunillakkad.travelmate.model.LocationInfo;
import com.example.sunillakkad.travelmate.model.TaxiFinderResponse;
import com.example.sunillakkad.travelmate.utils.Constants;
import com.example.sunillakkad.travelmate.utils.NetworkCallManager;
import com.example.sunillakkad.travelmate.utils.StringUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaxiRateFragment extends Fragment implements OnMapReadyCallback, DirectionCallback {
    private static final String TAG = TaxiRateFragment.class.getSimpleName();

    private GoogleMap mGoogleMap;
    private Activity mActivity;
    private LocationInfo mLocationInfo;
    private LatLng mCamera, mOrigin, mDestination;
    private Unbinder unbinder;

    @BindView(R.id.tf_rate)
    TextView mFareTextView;
    @BindView(R.id.tf_time)
    TextView mTimeTextView;
    @BindView(R.id.tf_distance)
    TextView mDistanceTextView;
    @BindView(R.id.tf_locationinfo)
    TextView mLocationTextView;
    @BindView(R.id.rloTaxiRate)
    RelativeLayout mRelativeLayout;

    public static TaxiRateFragment newInstance(LocationInfo locationInfo) {
        TaxiRateFragment taxiRateFragment = new TaxiRateFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.LOCATION_INFO, locationInfo);
        taxiRateFragment.setArguments(bundle);
        return taxiRateFragment;
    }

    public TaxiRateFragment() {
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        setRetainInstance(true);
        //mCallback = (OnTaxiServiceFindCallbacks) activity;
        mActivity = (AppCompatActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getBundleData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_taxi_rate, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

        getTaxiFare();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        if (direction.isOK()) {
            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.marker);
            mGoogleMap.addMarker(new MarkerOptions().position(mOrigin).icon(icon));
            mGoogleMap.addMarker(new MarkerOptions().position(mDestination));
            ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
            mGoogleMap.addPolyline(DirectionConverter.createPolyline(getContext(), directionPositionList, 5, Color.parseColor("#303F9F")));
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mCamera, 12));
        }
    }

    @Override
    public void onDirectionFailure(Throwable t) {
        Log.d(TAG, t.getMessage());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
    }

    private void getBundleData() {
        if (getArguments().getParcelable(Constants.LOCATION_INFO) != null)
            mLocationInfo = getArguments().getParcelable(Constants.LOCATION_INFO);

        if (mLocationInfo != null) {
            mOrigin = new LatLng(mLocationInfo.getOriginLatitude(), mLocationInfo.getOriginLongitude());
            mDestination = new LatLng(mLocationInfo.getDestinationLatitude(), mLocationInfo.getDestinationLongitude());
            mCamera = new LatLng(
                    (mLocationInfo.getOriginLatitude() + mLocationInfo.getDestinationLatitude()) / 2,
                    (mLocationInfo.getOriginLongitude() + mLocationInfo.getDestinationLongitude()) / 2);
        }
    }

    private void getTaxiFare() {
        String originLat = String.valueOf(mLocationInfo.getOriginLatitude());
        String originLong = String.valueOf(mLocationInfo.getOriginLongitude());
        String destLat = String.valueOf(mLocationInfo.getDestinationLatitude());
        String destLong = String.valueOf(mLocationInfo.getDestinationLongitude());

        String strOrigin = originLat + "," + originLong;
        String strDestination = destLat + "," + destLong;

        NetworkCallManager.getTaxiFareRate(Constants.TAXI_FARE_API_KEY, strOrigin, strDestination)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccess, this::onError);
    }

    private void onError(Throwable e) {
        Log.e(TAG, e.getMessage());
    }

    private void onSuccess(TaxiFinderResponse b) {
        requestDirection();
        mTimeTextView.setText(StringUtils.getPrettyTime(b.getDuration()));
        mDistanceTextView.setText(StringUtils.getPrettyDistance(b.getDistance()));
        mFareTextView.setText(String.format("$ %s", b.getTotal_fare()));
        String temp = "Per " + b.getRate_area() + " rates includes " + b.getTip_percentage() + "% tip";
        mLocationTextView.setText(temp);
        //locationName = b.getRate_area();
    }

    public void requestDirection() {
        GoogleDirection.withServerKey(Constants.GOOGLE_SERVER_KEY)
                .from(mOrigin)
                .to(mDestination)
                .transportMode(TransportMode.DRIVING)
                .execute(this);
    }
}
