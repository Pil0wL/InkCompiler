package com.fourthmach.inkcompiler.SaveFileSystem;

import static com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.helperFunctions.helperFunctions.getDateRightNow;

import com.fourthmach.inkcompiler.editingmenuclasses.EFIActivityInfo;
import com.fourthmach.inkcompiler.editingmenuclasses.enhanceddraggable.EnhancedDraggableLayout.EnhancedDraggableLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EditingSaveFile {


    private static class defaultNoteSettings {
        private static final int width = 250;
    }
    public class Note {

        public float x; // set during loading
        public float y;

        public int width;
        public String rawText = "";
        public JSONObject representingJSONObject;

        /*
        change Functions are used when creating the notes
        so its basically both the actual use and saver functions
         */
        public void changeXY(float newX, float newY) {
            x = newX;
            y = newY;
            try { representingJSONObject.put("x", newX); } catch (JSONException e) { e.printStackTrace(); }
            try { representingJSONObject.put("y", newY); } catch (JSONException e) { e.printStackTrace(); }
        }
        public void changeRawText(String newRawText) {
            rawText = newRawText;
            try { representingJSONObject.put("raw", rawText); } catch (JSONException e) { e.printStackTrace(); }
        }
        public void changeWidth(int newWidth) {
            width = newWidth;
            try { representingJSONObject.put("w", newWidth); } catch (JSONException e) { e.printStackTrace(); }
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

        public void setBold(boolean decision) {
            bold = decision;

            try { buildingSJSON.put("b", decision); }
            catch (JSONException e) { e.printStackTrace(); }
        }
        public void setItalic(boolean decision) {
            italic = decision;

            try { buildingSJSON.put("i", decision); }
            catch (JSONException e) { e.printStackTrace(); }
        }
        public void setUnderline(boolean decision) {
            underline = decision;

            try { buildingSJSON.put("u", decision); }
            catch (JSONException e) { e.printStackTrace(); }
        }
        public void setTextSize(float newTS) {
            textSize = newTS;

            try { buildingSJSON.put("ts", newTS); }
            catch (JSONException e) { e.printStackTrace(); }
        }
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
            try { rawText = loadedNote.getString("raw"); }
            catch (JSONException e) { e.printStackTrace(); rawText = ""; }

            int width;
            try { width = loadedNote.getInt("w"); }
            catch (JSONException e) { e.printStackTrace(); width = defaultNoteSettings.width; }

            Note created = new Note();
            JSONObject copy = new JSONObject();
            // values are put into it as they are being modified-then-saved by the change functions

            created.representingJSONObject = copy;
            created.changeXY(x, y);
            created.changeRawText(rawText);
            created.changeWidth(width);


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
                settings = created;
                buildingingJSON.put("settings", created);
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



    public void removeBox(Note target) {
        { // remove it from the main json array list
            JSONObject targetJSONObject = target.representingJSONObject;
            for (int i = 0; i < buildingNJA.length(); i++) {
                JSONObject candidate = buildingNJA.optJSONObject(i);
                if ( (candidate != null) && (candidate == targetJSONObject) ) {
                    buildingNJA.remove(i);
                    break;
                }
            }
        }

        // remove it from the list a boxes
        NoteArrayList.remove(target);
    }
    public Note addBox(float initalX, float initialY, String rawText) {

        Note created = new Note();
        created.x = initalX;
        created.y = initialY;
        created.rawText = rawText;
        created.width = defaultNoteSettings.width;


        JSONObject copy = new JSONObject();
        try { copy.put("x", initalX); } catch (JSONException e) { e.printStackTrace(); }
        try { copy.put("y", initialY); } catch (JSONException e) { e.printStackTrace(); }
        try { copy.put("raw", rawText); } catch (JSONException e) { e.printStackTrace(); }
        try { copy.put("w", defaultNoteSettings.width); } catch (JSONException e) { e.printStackTrace(); }
        created.representingJSONObject = copy;


        NoteArrayList.add(created);
        buildingNJA.put(copy);

        return created;
    }

    public void save() {
        try { buildingJSON.put("lm", getDateRightNow()); }
        catch (JSONException e) { e.printStackTrace(); }


        SaveFileManager.saveToFile(buildingJSON.toString(), FileName);
    }
}
