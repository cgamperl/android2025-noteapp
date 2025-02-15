package at.wifi.swdev.noteapp.view;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import at.wifi.swdev.noteapp.database.entity.Note;
import at.wifi.swdev.noteapp.viewmodel.NoteViewModel;

public class SwipeCallback extends ItemTouchHelper.SimpleCallback {

    private final NoteViewModel viewModel;
    private final NoteListAdapter adapter;

    public SwipeCallback(NoteViewModel viewModel, NoteListAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
        this.viewModel = viewModel;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
        // Ist egal, wir machen keine Drag-Gesten
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        // Hier passiert das löschen

        // Das ist die Position der Notiz die gelöscht werden soll in der Adapter-Liste
        int position = viewHolder.getAdapterPosition();

        // Wie bekommen wir die Liste der Notizen?
        Note noteToDelete = adapter.getNoteAtPosition(position);

        // Notiz löschen
        viewModel.delete(noteToDelete);

        // Rückgängig machen anzeigen
        Snackbar.make(viewHolder.itemView, "Notiz wurde gelöscht", Snackbar.LENGTH_LONG).setAction("Undo", view -> {
            // Wie mache ich das Löschen rückgängig?
            viewModel.insert(noteToDelete);
        }).show();
    }
}
