package com.fourthmach.inkcompiler;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.HashSet;
import java.util.Set;

public class SaveFile implements Parcelable {
    private String title;
    private String description;
    private float x;
    private float y;

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
        x = in.readFloat();
        y = in.readFloat();
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
        dest.writeFloat(x);
        dest.writeFloat(y);
    }
    // Function to save the note data (including X and Y) using SharedPreferences.
    public void saveNote(android.content.Context context) {
        android.content.SharedPreferences sharedPreferences = context.getSharedPreferences("NotesData", android.content.Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();

        // Ensure the note has a unique ID
        String noteId = "Note_" + title.hashCode(); // Using hashcode as a simple unique ID

        editor.putString(noteId + "noteTitle", title);
        editor.putString(noteId + "noteDescription", description);
        editor.putFloat(noteId + "noteX", x);
        editor.putFloat(noteId + "noteY", y);

        // Store the note ID in a list of saved notes
        Set<String> noteIds = sharedPreferences.getStringSet("SavedNotes", new HashSet<>());
        noteIds.add(noteId);
        editor.putStringSet("SavedNotes", noteIds);


        editor.apply(); // Save data asynchronously
    }
    // Function to load a saved note (static method)
    public static SaveFile loadSavedNote(android.content.Context context, String noteTitle) {
        android.content.SharedPreferences sharedPreferences = context.getSharedPreferences("NotesData", android.content.Context.MODE_PRIVATE);

        String noteId = "Note_" + noteTitle.hashCode(); // Retrieve the same ID format

        String title = sharedPreferences.getString(noteId + "_Title", null);
        if (title == null) return null; // Note not found


        String description = sharedPreferences.getString(noteId+ "noteDescription", "");
        float x = sharedPreferences.getFloat(noteId + "noteX", 0.0f);
        float y = sharedPreferences.getFloat(noteId + "noteY", 0.0f);

        return new SaveFile(title, description, x, y);
    }
}