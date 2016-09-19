package com.example.sunillakkad.travelmate.fragments;


import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sunillakkad.travelmate.R;
import com.example.sunillakkad.travelmate.application.TravelmateApp;
import com.example.sunillakkad.travelmate.model.LocationInfo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener {

    private final static String TAG = HomeFragment.class.getSimpleName();
    private final static long INTERVAL_TIME = 36000;
    private final static long FASTEST_INTERVAL_TIME = 18000;

    private OnFindTaxiFareClickedCallbacks mCallback;
    private PlaceAutocompleteFragment mOriginFragment;
    private FragmentManager mFragmentManager;
    private GoogleApiClient mGoogleApiClient;
    private Activity mActivity;
    private Unbinder unbinder;
    private double mOriginLatitude, mOriginLongitude, mDestinationLatitude, mDestinationLongitude;

    @BindString(R.string.origin)
    String mOriginString;
    @BindString(R.string.destination)
    String mDestinationString;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    public HomeFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
        mCallback = (OnFindTaxiFareClickedCallbacks) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        mFragmentManager = getActivity().getFragmentManager();
        setupPlaceAutoCompleteFragments();

        mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .build();

        return rootView;
    }

    @Override
    public void onStart() {
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        requestCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocationRequestCompleted(location);
    }

    private PlaceSelectionListener originSelectionListener = new PlaceSelectionListener() {
        @Override
        public void onPlaceSelected(Place place) {
            mOriginLatitude = place.getLatLng().latitude;
            mOriginLongitude = place.getLatLng().longitude;
            //getCurrentPlaces();
        }

        @Override
        public void onError(Status status) {
            Log.d(TAG, status.getStatusMessage());
        }
    };

    private PlaceSelectionListener destinationSelectionListener = new PlaceSelectionListener() {
        @Override
        public void onPlaceSelected(Place place) {
            mDestinationLatitude = place.getLatLng().latitude;
            mDestinationLongitude = place.getLatLng().longitude;
        }

        @Override
        public void onError(Status status) {
            Log.d(TAG, status.getStatusMessage());
        }
    };

    @OnClick(R.id.button_findTaxi)
    void fabClicked() {
        LocationInfo locationInfo = new LocationInfo(mOriginLatitude, mOriginLongitude, mDestinationLatitude, mDestinationLongitude);
        mCallback.onFindTaxiFareClicked(locationInfo);
    }

    private void setupPlaceAutoCompleteFragments() {
        mOriginFragment = (PlaceAutocompleteFragment) mFragmentManager.findFragmentById(R.id.origin_autocomplete_fragment);
        PlaceAutocompleteFragment mDestinationFragment =
                (PlaceAutocompleteFragment) mFragmentManager.findFragmentById(R.id.dest_autocomplete_fragment);

        mOriginFragment.setHint(mOriginString);
        mDestinationFragment.setHint(mDestinationString);

        mOriginFragment.setOnPlaceSelectedListener(originSelectionListener);
        mDestinationFragment.setOnPlaceSelectedListener(destinationSelectionListener);
    }

    private LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL_TIME);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL_TIME);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    private void currentLocationRequestCompleted(Location location) {
        Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses.size() > 0) {
                System.out.println(addresses.get(0).getLocality());
                System.out.println(addresses.get(0).getAddressLine(0));
                System.out.println(addresses.get(0).toString());

                Address address = addresses.get(0);

                TravelmateApp.getInstance().setCurrentAddress(address);

                mOriginLatitude = address.getLatitude();
                mOriginLongitude = address.getLongitude();

                mOriginFragment.setText(String.format("%s, %s", address.getAddressLine(0), address.getLocality()));
                //getCurrentPlaces();
            }
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    private void requestCurrentLocation() {
        LocationRequest locationRequest = createLocationRequest();
        LocationServices.FusedLocationApi
                .requestLocationUpdates(
                        mGoogleApiClient,
                        locationRequest,
                        this);

        if (ActivityCompat.checkSelfPermission(
                getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                        getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null)
            currentLocationRequestCompleted(location);
    }

    /*private void getCurrentPlaces() {
        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                .getCurrentPlace(mGoogleApiClient, null);
        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(@NonNull PlaceLikelihoodBuffer likelyPlaces) {
                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                    Log.i(TAG, String.format("Place '%s' has likelihood: %g",
                            placeLikelihood.getPlace().getName(),
                            placeLikelihood.getLikelihood()));
                }
                likelyPlaces.release();
            }
        });
    }*/

    public interface OnFindTaxiFareClickedCallbacks {
        void onFindTaxiFareClicked(LocationInfo locationInfo);
    }
}
