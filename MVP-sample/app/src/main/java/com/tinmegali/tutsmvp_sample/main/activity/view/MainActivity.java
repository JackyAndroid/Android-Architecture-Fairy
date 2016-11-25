
package com.tinmegali.tutsmvp_sample.main.activity.view;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tinmegali.tutsmvp_sample.R;
import com.tinmegali.tutsmvp_sample.common.StateMaintainer;
import com.tinmegali.tutsmvp_sample.main.activity.MVP_Main;
import com.tinmegali.tutsmvp_sample.main.activity.model.MainModel;
import com.tinmegali.tutsmvp_sample.main.activity.presenter.MainPresenter;
import com.tinmegali.tutsmvp_sample.main.activity.view.recycler.NotesViewHolder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MVP_Main.RequiredViewOps {

    private EditText mTextNewNote;
    private ListNotes mListAdapter;
    private ProgressBar mProgress;

    private MVP_Main.ProvidedPresenterOps mPresenter;

    // Responsible to maintain the object's integrity
    // during configurations change
    private final StateMaintainer mStateMaintainer = new StateMaintainer(getFragmentManager(), MainActivity.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupViews();
        setupMVP();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy(isChangingConfigurations());
    }

    /**
     * Setup the Views
     */
    private void setupViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        mTextNewNote = (EditText) findViewById(R.id.edit_note);
        mListAdapter = new ListNotes();
        mProgress = (ProgressBar) findViewById(R.id.progressbar);

        RecyclerView mList = (RecyclerView) findViewById(R.id.list_notes);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mList.setLayoutManager(linearLayoutManager);
        mList.setAdapter(mListAdapter);
        mList.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * Setup Model View Presenter pattern. Use a {@link StateMaintainer} to maintain the Presenter and Model instances between configuration changes. Could be done differently, using a dependency
     * injection for example.
     */
    private void setupMVP() {
        // Check if StateMaintainer has been created
        if (mStateMaintainer.firstTimeIn()) {
            // Create the Presenter
            MainPresenter presenter = new MainPresenter(this);
            // Create the Model
            MainModel model = new MainModel(presenter);
            // Set Presenter model
            presenter.setModel(model);
            // Add Presenter and Model to StateMaintainer
            mStateMaintainer.put(presenter);
            mStateMaintainer.put(model);

            // Set the Presenter as a interface
            // To limit the communication with it
            mPresenter = presenter;
        }
        // get the Presenter from StateMaintainer
        else {
            // Get the Presenter
            mPresenter = mStateMaintainer.get(MainPresenter.class.getName());
            // Updated the View in Presenter
            mPresenter.setView(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab: {
                // Add new note
                mPresenter.clickNewNote(mTextNewNote);
            }
        }
    }

    @Override
    public Context getActivityContext() {
        return this;
    }

    @Override
    public Context getAppContext() {
        return getApplicationContext();
    }

    @Override
    public void showToast(Toast toast) {
        toast.show();
    }

    @Override
    public void clearEditText() {
        mTextNewNote.setText("");
    }

    @Override
    public void showProgress() {
        mProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgress.setVisibility(View.GONE);
        ;
    }

    @Override
    public void showAlert(AlertDialog dialog) {
        dialog.show();
    }

    @Override
    public void notifyItemRemoved(int position) {
        mListAdapter.notifyItemRemoved(position);
    }

    @Override
    public void notifyItemInserted(int adapterPos) {
        mListAdapter.notifyItemInserted(adapterPos);
    }

    @Override
    public void notifyItemRangeChanged(int positionStart, int itemCount) {
        mListAdapter.notifyItemRangeChanged(positionStart, itemCount);
    }

    @Override
    public void notifyDataSetChanged() {
        mListAdapter.notifyDataSetChanged();
    }

    private class ListNotes extends RecyclerView.Adapter<NotesViewHolder> {

        @Override
        public int getItemCount() {
            return mPresenter.getNotesCount();
        }

        @Override
        public NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return mPresenter.createViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(NotesViewHolder holder, int position) {
            mPresenter.bindViewHolder(holder, position);
        }

    }
}
