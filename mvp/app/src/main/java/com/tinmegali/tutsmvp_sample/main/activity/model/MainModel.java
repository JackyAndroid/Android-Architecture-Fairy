package com.tinmegali.tutsmvp_sample.main.activity.model;

import com.tinmegali.tutsmvp_sample.data.DAO;
import com.tinmegali.tutsmvp_sample.main.activity.MVP_Main;
import com.tinmegali.tutsmvp_sample.models.Note;

import java.util.ArrayList;

/**
 * Model layer on Model View Presenter Pattern
 *
 * ---------------------------------------------------
 * Created by Tin Megali on 18/03/16.
 * Project: tuts+mvp_sample
 * ---------------------------------------------------
 * <a href="http://www.tinmegali.com">tinmegali.com</a>
 * <a href="http://www.github.com/tinmegali>github</a>
 * ---------------------------------------------------
 */
public class MainModel implements MVP_Main.ProvidedModelOps {

    // Presenter reference
    private MVP_Main.RequiredPresenterOps mPresenter;
    private DAO mDAO;
    // Recycler data
    public ArrayList<Note> mNotes;

    /**
     * Main constructor, called by Activity during MVP setup
     * @param presenter Presenter instance
     */
    public MainModel(MVP_Main.RequiredPresenterOps presenter) {
        this.mPresenter = presenter;
        mDAO = new DAO( mPresenter.getAppContext() );
    }

    /**
     * Test contructor. Called only during unit testing
     * @param presenter Presenter instance
     * @param dao       DAO instance
     */
    public MainModel(MVP_Main.RequiredPresenterOps presenter, DAO dao) {
        this.mPresenter = presenter;
        mDAO = dao;
    }

    /**
     * Called by Presenter when View is destroyed
     * @param isChangingConfiguration   true configuration is changing
     */
    @Override
    public void onDestroy(boolean isChangingConfiguration) {
        if (!isChangingConfiguration) {
            mPresenter = null;
            mDAO = null;
            mNotes = null;
        }
    }

    /**
     * Loads all Data, getting notes from DB
     * @return  true with success
     */
    @Override
    public boolean loadData() {
        mNotes = mDAO.getAllNotes();
        return mNotes != null;
    }

    /**
     * Get a specific note from notes list using its array postion
     * @param position    Array position
     * @return            Note from list
     */
    @Override
    public Note getNote(int position) {
        return mNotes.get(position);
    }

    /**
     * Get Note's positon on ArrayList
     * @param note  Note to check
     * @return      Positon on ArrayList
     */
    public int getNotePosition(Note note) {
        for (int i=0; i<mNotes.size(); i++){
            if ( note.getId() == mNotes.get(i).getId())
                return i;
        }
        return -1;
    }

    /**
     * Delete a given Note form DB and ArrayList
     * @param note          Note to be deleted
     * @param adapterPos    Position on array
     * @return              true when success
     */
    @Override
    public boolean deleteNote(Note note, int adapterPos) {
        long res = mDAO.deleteNote(note);
        if ( res > 0 ) {
            mNotes.remove(adapterPos);
            return true;
        }
        return false;
    }

    /**
     * Insert a note on DB
     * @param note  Note to insert
     * @return      Note's position on ArrayList
     */
    @Override
    public int insertNote(Note note) {
        Note insertedNote = mDAO.insertNote(note);
        if ( insertedNote != null ) {
            loadData();
            return getNotePosition(insertedNote);
        }
        return -1;
    }

    /**
     * Get ArrayList size
     * @return  ArrayList size
     */
    @Override
    public int getNotesCount() {
        if ( mNotes != null )
            return mNotes.size();
        return 0;
    }
}
