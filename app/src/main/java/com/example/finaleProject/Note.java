package com.example.finaleProject;

import java.io.Serializable;
import java.util.Date;

public class Note implements Serializable {
    private String title;
    private String content;
    private Date dueDate;
    private int id;
    static int counter = 0;
    private boolean isSmsNote = false;

    public Note(String title) {
        this.title = title;
        this.content = null;
        this.dueDate = null;
        this.id = counter++;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSmsNote() {
        return isSmsNote;
    }

    public void setSmsNote() {
        isSmsNote = !isSmsNote;
    }
}