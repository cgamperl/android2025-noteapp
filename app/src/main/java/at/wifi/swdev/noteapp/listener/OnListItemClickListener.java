package at.wifi.swdev.noteapp.listener;

import at.wifi.swdev.noteapp.database.entity.Note;

public interface OnListItemClickListener {
    void onListItemClick(Note note, int position);
}
