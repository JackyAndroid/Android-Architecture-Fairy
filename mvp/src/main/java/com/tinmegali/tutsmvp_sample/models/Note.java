package com.tinmegali.tutsmvp_sample.models;

import android.content.ContentValues;

import com.tinmegali.tutsmvp_sample.data.DBSchema;

/**
 * ---------------------------------------------------
 * Created by Tin Megali on 18/03/16.
 * Project: tuts+mvp_sample
 * ---------------------------------------------------
 * <a href="http://www.tinmegali.com">tinmegali.com</a>
 * <a href="http://www.github.com/tinmegali>github</a>
 * ---------------------------------------------------
 */
public class Note {

    private int id = -1;
    private String mText;
    private String mDate;

    public Note() {
    }

    public Note(int id, String mText, String mDate) {
        this.id = id;
        this.mText = mText;
        this.mDate = mDate;
    }

    public Note(String mText, String mDate) {
        this.mText = mText;
        this.mDate = mDate;
    }

    public ContentValues getValues(){
        ContentValues cv = new ContentValues();
        if ( id!=-1) cv.put(DBSchema.TB_NOTES.ID, id);
        cv.put(DBSchema.TB_NOTES.NOTE, mText);
        cv.put(DBSchema.TB_NOTES.DATE, mDate);
        return cv;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public void setText(String mText) {
        this.mText = mText;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return mDate;
    }

    public String getText() {
        return mText;
    }
}
