package at.wifi.swdev.noteapp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import at.wifi.swdev.noteapp.R;
import at.wifi.swdev.noteapp.database.entity.Note;
import at.wifi.swdev.noteapp.viewmodel.NoteViewModel;

public class SwipeCallback extends ItemTouchHelper.SimpleCallback {

    private final NoteViewModel viewModel;
    private final NoteListAdapter adapter;
    private Drawable deleteIcon;
    private ColorDrawable deleteBackground;
    private Drawable doneIcon;
    private ColorDrawable doneBackground;

    public SwipeCallback(Context context, NoteViewModel viewModel, NoteListAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
        this.viewModel = viewModel;

        deleteIcon = ContextCompat.getDrawable(context, R.drawable.baseline_delete_outline_24);
        deleteBackground = new ColorDrawable(context.getColor(R.color.red));

        doneIcon = ContextCompat.getDrawable(context, R.drawable.baseline_check_24);
        doneBackground = new ColorDrawable(context.getColor(R.color.green));
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
        // Ist egal, wir machen keine Drag-Gesten
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        // Das ist die Position der Notiz die geswiped wurde in der Adapter-Liste
        int position = viewHolder.getAdapterPosition();
        // Wie bekommen wir die Notiz selbst?
        Note selectedNote = adapter.getNoteAtPosition(position);

        if (direction == ItemTouchHelper.RIGHT) {
            // Hier passiert das löschen
            deleteNote(selectedNote, viewHolder.itemView);
        } else if (direction == ItemTouchHelper.LEFT) {
            // Notiz als erledigt markieren
            markNoteAsDone(selectedNote);
        }
    }

    private void markNoteAsDone(Note selectedNote) {
        selectedNote.done = true;
        viewModel.update(selectedNote);
    }

    private void deleteNote(Note selectedNote, View view) {
        // Notiz löschen
        viewModel.delete(selectedNote);

        // Rückgängig machen anzeigen
        Snackbar.make(view, "Notiz wurde gelöscht", Snackbar.LENGTH_LONG).setAction("Undo", v -> {
            // Wie mache ich das Löschen rückgängig?
            viewModel.insert(selectedNote);
        }).show();
    }

    @Override
    public void onChildDraw(@NonNull Canvas canvas, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        View itemView = viewHolder.itemView;
        int iconMargin = (itemView.getHeight() - doneIcon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + iconMargin;
        int iconBottom = itemView.getBottom() - iconMargin;

        if (dX > 0) {
            // Wir swipen von links nach rechts

            // Wir lassen das Icon von links "reinfahren"
            int slideIn = 0;
            int slideWidth = iconMargin + deleteIcon.getIntrinsicWidth() + iconMargin;

            if (dX < slideWidth) {
                slideIn = (int) dX - slideWidth;
            }

            deleteBackground.setBounds(
                    itemView.getLeft(),
                    itemView.getTop(),
                    itemView.getLeft() + (int) dX,
                    itemView.getBottom()
            );

            deleteIcon.setBounds(
                    itemView.getLeft() + iconMargin + slideIn,
                    iconTop,
                    itemView.getLeft() + iconMargin + deleteIcon.getIntrinsicWidth() + slideIn,
                    iconBottom
            );

            deleteBackground.draw(canvas);
            deleteIcon.draw(canvas);

            super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        } else if (dX < 0) {
            // Wir swipen von rechts nach links - MARK AS DONE

            // Wir lassen das Icon von rechts "reinfahren"
            int slideIn = 0;
            int slideWidth = iconMargin + doneIcon.getIntrinsicWidth() + iconMargin;

            // Maximale Swipedistanz
            double maxDx = -slideWidth;
            double clampedDx = Math.max(dX, maxDx);

            if (-dX < slideWidth) {
                slideIn = (int) dX + slideWidth;
            }

            doneBackground.setBounds(
                    itemView.getRight() + (int) clampedDx, // Plus, weil dX schon negativ ist
                    itemView.getTop(),
                    itemView.getRight(),
                    itemView.getBottom()
            );

            doneIcon.setBounds(
                    itemView.getRight() - iconMargin - doneIcon.getIntrinsicWidth() + slideIn,
                    iconTop,
                    itemView.getRight() - iconMargin + slideIn,
                    iconBottom
            );

            doneBackground.draw(canvas);
            doneIcon.draw(canvas);

            super.onChildDraw(canvas, recyclerView, viewHolder, (int) clampedDx, dY, actionState, isCurrentlyActive);
        } else {
            // Hintergrund löschen
            deleteBackground.setBounds(0, 0, 0, 0);
            deleteIcon.setBounds(0, 0, 0, 0);

            doneIcon.setBounds(0, 0, 0, 0);
            doneBackground.setBounds(0, 0, 0, 0);

            deleteBackground.draw(canvas);
            deleteIcon.draw(canvas);
            doneBackground.draw(canvas);
            doneIcon.draw(canvas);
        }
    }
}
