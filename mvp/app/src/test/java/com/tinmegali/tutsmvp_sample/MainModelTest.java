package com.tinmegali.tutsmvp_sample;

import android.content.Context;

import com.tinmegali.tutsmvp_sample.data.DAO;
import com.tinmegali.tutsmvp_sample.main.activity.model.MainModel;
import com.tinmegali.tutsmvp_sample.main.activity.presenter.MainPresenter;
import com.tinmegali.tutsmvp_sample.models.Note;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * ---------------------------------------------------
 * Created by Tin Megali on 18/03/16.
 * Project: tuts+mvp_sample
 * ---------------------------------------------------
 * <a href="http://www.tinmegali.com">tinmegali.com</a>
 * <a href="http://www.github.com/tinmegali>github</a>
 * ---------------------------------------------------
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "/src/main/AndroidManifest.xml")
public class MainModelTest {

    private MainModel mModel;
    private DAO mDAO;

    @Before
    public void setup() {
        Context context = RuntimeEnvironment.application;
        mDAO = new DAO(context);

        MainPresenter mockPresenter = Mockito.mock(MainPresenter.class);
        mModel = new MainModel(mockPresenter, mDAO);
        mModel.mNotes = new ArrayList<>();

        reset(mockPresenter);
    }


    private Note createNote(String text) {
        Note note = new Note();
        note.setText(text);
        note.setDate("some date");
        return note;
    }

    @Test
    public void loadData(){

        int notesSize = 10;
        for (int i =0; i<notesSize; i++){
            mDAO.insertNote(createNote("note_" + Integer.toString(i)));
        }

        mModel.loadData();
        assertEquals(mModel.mNotes.size(), notesSize);

    }

    @Test
    public void insertNote() {
        int pos = mModel.insertNote(createNote("noteText"));
        assertTrue(pos > -1);
    }

    @Test
    public void deleteNote() {
        Note note = createNote("testNote");
        Note insertedNote = mDAO.insertNote(note);
        mModel.mNotes = new ArrayList<>();
        mModel.mNotes.add(insertedNote);

        assertTrue(mModel.deleteNote(insertedNote, 0));

        Note fakeNote = createNote("fakeNote");
        assertFalse(mModel.deleteNote(fakeNote, 0));
    }
}
