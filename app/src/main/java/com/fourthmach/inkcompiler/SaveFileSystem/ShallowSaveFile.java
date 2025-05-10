package com.fourthmach.inkcompiler.SaveFileSystem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class ShallowSaveFile {
    public String DateCreated = "NOT SET";
    public String DateModified = "NOT SET";
    public String FileName;// = "NOT SET"; // this is actually very important; imma let this error while running just in case its not set

    public JSONArray unloadedNotes = new JSONArray();

    DefaultSettings defaultSettings = new DefaultSettings();

    public class DefaultSettings {
        public boolean bold = false;
        public boolean italic = false;
        public boolean underline = false;
        public float textSize = 16f;
    }
}