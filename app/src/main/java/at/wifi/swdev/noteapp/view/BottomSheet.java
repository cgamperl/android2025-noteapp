package at.wifi.swdev.noteapp.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import at.wifi.swdev.noteapp.database.entity.Category;
import at.wifi.swdev.noteapp.database.resultset.NoteWithCategory;
import at.wifi.swdev.noteapp.databinding.BottomsheetBinding;
import at.wifi.swdev.noteapp.viewmodel.CategoryViewModel;
import at.wifi.swdev.noteapp.viewmodel.NoteViewModel;

public class BottomSheet extends BottomSheetDialogFragment {

    public static final String NOTE_KEY = "note_key";
    private BottomsheetBinding binding;
    private NoteViewModel viewModel;
    private CategoryViewModel categoryViewModel;
    private NoteWithCategory noteToEdit;
    private NoteWithCategory newNote;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());
        viewModel = viewModelProvider.get(NoteViewModel.class);
        categoryViewModel = viewModelProvider.get(CategoryViewModel.class);

        // Mögliche Notiz "auspacken"
        // Gibt es ein Bundle?
        Bundle bundle = getArguments();

        if (bundle != null) {
            // Ja, gibt es!
            // Hat das Bundle eine Notiz?
            noteToEdit = (NoteWithCategory) bundle.getSerializable(NOTE_KEY);
        }

        newNote = new NoteWithCategory();
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

        categoryViewModel.getAllCategories().observe(requireActivity(), categories -> {
            // Spinner mit den Auswahlmöglichkeiten füllen
            ArrayAdapter<Category> adapter = new ArrayAdapter<>(
                    requireActivity(),
                    android.R.layout.simple_spinner_dropdown_item,
                    categories
            );
            binding.categorySpinner.setAdapter(adapter);

            binding.categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    Category selectedCategory = categories.get(position);

                    if (noteToEdit != null) {
                        noteToEdit.categoryId = selectedCategory.id;
                    } else {
                        newNote.categoryId = selectedCategory.id;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    // Do nothing
                }
            });

            if (noteToEdit != null) {
                // Start
                int position;

                for (position = 0; position < categories.size(); position++) {
                    Category category = categories.get(position);
                    if (category.id == noteToEdit.categoryId) {
                        break;
                    }
                }

                // Ziel
                binding.categorySpinner.setSelection(position);
            }

        });

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
                viewModel.update(noteToEdit.toNote());
            } else {
                // Wir erstellen eine neue Notiz

                // Schritt 3: Erstellen der Notiz
                newNote.title = title;
                newNote.content = content;

                // Schritt 4: Notiz in Datenbank speichern
                viewModel.insert(newNote.toNote());
            }

            // Schritt 5: Bottomsheet schließen
            dismiss();
        });
    }
}
