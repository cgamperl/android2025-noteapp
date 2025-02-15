package at.wifi.swdev.noteapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import at.wifi.swdev.noteapp.databinding.ActivityMainBinding;
import at.wifi.swdev.noteapp.view.BottomSheet;
import at.wifi.swdev.noteapp.view.NoteListAdapter;
import at.wifi.swdev.noteapp.view.SwipeCallback;
import at.wifi.swdev.noteapp.viewmodel.NoteViewModel;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Toolbar aktivieren
        setSupportActionBar(binding.toolbar);

        // Ans ViewModel "andocken"
        NoteViewModel viewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        // Adapter für den RecyclerView erstellen
        NoteListAdapter adapter = new NoteListAdapter();
        adapter.setOnListItemClickListener((note, position) -> {
            // Bottomsheet anzeigen mit ausgewählter Notiz
            BottomSheet bottomSheet = new BottomSheet();

            Bundle bundle = new Bundle();
            bundle.putSerializable(BottomSheet.NOTE_KEY, note);

            // Wie bekomme ich ein Bundle in mein BottomSheet hinein?
            bottomSheet.setArguments(bundle);

            bottomSheet.show(getSupportFragmentManager(), "EditBottomSheet");
        });

        // Daten aus dem ViewModel laden und in der Liste anzeigen
        // (-> dem Adapter die Liste von Notizen übergeben)
        viewModel.getAllNotes().observe(this, notes -> adapter.setAllNotes(notes));

        // Dem RecyclerView den Adapter zuweisen
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Swiping für RecyclerView aktivieren
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeCallback(viewModel, adapter));
        itemTouchHelper.attachToRecyclerView(binding.recyclerView);

        binding.floatingActionButton.setOnClickListener(view -> {
            // BottomSheet anzeigen
            BottomSheet bottomSheet = new BottomSheet();
            bottomSheet.show(getSupportFragmentManager(), "CreateBottomSheet");
        });
    }
}