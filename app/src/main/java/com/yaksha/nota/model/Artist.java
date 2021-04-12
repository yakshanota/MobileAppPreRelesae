package com.yaksha.nota.model;

import android.app.Application;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONObject;

import com.yaksha.nota.constants.Constants;

public class Artist extends Application implements Constants, Parcelable {

    private String id, fullname, username, mela, generate_userId;
    private String lowPhotoUrl = "";

    public Artist() {

    }

    public Artist(JSONObject jsonData) {

        try {

                this.setId(jsonData.getString("id"));
                this.setUsername(jsonData.getString("username"));
                this.setFullname(jsonData.getString("fullname"));
                this.setMela(jsonData.getString("mela"));
                this.setGenerate_userId(jsonData.getString("generate_userId"));
                this.setLowPhotoUrl(jsonData.getString("lowPhotoUrl"));

        } catch (Throwable t) {

            Log.e("Group", "Could not parse malformed JSON: \"" + jsonData.toString() + "\"");

        } finally {

            Log.d("Group", jsonData.toString());
        }
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMela() {
        return mela;
    }

    public void setMela(String mela) {
        this.mela = mela;
    }

    public String getGenerate_userId() {
        return generate_userId;
    }

    public void setGenerate_userId(String generate_userId) {
        this.generate_userId = generate_userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.username);
        dest.writeString(this.fullname);
        dest.writeString(this.mela);
        dest.writeString(this.generate_userId);
        dest.writeString(this.lowPhotoUrl);

    }

    protected Artist(Parcel in) {
        this.id = in.readString();
        this.username = in.readString();
        this.fullname = in.readString();
        this.mela = in.readString();
        this.generate_userId = in.readString();
        this.lowPhotoUrl = in.readString();
    }

    public static final Creator<Artist> CREATOR = new Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel source) {
            return new Artist(source);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };

    public String getLowPhotoUrl() {
        return lowPhotoUrl;
    }

    public void setLowPhotoUrl(String lowPhotoUrl) {
        this.lowPhotoUrl = lowPhotoUrl;
    }
}
