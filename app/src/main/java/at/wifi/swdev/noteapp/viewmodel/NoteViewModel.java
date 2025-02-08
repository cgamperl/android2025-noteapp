package at.wifi.swdev.noteapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import at.wifi.swdev.noteapp.database.AppDatabase;
import at.wifi.swdev.noteapp.database.dao.NoteDao;
import at.wifi.swdev.noteapp.database.entity.Note;

public class NoteViewModel extends AndroidViewModel {

    private final NoteDao dao;
    private LiveData<List<Note>> allNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        dao = AppDatabase.getInstance(application).getNoteDao();
    }

    public LiveData<List<Note>> getAllNotes() {
        // LiveData kümmert sich selbst darum, dass die Abfrage in einem eigenen Thread ausgeführt wird

        // Gibt es schon eine Verbindung?
        if (allNotes == null) {
            // Nein, wir stellen eine Verbindung her...
            allNotes = dao.getAllNotes();
        }

        return allNotes;
    }

    public void insert(Note note) {
        // Mit dem NoteDAO die Notiz in einem eingenen Thread in die DB schreiben
        // - DAO *check*
        // - ThreadPool *check*

        // Alles in execute() wird in einem eigenen Thread ausgeführt
        AppDatabase.databaseWriteExecutor.execute(() -> dao.insert(note));
    }

    public void update(Note note) {
        AppDatabase.databaseWriteExecutor.execute(() -> dao.update(note));
    }

    public void delete(Note note) {
        AppDatabase.databaseWriteExecutor.execute(() -> dao.delete(note));
    }

}
