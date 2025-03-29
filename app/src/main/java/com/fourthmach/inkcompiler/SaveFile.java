package com.fourthmach.inkcompiler;

import java.io.Serializable;

public class SaveFile implements Serializable {
    private String title;
    private String description;

    public SaveFile(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}