package at.wifi.swdev.noteapp.listener;

import at.wifi.swdev.noteapp.database.entity.Note;

@FunctionalInterface
public interface OnListItemClickListener {
    void onListItemClick(Note note, int position);
}
