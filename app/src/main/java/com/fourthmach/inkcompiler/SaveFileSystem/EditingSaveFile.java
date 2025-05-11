package com.fourthmach.inkcompiler.SaveFileSystem;

import com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.EnhancedDraggableLayout.EnhancedDraggableLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EditingSaveFile {

    public class Note {
        public float x;
        public float y;

        public float width;
        public String rawText = "";
        public JSONObject representingJSONObject;
        public EnhancedDraggableLayout representingView;

        public void changeXY(float newX, float newY) {
            x = newX;
            y = newY;
        }
        public void finalizeChangeXY() {
            try { representingJSONObject.put("x", x); } catch (JSONException e) { e.printStackTrace(); }
            try { representingJSONObject.put("y", y); } catch (JSONException e) { e.printStackTrace(); }
        }
        public void changeRawText(String newRawText) {
            rawText = newRawText;
            try { representingJSONObject.put("raw", rawText); } catch (JSONException e) { e.printStackTrace(); }
        }
        public void finalize(float newWidth) {
            width = newWidth;
        }
    }
    public JSONArray buildingNJA; // building Notes JSONArray
    public ArrayList<Note> NoteArrayList = new ArrayList<>();


    public class NoteSettings {
        public boolean bold;
        public boolean italic;
        public boolean underline;
        public float textSize;
        public JSONObject buildingSJSON; // Settings JSON
    }

    public JSONObject buildingJSON;
    public String FileName;
    public NoteSettings noteSettings = new NoteSettings();


    public EditingSaveFile(ShallowSaveFile fromShallow) { // re building a JSON from the passed ShallowSaveFile
        JSONArray unloadedNotes = fromShallow.unloadedNotes;

        JSONObject buildingingJSON = new JSONObject();
        try { buildingingJSON.put("dc", fromShallow.DateCreated); }
        catch (JSONException e) { e.printStackTrace(); }
        try { buildingingJSON.put("lm", fromShallow.DateModified); }
        catch (JSONException e) { e.printStackTrace(); }

        JSONArray notes = new JSONArray();
        for (int i = 0; i < unloadedNotes.length(); i++) {

            JSONObject loadedNote;  // Get individual point
            try {
                loadedNote = unloadedNotes.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }



            float x = OrZero(loadedNote,"x");  // Extract x value
            float y = OrZero(loadedNote,"y");  // Extract y value
            String rawText;
            try {
                rawText = loadedNote.getString("raw");
            } catch (JSONException e) {
                e.printStackTrace();
                rawText = "";
            }

            Note created = new Note();
            created.x = x;
            created.y = y;
            created.rawText = rawText;


            JSONObject copy = new JSONObject();
            /*
            values are put into it as they are being modified-then-saved
            i.e.
                try{copy.x = ...}
             */
            created.representingJSONObject = copy;
            created.changeXY(x, y);
            created.changeRawText(rawText);


            NoteArrayList.add(created);
            notes.put(copy);
        }


        JSONObject settings = new JSONObject();
        {
            ShallowSaveFile.DefaultSettings defaultSettings = fromShallow.defaultSettings;

            noteSettings.bold = defaultSettings.bold;
            noteSettings.italic = defaultSettings.italic;
            noteSettings.underline = defaultSettings.underline;
            noteSettings.textSize = defaultSettings.textSize;

            try { settings.put("b", defaultSettings.bold); }
            catch (JSONException e) { e.printStackTrace(); }
            try { settings.put("i", defaultSettings.italic); }
            catch (JSONException e) { e.printStackTrace(); }
            try { settings.put("u", defaultSettings.underline); }
            catch (JSONException e) { e.printStackTrace(); }
            try { settings.put("ts", defaultSettings.textSize); }
            catch (JSONException e) { e.printStackTrace(); }
        }


        try { // put in the JSONArray into the main one, or create a new. Either way make sure its the one representing
            buildingingJSON.put("notes", notes);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                JSONArray created = new JSONArray();
                buildingingJSON.put("notes", created);
                notes = created;
            } catch (JSONException e2) { e2.printStackTrace(); }
        }


        try {
            buildingingJSON.put("settings", settings);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                JSONObject created = new JSONObject();
                buildingingJSON.put("settings", created);
                settings = created;
            } catch (JSONException e2) { e2.printStackTrace(); }
        }

        FileName = fromShallow.FileName;
        buildingNJA = notes;
        buildingJSON = buildingingJSON;
        noteSettings.buildingSJSON = settings;
    }

    private float OrZero(JSONObject loadedNote, String key) {
        try {
            return (float) loadedNote.getDouble(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }


    public Note addBox(float initalX, float initialY, String rawText) {

        Note created = new Note();
        created.x = initalX;
        created.y = initialY;
        created.rawText = rawText;


        JSONObject copy = new JSONObject();
        try { copy.put("x", initalX); } catch (JSONException e) { e.printStackTrace(); }
        try { copy.put("y", initialY); } catch (JSONException e) { e.printStackTrace(); }
        try { copy.put("raw", rawText); } catch (JSONException e) { e.printStackTrace(); }
        created.representingJSONObject = copy;


        NoteArrayList.add(created);
        buildingNJA.put(copy);

        return created;
    }

    public void save() {
        SaveFileManager.saveToFile(buildingJSON.toString(), FileName);
    }
}
