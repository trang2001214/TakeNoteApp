package com.lamtrang.takenoteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    private CustomAdapter customAdapter;
    private Cursor cursor;
    private int selectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("TakeNote Management");
        this.setSupportActionBar(toolbar);
        ListView listView = findViewById(R.id.listView);
        //Adapter
        cursor = NoteModify.getInstance(this).getCursorAllNotes();
        customAdapter = new CustomAdapter(this, cursor);
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectedPosition = position;
            }
        });
        registerForContextMenu(listView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemMenu = item.getItemId();
        switch (itemMenu) {
            case R.id.itemAdd:
                //Toast.makeText(this, "You press Add", Toast.LENGTH_LONG).show();
                NoteAlertDialog noteAlertDialog = new NoteAlertDialog(this);
                noteAlertDialog.show();
                break;
            case R.id.itemExit:
                Toast.makeText(this, "You press Exit", Toast.LENGTH_LONG).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.context_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo contextMenuInfo = (AdapterView.AdapterContextMenuInfo)
        item.getMenuInfo();
        selectedPosition = contextMenuInfo.position;
        switch (item.getItemId()){
            case R.id.edit:
                //Show a Dialog to edit
                Cursor cursor = (Cursor) customAdapter.getItem(selectedPosition);
                TakeNote selectedTakeNote = TakeNote.getNoteFromCursor(cursor);
                NoteAlertDialog noteAlertDialog =new NoteAlertDialog(MainActivity.this, selectedTakeNote, NoteAlertDialog.UPDATE);
                noteAlertDialog.show();
                break;
            case R.id.delete:
                //Show confirmation Dialog
                new AlertDialog.Builder(this).
                        setTitle("Delete TakeNote")
                        .setMessage("Are you sure you want to delete this?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, (DialogInterface dialog, int whichButton) -> {
                            Cursor selectedCursor = (Cursor) customAdapter.getItem(selectedPosition);
                            if(selectedCursor != null){
                                //Convert cursor => TakeNote Object
                                TakeNote takeNote = TakeNote.getNoteFromCursor(selectedCursor);
                                if(takeNote != null) {
                                    //Delete takeNote
                                    NoteModify.getInstance(MainActivity.this).deleteNote(takeNote.getNodeId());
                                    //You must refresh listview after delete
                                    refreshListView();
                                }
                            }
                        }).setNegativeButton(android.R.string.no, null).show();
                break;
        }
        return super.onContextItemSelected(item);
    }
    public void refreshListView(){
        cursor = NoteModify.getInstance(this).getCursorAllNotes();
        customAdapter.swapCursor(cursor);
    }
}