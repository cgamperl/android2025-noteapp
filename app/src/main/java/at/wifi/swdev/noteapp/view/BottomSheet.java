package at.wifi.swdev.noteapp.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import at.wifi.swdev.noteapp.database.entity.Note;
import at.wifi.swdev.noteapp.databinding.BottomsheetBinding;
import at.wifi.swdev.noteapp.viewmodel.NoteViewModel;

public class BottomSheet extends BottomSheetDialogFragment {

    public static final String NOTE_KEY = "note_key";
    private BottomsheetBinding binding;
    private NoteViewModel viewModel;
    private Note noteToEdit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);

        // Mögliche Notiz "auspacken"
        // Gibt es ein Bundle?
        Bundle bundle = getArguments();

        if (bundle != null) {
            // Ja, gibt es!
            // Hat das Bundle eine Notiz?
            noteToEdit = (Note) bundle.getSerializable(NOTE_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        binding = BottomsheetBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Wollen wir eine Notiz erstellen oder bearbeiten?
        // -> Wenn noteToEdit nicht null ist, bearbeiten wir eine bestehende Notiz
        if (noteToEdit != null) {
            // Bearbeiten -> Formular vorausfüllen
            binding.titleET.setText(noteToEdit.title);
            binding.contentET.setText(noteToEdit.content);
        }

        binding.saveBtn.setOnClickListener(v -> {

            // Schritt 1: Inhalt der Eingabefelder auslesen
            String title = binding.titleET.getText().toString();
            String content = binding.contentET.getText().toString();

            // TODO: Schritt 2: Validierung

            if (noteToEdit != null) {
                // wir aktualisieren die bestehende Notiz mit den aktuellen Werten aus dem Formular
                noteToEdit.title = title;
                noteToEdit.content = content;
                // und schreiben diese zurück in die Datenbank
                viewModel.update(noteToEdit);
            } else {
                // Wir erstellen eine neue Notiz

                // Schritt 3: Erstellen der Notiz
                Note note = new Note(title, content);

                // Schritt 4: Notiz in Datenbank speichern
                viewModel.insert(note);
            }

            // Schritt 5: Bottomsheet schließen
            dismiss();
        });
    }
}
