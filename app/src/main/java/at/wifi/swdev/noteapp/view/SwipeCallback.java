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

    public SwipeCallback(Context context, NoteViewModel viewModel, NoteListAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
        this.viewModel = viewModel;

        deleteIcon = ContextCompat.getDrawable(context, R.drawable.baseline_delete_outline_24);
        deleteBackground = new ColorDrawable(context.getColor(R.color.red));
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

    @Override
    public void onChildDraw(@NonNull Canvas canvas, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        View itemView = viewHolder.itemView;
        int iconMargin = (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
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

        } else if (dX < 0) {
            // Wir swipen von rechts nach links

            // Wir lassen das Icon von rechts "reinfahren"
            int slideIn = 0;
            int slideWidth = iconMargin + deleteIcon.getIntrinsicWidth() + iconMargin;

            if (-dX < slideWidth) {
                slideIn = (int) dX + slideWidth;
            }

            deleteBackground.setBounds(
                    itemView.getRight() + (int) dX, // Plus, weil dX schon negativ ist
                    itemView.getTop(),
                    itemView.getRight(),
                    itemView.getBottom()
            );

            deleteIcon.setBounds(
                    itemView.getRight() - iconMargin - deleteIcon.getIntrinsicWidth() + slideIn,
                    iconTop,
                    itemView.getRight() - iconMargin + slideIn,
                    iconBottom
            );


        } else {
            // Hintergrund löschen
            deleteBackground.setBounds(0, 0, 0, 0);
            deleteIcon.setBounds(0, 0, 0, 0);
        }

        deleteBackground.draw(canvas);
        deleteIcon.draw(canvas);
    }
}
