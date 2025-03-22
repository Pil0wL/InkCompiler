package com.fourthmach.inkcompiler;

import android.os.Parcel;
import android.os.Parcelable;

public class SaveFile implements Parcelable {
    private String title;
    private String description;

    // Constructor
    public SaveFile(String title, String description) {
        this.title = title;
        this.description = description;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }




    protected SaveFile(Parcel in) {
        title = in.readString();
        description = in.readString();
    }
    public static final Creator<SaveFile> CREATOR = new Creator<SaveFile>() {
        @Override
        public SaveFile createFromParcel(Parcel in) {
            return new SaveFile(in);
        }

        @Override
        public SaveFile[] newArray(int size) {
            return new SaveFile[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
    }

}
