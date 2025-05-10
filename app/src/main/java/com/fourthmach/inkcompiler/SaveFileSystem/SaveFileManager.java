package com.fourthmach.inkcompiler.SaveFileSystem;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
public class SaveFileManager {
    public static Context applicationContext;
    public static ShallowSaveFile beholdedShallowSaveFile;


    public static final String folderName = "Save Files";
    public static String saveToFile(String jsonData, String filename) {
        File folder = new File(applicationContext.getFilesDir(), folderName);

        if (!folder.exists()) {
            boolean created = folder.mkdirs(); // creates the folder and any missing parent folders
            /*
            if (created) {
                Log.d("Folder", "Folder created!");
            } else {
                Log.d("Folder", "Failed to create folder.");
            }

             */
        }

        if (filename == null) {
            long timestamp = System.currentTimeMillis();
            filename = Long.toString(timestamp);
        }
        File file = new File(folder, filename);

        try {
            FileOutputStream fos = new FileOutputStream(file, false);
            fos.write(jsonData.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        Log.d("SaveFileManager",
                String.format("Saved To File! | File Name = %s | Raw Data = %s",
                        filename, jsonData
                ));
        return filename;
    }

    public static ShallowSaveFile officiateNewSaveFile() {
        ShallowSaveFile newFile = new ShallowSaveFile();
        ShallowSaveFile.DefaultSettings defaultSettings = newFile.defaultSettings;
        newFile.DateCreated = "Today";
        newFile.DateModified = "Today LOL ( but through the officiate thing )";

        JSONObject mainJSON = new JSONObject();
        try { mainJSON.put("dc", newFile.DateCreated); }
        catch (JSONException e) { e.printStackTrace(); }
        try { mainJSON.put("lm", newFile.DateModified); }
        catch (JSONException e) { e.printStackTrace(); }

        {

            JSONObject dsJSON = new JSONObject();
            try { dsJSON.put("b", defaultSettings.bold); }
            catch (JSONException e) { e.printStackTrace(); }
            try { dsJSON.put("i", defaultSettings.italic); }
            catch (JSONException e) { e.printStackTrace(); }
            try { dsJSON.put("u", defaultSettings.underline); }
            catch (JSONException e) { e.printStackTrace(); }
            try { dsJSON.put("ts", defaultSettings.textSize); }
            catch (JSONException e) { e.printStackTrace(); }

            try { mainJSON.put("settings", dsJSON); }
            catch (JSONException e) { e.printStackTrace(); }
        }

        try { mainJSON.put("notes", newFile.unloadedNotes); }
        catch (Exception e) { e.printStackTrace(); }

        newFile.FileName = saveToFile(mainJSON.toString(), null);

        return newFile;
    }
    public static HashMap<String, ShallowSaveFile> loadAllFiles() {
        File directory = new File(applicationContext.getFilesDir(), folderName);

        HashMap<String, ShallowSaveFile> SaveFileArray = new HashMap<>();


        if (directory.exists() && directory.isDirectory()) {

            // 3. Get list of all files inside it
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {

                    String content = null;
                    try {
                        FileInputStream fis = new FileInputStream(file); // `file` is a java.io.File
                        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                        StringBuilder builder = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }

                        reader.close();
                        fis.close();

                        content = builder.toString();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace(); // file not found
                    } catch (IOException e) {
                        e.printStackTrace(); // problem reading file
                    }

                    // Do something with `content` or `file.getName()`

                    if (content == null) continue;
                    JSONObject loadedJSON;
                    try {
                        // Create JSONObject from the string (content)
                        JSONObject json = new JSONObject(content);
                        loadedJSON = json;
                    } catch (JSONException e) {
                        e.printStackTrace();  // Handle exception if the JSON is malformed
                        continue;
                    }

                    ShallowSaveFile createdSaveFile = new ShallowSaveFile();

                    // "Jarvis I need that O(1) requirement"
                    try {
                        String dateCreated = loadedJSON.getString("dc");
                        createdSaveFile.DateCreated = dateCreated;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        createdSaveFile.DateCreated = "The date tosday";
                    }
                    try {
                        String lastModified = loadedJSON.getString("lm");
                        createdSaveFile.DateModified = lastModified;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        createdSaveFile.DateModified = "The date toseday";
                    }


                    try {
                        JSONArray notes = loadedJSON.getJSONArray("notes");
                        createdSaveFile.unloadedNotes = notes;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        createdSaveFile.unloadedNotes = new JSONArray();
                    }

                    {
                        ShallowSaveFile.DefaultSettings defaultSettings = createdSaveFile.defaultSettings;
                        JSONObject loadedSettings = null;

                        try { loadedSettings = loadedJSON.getJSONObject("settings");
                        } catch (JSONException e) { e.printStackTrace(); }

                        if (loadedSettings != null) {

                            try {defaultSettings.bold = loadedSettings.getBoolean("b");
                            } catch (JSONException e) { e.printStackTrace(); }
                            try {defaultSettings.italic = loadedSettings.getBoolean("i");
                            } catch (JSONException e) { e.printStackTrace(); }
                            try {defaultSettings.underline = loadedSettings.getBoolean("u");
                            } catch (JSONException e) { e.printStackTrace(); }
                            try {defaultSettings.textSize = (float) loadedSettings.getDouble("ts");
                            } catch (JSONException e) { e.printStackTrace(); }
                        } // else there are already default values for the settings
                    }


                    String filename = file.getName();
                    createdSaveFile.FileName = filename;
                    SaveFileArray.put(filename, createdSaveFile);
                }
            }
        }

        return SaveFileArray;
    }
}
