package com.example.sunillakkad.travelmate.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mm98568 on 9/18/16.
 */
public class LocationInfo implements Parcelable {
    private double originLatitude;
    private double originLongitude;
    private double destinationLatitude;
    private double destinationLongitude;

    public LocationInfo() {
    }

    public LocationInfo(double originLatitude, double originLongitude, double destinationLatitude, double destinationLongitude) {
        this.originLatitude = originLatitude;
        this.originLongitude = originLongitude;
        this.destinationLatitude = destinationLatitude;
        this.destinationLongitude = destinationLongitude;
    }

    protected LocationInfo(Parcel in) {
        originLatitude = in.readDouble();
        originLongitude = in.readDouble();
        destinationLatitude = in.readDouble();
        destinationLongitude = in.readDouble();
    }

    public static final Creator<LocationInfo> CREATOR = new Creator<LocationInfo>() {
        @Override
        public LocationInfo createFromParcel(Parcel in) {
            return new LocationInfo(in);
        }

        @Override
        public LocationInfo[] newArray(int size) {
            return new LocationInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(originLatitude);
        parcel.writeDouble(originLongitude);
        parcel.writeDouble(destinationLatitude);
        parcel.writeDouble(destinationLongitude);
    }

    public double getOriginLatitude() {
        return originLatitude;
    }

    public void setOriginLatitude(double originLatitude) {
        this.originLatitude = originLatitude;
    }

    public double getOriginLongitude() {
        return originLongitude;
    }

    public void setOriginLongitude(double originLongitude) {
        this.originLongitude = originLongitude;
    }

    public double getDestinationLatitude() {
        return destinationLatitude;
    }

    public void setDestinationLatitude(double destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }

    public double getDestinationLongitude() {
        return destinationLongitude;
    }

    public void setDestinationLongitude(double destinationLongitude) {
        this.destinationLongitude = destinationLongitude;
    }
}
