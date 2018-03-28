package uk.co.barbuzz.tofind.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Site implements Parcelable {

    private String name;
    private LatLng position;
    private float lat;
    private float lng;
    private String imagesmalldrawable;
    private String imagelargedrawable;
    private List<Review> reviews;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getPosition() {
        return new LatLng(lat, lng);
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public String getImagesmalldrawable() {
        return imagesmalldrawable;
    }

    public void setImagesmalldrawable(String imagesmalldrawable) {
        this.imagesmalldrawable = imagesmalldrawable;
    }

    public String getImagelargedrawable() {
        return imagelargedrawable;
    }

    public void setImagelargedrawable(String imagelargedrawable) {
        this.imagelargedrawable = imagelargedrawable;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeParcelable(this.position, 0);
        dest.writeFloat(this.lat);
        dest.writeFloat(this.lng);
        dest.writeString(this.imagesmalldrawable);
        dest.writeString(this.imagelargedrawable);
        dest.writeTypedList(reviews);
    }

    public Site() {
    }

    protected Site(Parcel in) {
        this.name = in.readString();
        this.position = in.readParcelable(LatLng.class.getClassLoader());
        this.lat = in.readFloat();
        this.lng = in.readFloat();
        this.imagesmalldrawable = in.readString();
        this.imagelargedrawable = in.readString();
        this.reviews = in.createTypedArrayList(Review.CREATOR);
    }

    public static final Creator<Site> CREATOR = new Creator<Site>() {
        public Site createFromParcel(Parcel source) {
            return new Site(source);
        }

        public Site[] newArray(int size) {
            return new Site[size];
        }
    };
}
