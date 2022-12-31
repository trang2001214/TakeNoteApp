package com.lamtrang.takenoteapp;

import android.database.Cursor;

import java.util.Date;

public class TakeNote {
    private long nodeId = -1;
    private String content;
    private Boolean isImportant;
    private Date createdDate;
    public TakeNote(){

    }

    public TakeNote(long nodeId, String content, Boolean isImportant, Date createdDate) {
        this.nodeId = nodeId;
        this.content = content;
        this.isImportant = isImportant;
        this.createdDate = createdDate;
    }

    public long getNodeId() {
        return nodeId;
    }

    public void setNodeId(long nodeId) {
        this.nodeId = nodeId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getImportant() {
        return isImportant;
    }

    public void setImportant(Boolean important) {
        isImportant = important;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    public static TakeNote getNoteFromCursor(Cursor cursor) {
        //"factory method
        try {
            String content = cursor.getString(cursor.getColumnIndexOrThrow(TakeNoteTable.NoteEntry.COLUMN_CONTENT));
            Boolean isImportant = cursor.getInt(cursor.getColumnIndexOrThrow(TakeNoteTable.NoteEntry.COLUMN_IS_IMPORTANT)) > 0;
            long dateLong = cursor.getLong(cursor.getColumnIndexOrThrow(TakeNoteTable.NoteEntry.COLUMN_CREATED_DATE));
            long noteId = cursor.getLong(cursor.getColumnIndexOrThrow(TakeNoteTable.NoteEntry._ID));
            Date createdDate = new Date(dateLong);
            return new TakeNote(noteId, content,isImportant, createdDate);
        }catch (Exception e) {
            System.out.println("Cannot create TakeNote. Error: "+e.toString());
            return null;
        }
    }
}
