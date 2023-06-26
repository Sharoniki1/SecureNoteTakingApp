package com.example.securenoteslib;

import java.io.Serializable;

public class Note implements Serializable {

    private String note_name;
    private String note_content;
    private int index = -1;

    public Note(){}

    public String getNote_name() {
        return note_name;
    }

    public Note setNote_name(String note_name) {
        this.note_name = note_name;
        return this;
    }

    public String getNote_content() {
        return note_content;
    }

    public Note setNote_content(String note_content) {
        this.note_content = note_content;
        return this;
    }

    public int getIndex() {
        return index;
    }

    public Note setIndex(int index) {
        this.index = index;
        return this;
    }
}
