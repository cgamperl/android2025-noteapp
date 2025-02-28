package at.wifi.swdev.noteapp.view;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import at.wifi.swdev.noteapp.R;
import at.wifi.swdev.noteapp.database.resultset.NoteWithCategory;
import at.wifi.swdev.noteapp.listener.OnListItemClickListener;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteViewHolder> implements Filterable {

    private List<NoteWithCategory> allNotes;
    private List<NoteWithCategory> filteredNotes;
    private OnListItemClickListener itemClickListener;

    /**
     * Setter, damit dem Adapter eine Liste der anzuzeigenden Notes übergeben werden kann.
     *
     * @param allNotes
     */
    public void setAllNotes(List<NoteWithCategory> allNotes) {
        this.allNotes = allNotes;
        this.filteredNotes = new ArrayList<>(allNotes);  // TODO: Bug, wenn Notizen gelöscht oder hinzugefügt werden
        notifyDataSetChanged(); // Sagt dem Adapter, dass sich seine Daten geändert haben
    }

    public void setOnListItemClickListener(OnListItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public NoteWithCategory getNoteAtPosition(int position) {
        if (this.filteredNotes != null) { // Noch besser: Prüfen, ob position auch gültig
            return this.filteredNotes.get(position);
        }

        return null;
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
        NoteWithCategory note = filteredNotes.get(position);

        // Felder dem Layout zuordnen
        holder.titleTV.setText(note.title);
        holder.contentTV.setText(note.content);

        // Text durchstreichen, wenn Notiz schon erledigt
        if (note.done) {
            holder.titleTV.setPaintFlags(holder.titleTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.contentTV.setPaintFlags(holder.contentTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        // Wenn auf den Holder geklickt wird...
        holder.itemView.setOnClickListener(view -> {
            if (itemClickListener != null) {
                // Wenn wir onListItemClick ausführen, wird der Code, den die MainActivity übergeben hat, ausgeführt
                itemClickListener.onListItemClick(note, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        // Hier geben wir einfach die Anzahl der anzuzeigenden Elemente aus
        if (filteredNotes != null) {
            return filteredNotes.size();
        }

        return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                // Läuft in einem eigenen Thread (wir müssen dafür aber nichts extra machen)

                List<NoteWithCategory> filteredResults;
                String searchTerm = charSequence.toString().toLowerCase();

                if (charSequence.length() == 0) {
                    // Suchbegriff wurde gelöscht -> Wieder alles anzeigen
                    filteredResults = new ArrayList<>(allNotes);
                } else {
                    filteredResults = allNotes.stream()
                            .filter(note -> note.title.toLowerCase().contains(searchTerm) || note.content.toLowerCase().contains(searchTerm))
                            .collect(Collectors.toList());
                }

                // Suchtreffer in FilterResults "einpacken"
                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredNotes.clear();
                filteredNotes.addAll((List) filterResults.values);
                notifyDataSetChanged();
            }
        };
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
