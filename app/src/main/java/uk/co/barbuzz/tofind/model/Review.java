package uk.co.barbuzz.tofind.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable {

    private String name;
    private String review;
    private String views;
    private String comments;
    private String likes;
    private String profileimagedrawable;

    public Review(String name, String review, String views, String comments, String likes, String profileimagedrawable) {
        this.name = name;
        this.review = review;
        this.views = views;
        this.comments = comments;
        this.likes = likes;
        this.profileimagedrawable = profileimagedrawable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getProfileimagedrawable() {
        return profileimagedrawable;
    }

    public void setProfileimagedrawable(String profileimagedrawable) {
        this.profileimagedrawable = profileimagedrawable;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.review);
        dest.writeString(this.views);
        dest.writeString(this.comments);
        dest.writeString(this.likes);
        dest.writeString(this.profileimagedrawable);
    }

    protected Review(Parcel in) {
        this.name = in.readString();
        this.review = in.readString();
        this.views = in.readString();
        this.comments = in.readString();
        this.likes = in.readString();
        this.profileimagedrawable = in.readString();
    }

    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}
