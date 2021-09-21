package com.business.card.scanner.maker.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;

import java.util.Objects;

public class Business_Card extends BaseObservable implements Parcelable {
    public static final Creator<Business_Card> CREATOR = new Creator<Business_Card>() {
        public Business_Card createFromParcel(Parcel parcel) {
            return new Business_Card(parcel);
        }

        public Business_Card[] newArray(int i) {
            return new Business_Card[i];
        }
    };
    private String address;
    private String company;
    private Long date;
    private String email;
    private String fav;
    private String group_id;

    /* renamed from: id */
    private String f176id;
    private String image_name;
    private String job_title;
    private String name;
    private String note;
    private String phone;
    private String website;

    public int describeContents() {
        return 0;
    }

    public Business_Card() {
    }

    protected Business_Card(Parcel parcel) {
        this.f176id = parcel.readString();
        this.group_id = parcel.readString();
        this.name = parcel.readString();
        this.job_title = parcel.readString();
        this.company = parcel.readString();
        this.phone = parcel.readString();
        this.email = parcel.readString();
        this.website = parcel.readString();
        this.address = parcel.readString();
        this.image_name = parcel.readString();
        if (parcel.readByte() == 0) {
            this.date = null;
        } else {
            this.date = Long.valueOf(parcel.readLong());
        }
        this.fav = parcel.readString();
        this.note = parcel.readString();
    }

    public String getId() {
        return this.f176id;
    }

    public void setId(String str) {
        this.f176id = str;
    }

    public String getGroup_id() {
        return this.group_id;
    }

    public void setGroup_id(String str) {
        this.group_id = str;
    }

    public String getName() {
        String str = this.name;
        return str == null ? "" : str;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getJob_title() {
        String str = this.job_title;
        return str == null ? "" : str;
    }

    public void setJob_title(String str) {
        this.job_title = str;
    }

    public String getCompany() {
        String str = this.company;
        return str == null ? "" : str;
    }

    public void setCompany(String str) {
        this.company = str;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String str) {
        this.phone = str;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String str) {
        this.email = str;
    }

    public String getWebsite() {
        return this.website;
    }

    public void setWebsite(String str) {
        this.website = str;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String str) {
        this.address = str;
    }

    public String getImage_name() {
        return this.image_name;
    }

    public void setImage_name(String str) {
        this.image_name = str;
    }

    public Long getDate() {
        Long l = this.date;
        return Long.valueOf(l == null ? 0 : l.longValue());
    }

    public void setDate(Long l) {
        this.date = l;
    }

    public String getFav() {
        return this.fav;
    }

    public void setFav(String str) {
        this.fav = str;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String str) {
        this.note = str;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.f176id);
        parcel.writeString(this.group_id);
        parcel.writeString(this.name);
        parcel.writeString(this.job_title);
        parcel.writeString(this.company);
        parcel.writeString(this.phone);
        parcel.writeString(this.email);
        parcel.writeString(this.website);
        parcel.writeString(this.address);
        parcel.writeString(this.image_name);
        if (this.date == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(this.date.longValue());
        }
        parcel.writeString(this.fav);
        parcel.writeString(this.note);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Business_Card)) {
            return false;
        }
        return this.f176id.equals(((Business_Card) obj).f176id);
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.f176id});
    }
}
