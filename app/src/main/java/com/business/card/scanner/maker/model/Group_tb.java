package com.business.card.scanner.maker.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;

public class Group_tb extends BaseObservable implements Parcelable {
    public static final Creator<Group_tb> CREATOR = new Creator<Group_tb>() {
        public Group_tb createFromParcel(Parcel parcel) {
            return new Group_tb(parcel);
        }

        public Group_tb[] newArray(int i) {
            return new Group_tb[i];
        }
    };
    private String defultcard;
    private String group_id;
    private String group_name;

    public int describeContents() {
        return 0;
    }

    public Group_tb() {
    }

    public Group_tb(String str, String str2, String str3) {
        this.group_id = str;
        this.group_name = str2;
        this.defultcard = str3;
    }

    protected Group_tb(Parcel parcel) {
        this.group_id = parcel.readString();
        this.group_name = parcel.readString();
        this.defultcard = parcel.readString();
    }

    public String getDefultcard() {
        return this.defultcard;
    }

    public void setDefultcard(String str) {
        this.defultcard = str;
    }

    public String getGroup_id() {
        return this.group_id;
    }

    public void setGroup_id(String str) {
        this.group_id = str;
    }

    public String getGroup_name() {
        return this.group_name;
    }

    public void setGroup_name(String str) {
        this.group_name = str;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.group_id);
        parcel.writeString(this.group_name);
        parcel.writeString(this.defultcard);
    }
}
