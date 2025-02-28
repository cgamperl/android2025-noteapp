package at.wifi.swdev.noteapp.listener;

import at.wifi.swdev.noteapp.database.resultset.NoteWithCategory;

@FunctionalInterface
public interface OnListItemClickListener {
    void onListItemClick(NoteWithCategory note, int position);
}
