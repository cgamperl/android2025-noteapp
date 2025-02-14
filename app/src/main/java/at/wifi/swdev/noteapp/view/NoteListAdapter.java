package at.wifi.swdev.noteapp.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import at.wifi.swdev.noteapp.R;
import at.wifi.swdev.noteapp.database.entity.Note;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteViewHolder> {

    private List<Note> allNotes;

    /**
     * Setter, damit dem Adapter eine Liste der anzuzeigenden Notes übergeben werden kann.
     *
     * @param allNotes
     */
    public void setAllNotes(List<Note> allNotes) {
        this.allNotes = allNotes;
        notifyDataSetChanged(); // Sagt dem Adapter, dass sich seine Daten geändert haben
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Ziel: ein NoteViewHolder Objekt erstellen und zurückgeben
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_list_item, parent, false);
        NoteViewHolder noteViewHolder = new NoteViewHolder(view);

        return noteViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        // Ausgangssituation:
        // * Anzeigefläche vorhanden (wurde in onCreateViewHolder erstellt)
        // * Welches Element soll angezeigt werden? (-> position)

        // Notiz die anzeigt werden soll ermitteln
        Note note = allNotes.get(position);

        // Felder dem Layout zuordnen
        holder.titleTV.setText(note.title);
        holder.contentTV.setText(note.content);
    }

    @Override
    public int getItemCount() {
        // Hier geben wir einfach die Anzahl der anzuzeigenden Elemente aus
        if (allNotes != null) {
            return allNotes.size();
        }

        return 0;
    }

    // ViewHolder ist eine "Anzeigefläche" (=Listen-Element)
    public class NoteViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleTV;
        private final TextView contentTV;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            // itemView ist das äußerste Element in note_list_item.xml (=ConstraintLayout)
            titleTV = itemView.findViewById(R.id.noteTitle);
            contentTV = itemView.findViewById(R.id.noteContent);
        }
    }

}
