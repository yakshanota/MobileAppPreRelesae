package com.yaksha.nota.model;

import android.app.Application;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONObject;

import com.yaksha.nota.constants.Constants;

public class MelaList extends Application implements Constants, Parcelable {

    private String id;
    private String fullname;
    private String login;
    private String normalPhotoUrl;


    public MelaList() {

    }

    public MelaList(JSONObject jsonData) {

        try {

            this.setId(jsonData.getString("id"));
            this.setFullname(jsonData.getString("fullname"));
            this.setLogin(jsonData.getString("login"));
            this.setNormalPhotoUrl(jsonData.getString("normalPhotoUrl"));

        } catch (Throwable t) {

            Log.e("Group", "Could not parse malformed JSON: \"" + jsonData.toString() + "\"");

        } finally {

            Log.d("Group", jsonData.toString());
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.fullname);
        dest.writeString(this.login);
        dest.writeString(this.normalPhotoUrl);

    }

    protected MelaList(Parcel in) {
        this.id = in.readString();
        this.fullname = in.readString();
        this.login = in.readString();
        this.normalPhotoUrl = in.readString();


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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNormalPhotoUrl() {
        return normalPhotoUrl;
    }

    public void setNormalPhotoUrl(String normalPhotoUrl) {
        this.normalPhotoUrl = normalPhotoUrl;
    }

}
