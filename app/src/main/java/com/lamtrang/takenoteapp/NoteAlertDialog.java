package com.lamtrang.takenoteapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;

import java.util.Date;

public class NoteAlertDialog extends AlertDialog {
    private View view;
    static int INSERT = 0;
    static int UPDATE = 1;
    int DATA_TYPE = INSERT;
    private Button btnComit;
    private Button btnCancel;
    private CheckBox checkBoxImportant;
    private EditText txtContent;
    private TakeNote takeNote = new TakeNote();
    NoteAlertDialog(@NonNull Context context) {
        super(context);
        setupUI();
    }
    NoteAlertDialog(@NonNull Context context, TakeNote takeNote, int dataType) {
        super(context);
        this.takeNote = takeNote;
        this.DATA_TYPE = dataType;
        setupUI();
    }
    private void setupUI() {
        view = this.getLayoutInflater().inflate(R.layout.note_alert_dialog, null);
        btnComit = view.findViewById(R.id.btnCommit);
        btnCancel = view.findViewById(R.id.btnCancel);
        checkBoxImportant = view.findViewById(R.id.checkBoxImportant);
        txtContent = view.findViewById(R.id.txtName);
        btnCancel.setOnClickListener(v -> {
            this.dismiss();
        });
        btnComit.setOnClickListener(v -> {
            String content = txtContent.getText().toString().trim();
            Boolean isImportant = checkBoxImportant.isChecked();
            if(content.equals("")){
                this.dismiss();
                return;
            }

            if(this.DATA_TYPE == NoteAlertDialog.INSERT) {
                TakeNote newTakeNote = new TakeNote(-1, content, isImportant, new Date());
                NoteModify.getInstance(this.getContext()).insertNote(newTakeNote);
            } else if(this.DATA_TYPE == NoteAlertDialog.UPDATE){
                takeNote.setContent(content);
                takeNote.setImportant(isImportant);
                NoteModify.getInstance(this.getContext()).updateNote(takeNote.getNodeId(), takeNote);
            }
            //refresh MainActivity
            MainActivity mainActivity = (MainActivity)((ContextWrapper)getContext()).getBaseContext();
            mainActivity.refreshListView();
            this.dismiss();
        });
        if(this.DATA_TYPE == NoteAlertDialog.UPDATE){
            fetchDataToUI();
        }
        this.setView(view);
    }
    private void fetchDataToUI(){
        if(takeNote.getContent().trim().length() >0){
            txtContent.setText(takeNote.getContent());
            checkBoxImportant.setChecked(takeNote.getImportant());
        }
    }
}

