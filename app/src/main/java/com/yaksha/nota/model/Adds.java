package com.yaksha.nota.model;

import android.app.Application;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONObject;

import com.yaksha.nota.constants.Constants;

public class Adds extends Application implements Constants, Parcelable {

    private String id;
    private String originImgUrl;
    private String name;
    private String desc;
    private String active;

    public Adds() {

    }

    public Adds(JSONObject jsonData) {

        try {

            this.setId(jsonData.getString("id"));
            this.setOriginImgUrl(jsonData.getString("originImgUrl"));
            this.setDesc(jsonData.getString("desc"));
            this.setActive(jsonData.getString("active"));
            this.setName(jsonData.getString("name"));

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
        dest.writeString(this.originImgUrl);
        dest.writeString(this.desc);
        dest.writeString(this.active);
        dest.writeString(this.name);

    }

    protected Adds(Parcel in) {
        this.id = in.readString();
        this.originImgUrl = in.readString();
        this.desc = in.readString();
        this.active = in.readString();
        this.name = in.readString();

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

    public String getOriginImgUrl() {
        return originImgUrl;
    }

    public void setOriginImgUrl(String originImgUrl) {
        this.originImgUrl = originImgUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }


}
