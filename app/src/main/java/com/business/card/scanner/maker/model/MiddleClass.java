package com.business.card.scanner.maker.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;

public class MiddleClass extends BaseObservable implements Parcelable {
    public static final Creator<MiddleClass> CREATOR = new Creator<MiddleClass>() {
        public MiddleClass createFromParcel(Parcel parcel) {
            return new MiddleClass(parcel);
        }

        public MiddleClass[] newArray(int i) {
            return new MiddleClass[i];
        }
    };
    private String group_id;

    /* renamed from: id */
    private String f177id;

    public int describeContents() {
        return 0;
    }

    public MiddleClass() {
    }

    protected MiddleClass(Parcel parcel) {
        this.f177id = parcel.readString();
        this.group_id = parcel.readString();
    }

    public String getId() {
        return this.f177id;
    }

    public void setId(String str) {
        this.f177id = str;
    }

    public String getGroup_id() {
        return this.group_id;
    }

    public void setGroup_id(String str) {
        this.group_id = str;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.f177id);
        parcel.writeString(this.group_id);
    }
}
