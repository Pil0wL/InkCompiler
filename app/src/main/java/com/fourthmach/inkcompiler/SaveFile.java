package com.fourthmach.inkcompiler;

import android.os.Parcel;
import android.os.Parcelable;

public class SaveFile implements Parcelable {
    private String title;
    private String description;
    private float x; // X position of the note
    private float y; // Y position of the note

    // Constructor
    public SaveFile(String title, String description, float x, float y) {
        this.title = title;
        this.description = description;
        this.x = x;
        this.y = y;
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

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }


    protected SaveFile(Parcel in) {
        title = in.readString();
        description = in.readString();
        x = in.readFloat(); // Read X position
        y = in.readFloat(); // Read Y position

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
        dest.writeFloat(x); // Write X position
        dest.writeFloat(y); // Write Y position
    }

    // Function to save the note data (including X and Y) using SharedPreferences.
    public void saveNote(android.content.Context context) {
        android.content.SharedPreferences sharedPreferences = context.getSharedPreferences("NotesData", android.content.Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("noteTitle", title);
        editor.putString("noteDescription", description);
        editor.putFloat("noteX", x);
        editor.putFloat("noteY", y);

        editor.apply(); // Save data asynchronously
    }

    // Function to load a saved note (static method)
    public static SaveFile loadSavedNote(android.content.Context context) {
        android.content.SharedPreferences sharedPreferences = context.getSharedPreferences("NotesData", android.content.Context.MODE_PRIVATE);

        String title = sharedPreferences.getString("noteTitle", "Untitled");
        String description = sharedPreferences.getString("noteDescription", "");
        float x = sharedPreferences.getFloat("noteX", 0.0f);
        float y = sharedPreferences.getFloat("noteY", 0.0f);

        return new SaveFile(title, description, x, y);
    }
}
