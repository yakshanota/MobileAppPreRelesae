package com.yaksha.nota.model;

import android.app.Application;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONObject;

import com.yaksha.nota.constants.Constants;

public class NewVideo extends Application implements Constants, Parcelable {

    private String id;
    private String fromUserId;
    private String accessMode;
    private String likesCount;
    private String name;
    private String shortname, Description, previewVideoImgUrl, videoUrl;

    public NewVideo() {

    }

    public NewVideo(JSONObject jsonData) {

        try {

            this.setId(jsonData.getString("id"));
            this.setFromUserId(jsonData.getString("fromUserId"));
            this.setAccessMode(jsonData.getString("accessMode"));
            this.setLikesCount(jsonData.getString("likesCount"));
            this.setName(jsonData.getString("name"));
            this.setShortname(jsonData.getString("shortname"));
            this.setDescription(jsonData.getString("Description"));
            this.setPreviewVideoImgUrl(jsonData.getString("previewVideoImgUrl"));
            this.setVideoUrl(jsonData.getString("videoUrl"));

        } catch (Throwable t) {

            Log.e("Group", "Could not parse malformed JSON: \"" + jsonData.toString() + "\"");

        } finally {

            Log.d("Group", jsonData.toString());
        }
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getAccessMode() {
        return accessMode;
    }

    public void setAccessMode(String accessMode) {
        this.accessMode = accessMode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(String likesCount) {
        this.likesCount = likesCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPreviewVideoImgUrl() {
        return previewVideoImgUrl;
    }

    public void setPreviewVideoImgUrl(String previewVideoImgUrl) {
        this.previewVideoImgUrl = previewVideoImgUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.fromUserId);
        dest.writeString(this.accessMode);
        dest.writeString(this.Description);
        dest.writeString(this.likesCount);
        dest.writeString(this.name);
        dest.writeString(this.previewVideoImgUrl);
        dest.writeString(this.shortname);
        dest.writeString(this.videoUrl);

    }

    protected NewVideo(Parcel in) {
        this.id = in.readString();
        this.fromUserId = in.readString();
        this.accessMode = in.readString();
        this.Description = in.readString();
        this.likesCount = in.readString();
        this.name = in.readString();
        this.previewVideoImgUrl = in.readString();
        this.shortname = in.readString();
        this.videoUrl = in.readString();
    }

    public static final Creator<NewVideo> CREATOR = new Creator<NewVideo>() {
        @Override
        public NewVideo createFromParcel(Parcel source) {
            return new NewVideo(source);
        }

        @Override
        public NewVideo[] newArray(int size) {
            return new NewVideo[size];
        }
    };


}
