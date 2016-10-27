package com.tinmegali.tutsmvp_sample.main.activity.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.tinmegali.tutsmvp_sample.R;
import com.tinmegali.tutsmvp_sample.main.activity.MVP_Main;
import com.tinmegali.tutsmvp_sample.main.activity.view.recycler.NotesViewHolder;
import com.tinmegali.tutsmvp_sample.models.Note;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Presenter layer on Model View Presenter pattern
 *
 * ---------------------------------------------------
 * Created by Tin Megali on 18/03/16.
 * Project: tuts+mvp_sample
 * ---------------------------------------------------
 * <a href="http://www.tinmegali.com">tinmegali.com</a>
 * <a href="http://www.github.com/tinmegali>github</a>
 * ---------------------------------------------------
 */
public class MainPresenter implements MVP_Main.ProvidedPresenterOps, MVP_Main.RequiredPresenterOps {

    // View reference. We use as a WeakReference
    // because the Activity could be destroyed at any time
    // and we don't want to create a memory leak
    private WeakReference<MVP_Main.RequiredViewOps> mView;
    // Model reference
    private MVP_Main.ProvidedModelOps mModel;

    /**
     * Presenter Constructor
     * @param view  MainActivity
     */
    public MainPresenter(MVP_Main.RequiredViewOps view) {
        mView = new WeakReference<>(view);
    }

    /**
     * Called by View every time it is destroyed.
     * @param isChangingConfiguration   true: is changing configuration
     *                                  and will be recreated
     */
    @Override
    public void onDestroy(boolean isChangingConfiguration) {
        // View show be null every time onDestroy is called
        mView = null;
        // Inform Model about the event
        mModel.onDestroy(isChangingConfiguration);
        // Activity destroyed
        if ( !isChangingConfiguration ) {
            // Nulls Model when the Activity destruction is permanent
            mModel = null;
        }
    }

    /**
     * Return the View reference.
     * Could throw an exception if the View is unavailable.
     *
     * @return  {@link com.tinmegali.tutsmvp_sample.main.activity.MVP_Main.RequiredViewOps}
     * @throws NullPointerException when View is unavailable
     */
    private MVP_Main.RequiredViewOps getView() throws NullPointerException{
        if ( mView != null )
            return mView.get();
        else
            throw new NullPointerException("View is unavailable");
    }

    /**
     * Called by View during the reconstruction events
     * @param view  Activity instance
     */
    @Override
    public void setView(MVP_Main.RequiredViewOps view) {
        mView = new WeakReference<>(view);
    }

    /**
     * Called by Activity during MVP setup. Only called once.
     * @param model Model instance
     */
    public void setModel(MVP_Main.ProvidedModelOps model) {
        mModel = model;

        // start to load data
        loadData();
    }

