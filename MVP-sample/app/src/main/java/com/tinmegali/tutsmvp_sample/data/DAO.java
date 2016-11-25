package com.tinmegali.tutsmvp_sample.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tinmegali.tutsmvp_sample.models.Note;

import java.util.ArrayList;

/**
 * ---------------------------------------------------
 * Created by Tin Megali on 18/03/16.
 * Project: tuts+mvp_sample
 * ---------------------------------------------------
 * <a href="http://www.tinmegali.com">tinmegali.com</a>
 * <a href="http://www.github.com/tinmegali>github</a>
 * ---------------------------------------------------
 */
public class DAO {

    private DBSchema mHelper;
    private Context mContext;

    //SELECTIONS
    private static final String     SELECT_ID_BASED = DBSchema.TB_NOTES.ID + " = ? ";
    private static final String     PROJECTION_ALL  = " * ";
    public static final String      SORT_ORDER_DEFAULT  = DBSchema.TB_NOTES.ID + " DESC";

    public DAO(Context context) {
        this.mContext = context;
        mHelper = new DBSchema(mContext);
    }

    private SQLiteDatabase getReadDB(){
        return mHelper.getReadableDatabase();
    }

    private SQLiteDatabase getWriteDB(){
        return mHelper.getWritableDatabase();
    }

    public Note insertNote(Note note) {
        SQLiteDatabase db = getWriteDB();
        long id = db.insert(
                DBSchema.TABLE_NOTES,
                null,
                note.getValues()
        );
        Note insertedNote = getNote((int)id);
        db.close();
        return insertedNote;
    }

    public long deleteNote(Note note) {
        SQLiteDatabase db = getWriteDB();
        long res = db.delete(
                DBSchema.TABLE_NOTES,
                SELECT_ID_BASED,
                new String[]{Integer.toString(note.getId())}

        );
        db.close();
        return res;
    }

    public ArrayList<Note> getAllNotes() {
        SQLiteDatabase db = getReadDB();
        Cursor c = db.query(
                DBSchema.TABLE_NOTES,
                null,
                null,
                null, null, null,
                SORT_ORDER_DEFAULT
        );
        if ( c!= null) {
            c.moveToFirst();
            ArrayList<Note> notes = new ArrayList<>();
            while (!c.isAfterLast()) {
                Note note = new Note();
                note.setId( c.getInt( c.getColumnIndexOrThrow( DBSchema.TB_NOTES.ID )));
                note.setText(c.getString(c.getColumnIndexOrThrow(DBSchema.TB_NOTES.NOTE)));
                note.setDate(c.getString(c.getColumnIndexOrThrow(DBSchema.TB_NOTES.DATE)));
                notes.add(note);
                c.moveToNext();
            }
            c.close();
            db.close();
            return notes;
        } else {
            return null;
        }
    }

    public Note getNote(int id){
        SQLiteDatabase db = getReadDB();
        Cursor c = db.query(
                DBSchema.TABLE_NOTES,
                null,
                SELECT_ID_BASED,
                new String[]{Integer.toString(id)},
                null,
                null,
                null
        );
        if (c != null) {
            c.moveToFirst();
            Note note = new Note();
            note.setId(c.getInt(c.getColumnIndexOrThrow(DBSchema.TB_NOTES.ID)));
            note.setText(c.getString(c.getColumnIndexOrThrow(DBSchema.TB_NOTES.NOTE)));
            note.setDate(c.getString(c.getColumnIndexOrThrow(DBSchema.TB_NOTES.DATE)));
            c.close();
            db.close();
            return note;
        } else return null;
    }

}
