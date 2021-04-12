package com.yaksha.nota.model;

import android.app.Application;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONObject;

import com.yaksha.nota.constants.Constants;

public class MEvent extends Application implements Constants, Parcelable {


    private String id;
    private String user_ID;
    private String user_name;
    private String event_date;
    private String event_mela;
    private String event_name;
    private String event_place;
    private  String event_description ;
    private String event_image;
    private String created_at;

    public MEvent() {

    }

    public MEvent(JSONObject jsonData) {

        try {

            this.setId(jsonData.getString("id"));
            this.setUser_ID(jsonData.getString("user_ID"));
            this.setUser_name(jsonData.getString("user_name"));
            this.setEvent_date(jsonData.getString("event_date"));
            this.setEvent_mela(jsonData.getString("event_mela"));
            this.setEvent_name(jsonData.getString("event_name"));
            this.setEvent_place(jsonData.getString("event_place"));
            this.setEvent_description(jsonData.getString("event_description"));
            this.setEvent_image(jsonData.getString("event_image"));
            this.setCreated_at(jsonData.getString("created_at"));


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
        dest.writeString(this.user_ID);
        dest.writeString(this.user_name);
        dest.writeString(this.event_date);
        dest.writeString(this.event_mela);
        dest.writeString(this.event_description);
        dest.writeString(this.event_name);
        dest.writeString(this.event_image);
        dest.writeString(this.event_place);
        dest.writeString(this.created_at);


    }

    protected MEvent(Parcel in) {
        this.id = in.readString();
        this.user_ID = in.readString();
        this.user_name = in.readString();
        this.event_mela = in.readString();
        this.event_name = in.readString();
        this.event_date = in.readString();
        this.event_description = in.readString();
        this.event_image = in.readString();
        this.event_place = in.readString();
        this.created_at = in.readString();

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


    public String getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(String user_ID) {
        this.user_ID = user_ID;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getEvent_date() {
        return event_date;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    public String getEvent_mela() {
        return event_mela;
    }

    public void setEvent_mela(String event_mela) {
        this.event_mela = event_mela;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_place() {
        return event_place;
    }

    public void setEvent_place(String event_place) {
        this.event_place = event_place;
    }

    public String getEvent_description() {
        return event_description;
    }

    public void setEvent_description(String event_description) {
        this.event_description = event_description;
    }

    public String getEvent_image() {
        return event_image;
    }

    public void setEvent_image(String event_image) {
        this.event_image = event_image;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }



}
