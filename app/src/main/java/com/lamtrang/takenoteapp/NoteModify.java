package com.lamtrang.takenoteapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NoteModify {
    //Singleton pattern
    private static NoteModify instance;
    private Context context;
    private SQLiteDatabase db;
    private DBHelper dbHelper;
    private NoteModify(Context context) {
        this.context = context;
        this.dbHelper = new DBHelper(context);
        this.db = dbHelper.getWritableDatabase();
    }
    public static NoteModify getInstance(Context context) {
        if(instance == null) {
            instance = new NoteModify(context);
        }
        instance.context = context;
        instance.dbHelper = new DBHelper(context);
        instance.db = instance.dbHelper.getWritableDatabase();
        return instance;
    }
    //Some CRUD functions
    void insertNote(TakeNote takeNote) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TakeNoteTable.NoteEntry.COLUMN_CONTENT, takeNote.getContent());
        String strDate = Utilities.dateToString(takeNote.getCreatedDate());
        contentValues.put(TakeNoteTable.NoteEntry.COLUMN_CREATED_DATE, strDate);
        contentValues.put(TakeNoteTable.NoteEntry.COLUMN_IS_IMPORTANT, takeNote.getImportant());
        long newRowId = db.insert(TakeNoteTable.NoteEntry.TABLE_NAME, null, contentValues);
        takeNote.setNodeId(newRowId);
    }
    void updateNote(long id, TakeNote takeNote) {
        ContentValues contentValues = new ContentValues();
        String selection = TakeNoteTable.NoteEntry._ID + " =?";
        String[] selectionArgs = { String.valueOf(id) };// "WHERE" clause
        contentValues.put(TakeNoteTable.NoteEntry.COLUMN_CONTENT, takeNote.getContent());
        contentValues.put(TakeNoteTable.NoteEntry.COLUMN_IS_IMPORTANT, takeNote.getImportant());
        db.update(TakeNoteTable.NoteEntry.TABLE_NAME, contentValues,
                selection,
                selectionArgs);
    }
    void deleteNote(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = TakeNoteTable.NoteEntry._ID + "=?";
        String[] selectionArgs = { String.valueOf(id) };// "WHERE" clause
        db.delete(TakeNoteTable.NoteEntry.TABLE_NAME, selection, selectionArgs);
    }

    Cursor getCursorAllNotes() {
        //Query all using Cursor
        String[] projection = {
                TakeNoteTable.NoteEntry._ID,
                TakeNoteTable.NoteEntry.COLUMN_CONTENT,
                TakeNoteTable.NoteEntry.COLUMN_CREATED_DATE,
                TakeNoteTable.NoteEntry.COLUMN_IS_IMPORTANT
        };//like "SELECT * FROM..."
        return db.query(
                TakeNoteTable.NoteEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
    }

}
