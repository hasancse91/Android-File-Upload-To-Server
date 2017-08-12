
package com.hellohasan.android_file_upload_tutorial.ModelClass;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ImageSenderInfo implements Parcelable {

    @SerializedName("sender_name")
    private String sender;
    @SerializedName("sender_age")
    private int age;

    public ImageSenderInfo() {
    }

    public ImageSenderInfo(String sender, int age) {
        this.sender = sender;
        this.age = age;
    }

    public final static Parcelable.Creator<ImageSenderInfo> CREATOR = new Creator<ImageSenderInfo>() {

        @SuppressWarnings({
            "unchecked"
        })
        public ImageSenderInfo createFromParcel(Parcel in) {
            ImageSenderInfo instance = new ImageSenderInfo();
            instance.sender = ((String) in.readValue((String.class.getClassLoader())));
            instance.age = ((int) in.readValue((int.class.getClassLoader())));
            return instance;
        }

        public ImageSenderInfo[] newArray(int size) {
            return (new ImageSenderInfo[size]);
        }

    };


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(sender);
        dest.writeValue(age);
    }

    public int describeContents() {
        return  0;
    }

}