    /**
     * Load data from Model in a AsyncTask
     */
    private void loadData() {
        try {
            getView().showProgress();
            new AsyncTask<Void, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Void... params) {
                    // Load data from Model
                    return mModel.loadData();
                }

                @Override
                protected void onPostExecute(Boolean result) {
                    try {
                        getView().hideProgress();
                        if (!result) // Loading error
                            getView().showToast(makeToast("Error loading data."));
                        else // success
                            getView().notifyDataSetChanged();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }.execute();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creat a Toast object with given message
     * @param msg   Toast message
     * @return      A Toast object
     */
    private Toast makeToast(String msg) {
        return Toast.makeText(getView().getAppContext(), msg, Toast.LENGTH_SHORT);
    }

    /**
     * Retrieve total Notes count from Model
     * @return  Notes size
     */
    @Override
    public int getNotesCount() {
        return mModel.getNotesCount();
    }

    /**
     * Create the RecyclerView holder and setup its view
     * @param parent    Recycler viewgroup
     * @param viewType  Holder type
     * @return          Recycler ViewHolder
     */
    @Override
    public NotesViewHolder createViewHolder(ViewGroup parent, int viewType) {
        NotesViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View viewTaskRow = inflater.inflate(R.layout.holder_notes, parent, false);
        viewHolder = new NotesViewHolder(viewTaskRow);

        return viewHolder;
    }

    /**
     * Binds ViewHolder with RecyclerView
     * @param holder    Holder to bind
     * @param position  Position on Recycler adapter
     */
    @Override
    public void bindViewHolder(final NotesViewHolder holder, int position) {
        final Note note = mModel.getNote(position);
        holder.text.setText( note.getText() );
        holder.date.setText( note.getDate() );
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickDeleteNote(note, holder.getAdapterPosition(), holder.getLayoutPosition());
            }
        });

    }

    /**
     * Retrieve Application Context
     * @return  Application context
     */
    @Override
    public Context getAppContext() {
        try {
            return getView().getAppContext();
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Retrieves Activity context
     * @return  Activity context
     */
    @Override
    public Context getActivityContext() {
        try {
            return getView().getActivityContext();
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Called by View when user clicks on new Note btn.
     * Creates a Note with text typed by the user and asks
     * Model to insert in DB.
     * @param editText  EdiText with text typed by user
     */
    @Override
    public void clickNewNote(final EditText editText) {
        getView().showProgress();
        final String noteText = editText.getText().toString();
        if ( !noteText.isEmpty() ) {
            new AsyncTask<Void, Void, Integer>() {
                @Override
                protected Integer doInBackground(Void... params) {
                    // Insert note in Model, returning result
                    return mModel.insertNote(makeNote(noteText));
                }

                @Override
                protected void onPostExecute(Integer adapterPosition) {
                    try {
                        if (adapterPosition > -1) {
                            // Insert note
                            getView().clearEditText();
                            getView().notifyItemInserted(adapterPosition + 1);
                            getView().notifyItemRangeChanged(adapterPosition, mModel.getNotesCount());
                        } else {
                            // Inform about error
                            getView().hideProgress();
                            getView().showToast(makeToast("Error creating note [" + noteText + "]"));
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }.execute();
        } else {
            try {
                getView().showToast(makeToast("Cannot add a blank note!"));
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Create a Note object with giver text
     * @param noteText  String with Note text
     * @return  A Note object
     */
    public Note makeNote(String noteText) {
        Note note = new Note();
        note.setText( noteText );
        note.setDate(getDate());
        return note;

    }

    /**
     * Get current Date as a String
     * @return  The current date
     */
    private String getDate() {
        return new SimpleDateFormat("HH:mm:ss - MM/dd/yyyy", Locale.getDefault()).format(new Date());
    }

    /**
     * Called by View when user click on delete button.
     * Create a AlertBox to confirm the action.
     * @param note          Note to delete
     * @param adapterPos    Position on adapter
     * @param layoutPos     Layout position on RecyclerView
     */
    @Override
    public void clickDeleteNote(final Note note, final int adapterPos, final int layoutPos) {
        openDeleteAlert(note, adapterPos, layoutPos);
    }

    /**
     * Create an AlertBox to confirm a delete action
     * @param note          Note to be deleted
     * @param adapterPos    Adapter postion
     * @param layoutPos     Recycler layout position
     */
    private void openDeleteAlert(final Note note, final int adapterPos, final int layoutPos){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivityContext());
        alertBuilder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Delete note if action is confirmed
                deleteNote(note, adapterPos, layoutPos);
            }
        });
        alertBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertBuilder.setTitle("Delete Note");
        alertBuilder.setMessage("Delete " + note.getText() + " ?");

        AlertDialog alertDialog = alertBuilder.create();
        try {
            getView().showAlert(alertDialog);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a asyncTask to delete the object in Model
     * @param note          Note to delete
     * @param adapterPos    Adapter position
     * @param layoutPos     Recycler layout position
     */
    public void deleteNote(final Note note, final int adapterPos, final int layoutPos) {
        getView().showProgress();
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                // Delete note on Model, returning result
                return mModel.deleteNote(note, adapterPos);
            }

            @Override
            protected void onPostExecute(Boolean result) {
                try {
                    getView().hideProgress();
                    if ( result ) {
                        // Remove item from RecyclerView
                        getView().notifyItemRemoved(layoutPos);
                        getView().showToast(makeToast("Note deleted."));
                    } else {
                        // Inform about error
                        getView().showToast(makeToast("Error deleting note["+note.getId()+"]"));
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }
        }.execute();
    }


}
