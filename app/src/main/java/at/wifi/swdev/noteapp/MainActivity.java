package at.wifi.swdev.noteapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import at.wifi.swdev.noteapp.database.entity.Category;
import at.wifi.swdev.noteapp.databinding.ActivityMainBinding;
import at.wifi.swdev.noteapp.view.BottomSheet;
import at.wifi.swdev.noteapp.view.NoteListAdapter;
import at.wifi.swdev.noteapp.view.SwipeCallback;
import at.wifi.swdev.noteapp.viewmodel.CategoryViewModel;
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

        // Seed categories
        seedCategories();

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
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeCallback(this, viewModel, adapter));
        itemTouchHelper.attachToRecyclerView(binding.recyclerView);

        binding.floatingActionButton.setOnClickListener(view -> {
            // BottomSheet anzeigen
            BottomSheet bottomSheet = new BottomSheet();
            bottomSheet.show(getSupportFragmentManager(), "CreateBottomSheet");
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Menü "einklinken"
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        // Wir implementieren die Suche
        // Schritt 1: Wir brauchen das Menu-Item
        MenuItem menuItem = menu.findItem(R.id.search);
        // Schritt 2: Wir packen aus dem MenuItem den ActionView (Suchansicht) aus
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setQueryHint("Notizen durchsuchen");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Egal für uns... (Sucht erst, wenn der Benutzer 'Enter' drückt)
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Sucht, sobald sich der Suchbegriff geändert hat
                // Wir übergeben dem Filter des Adapters den Suchbegriff
                NoteListAdapter adapter = (NoteListAdapter) binding.recyclerView.getAdapter();
                adapter.getFilter().filter(newText);

                return true;
            }
        });

        return true;
    }

    private void seedCategories() {
        // Viewmodel
        CategoryViewModel categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        categoryViewModel.getAllCategories().observe(this, categories -> {
            // Insert categories, if list is empty
            if (categories.isEmpty()) {
                Category categoryHome = new Category("Zuhause", "2B4570");
                Category categoryWork = new Category("Arbeit", "E49273");
                Category categoryHobby = new Category("Hobby", "4BA3C3");

                // Insert
                categoryViewModel.insert(categoryHome);
                categoryViewModel.insert(categoryWork);
                categoryViewModel.insert(categoryHobby);
            }
        });
    }

}