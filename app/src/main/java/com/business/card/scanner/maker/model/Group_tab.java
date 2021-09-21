package com.business.card.scanner.maker.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;

public class Group_tab extends BaseObservable implements Parcelable {
    public static final Creator<Group_tab> CREATOR = new Creator<Group_tab>() {
        public Group_tab createFromParcel(Parcel parcel) {
            return new Group_tab(parcel);
        }

        public Group_tab[] newArray(int i) {
            return new Group_tab[i];
        }
    };
    int count = 0;
    Group_tb group_tb;
    Boolean isCheck = false;

    public int describeContents() {
        return 0;
    }

    public Group_tab() {
    }

    protected Group_tab(Parcel parcel) {
        Boolean bool;
        boolean z = false;
        this.group_tb = (Group_tb) parcel.readParcelable(Group_tb.class.getClassLoader());
        byte readByte = parcel.readByte();
        if (readByte == 0) {
            bool = null;
        } else {
            bool = Boolean.valueOf(readByte == 1 ? true : z);
        }
        this.isCheck = bool;
        this.count = parcel.readInt();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.group_tb, i);
        Boolean bool = this.isCheck;
        parcel.writeByte((byte) (bool == null ? 0 : bool.booleanValue() ? 1 : 2));
        parcel.writeInt(this.count);
    }

    public Group_tb getGroup_tb() {
        return this.group_tb;
    }

    public void setGroup_tb(Group_tb group_tb2) {
        this.group_tb = group_tb2;
    }

    public Boolean getCheck() {
        return this.isCheck;
    }

    public void setCheck(Boolean bool) {
        this.isCheck = bool;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int i) {
        this.count = i;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Group_tab)) {
            return false;
        }
        return this.group_tb.getGroup_id().equals(((Group_tab) obj).group_tb.getGroup_id());
    }
}
