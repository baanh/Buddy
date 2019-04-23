package com.buddy.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class UserContact implements Parcelable {
    private String name;
    private String phone;
    private String email;

    public UserContact(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    protected UserContact(Parcel in) {
        name = in.readString();
        phone = in.readString();
        email = in.readString();
    }

    public static final Creator<UserContact> CREATOR = new Creator<UserContact>() {
        @Override
        public UserContact createFromParcel(Parcel in) {
            return new UserContact(in);
        }

        @Override
        public UserContact[] newArray(int size) {
            return new UserContact[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(email);
    }
}
