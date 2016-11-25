package com.tinmegali.tutsmvp_sample.main.activity;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.tinmegali.tutsmvp_sample.main.activity.view.recycler.NotesViewHolder;
import com.tinmegali.tutsmvp_sample.models.Note;

import java.util.ArrayList;

/**
 * Holder interface that contains all interfaces
 * responsible to maintain communication between
 * Model View Presenter layers.
 * Each layer implements its respective interface:
 *      View implements RequiredViewOps
 *      Presenter implements ProvidedPresenterOps, RequiredPresenterOps
 *      Model implements ProvidedModelOps
 *
 * ---------------------------------------------------
 * Created by Tin Megali on 18/03/16.
 * Project: tuts+mvp_sample
 * ---------------------------------------------------
 * <a href="http://www.tinmegali.com">tinmegali.com</a>
 * <a href="http://www.github.com/tinmegali>github</a>
 * ---------------------------------------------------
 */
public interface MVP_Main {
    /**
     * Required View methods available to Presenter.
     * A passive layer, responsible to show data
     * and receive user interactions
     *      Presenter to View
     */
    interface RequiredViewOps {
        Context getAppContext();
        Context getActivityContext();
        void showToast(Toast toast);
        void showProgress();
        void hideProgress();
        void showAlert(AlertDialog dialog);
        void notifyItemRemoved(int position);
        void notifyDataSetChanged();
        void notifyItemInserted(int layoutPosition);
        void notifyItemRangeChanged(int positionStart, int itemCount);
        void clearEditText();
    }

    /**
     * Operations offered to View to communicate with Presenter.
     * Process user interaction, sends data requests to Model, etc.
     *      View to Presenter
     */
    interface ProvidedPresenterOps {
        void onDestroy(boolean isChangingConfiguration);
        void setView(RequiredViewOps view);
        NotesViewHolder createViewHolder(ViewGroup parent, int viewType);
        void bindViewHolder(NotesViewHolder holder, int position);
        int getNotesCount();
        void clickNewNote(EditText editText);
        void clickDeleteNote(Note note, int adapterPos, int layoutPos);
    }

    /**
     * Required Presenter methods available to Model.
     *      Model to Presenter
     */
    interface RequiredPresenterOps {
        Context getAppContext();
        Context getActivityContext();
    }

    /**
     * Operations offered to Model to communicate with Presenter
     * Handles all data business logic.
     *      Presenter to Model
     */
    interface ProvidedModelOps {
        void onDestroy(boolean isChangingConfiguration);
        int insertNote(Note note);
        boolean loadData();
        Note getNote(int position);
        boolean deleteNote(Note note, int adapterPos);
        int getNotesCount();
    }
}
