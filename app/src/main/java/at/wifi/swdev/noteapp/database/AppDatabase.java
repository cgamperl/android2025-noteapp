package at.wifi.swdev.noteapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import at.wifi.swdev.noteapp.database.converters.LocalDateTimeConverter;
import at.wifi.swdev.noteapp.database.dao.CategoryDao;
import at.wifi.swdev.noteapp.database.dao.NoteDao;
import at.wifi.swdev.noteapp.database.entity.Category;
import at.wifi.swdev.noteapp.database.entity.Note;

@Database(version = 1, entities = {Note.class, Category.class}, exportSchema = false)
@TypeConverters({LocalDateTimeConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    // Es soll nicht möglich sein, mehr als eine Instanz der Datenbank zu erstellen (=SINGLETON)
    private static AppDatabase INSTANCE;

    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    public static AppDatabase getInstance(Context context) {

        // Wir erstellen einen geschützten Bereich, den immer nur 1 Thread betreten kann
        // Befindet sich schon 1 Thread im geschützten Bereich wird jeder andere Thread, der versucht diesen Bereich
        // zu betreten pausiert / blockiert, bis der Bereich wieder frei ist.
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                // Wenn es noch keine Instanz gibt -> neue erstellen
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, AppDatabase.class, "database.sqlite").build();
                }
            }
        }
        // Wenn er bereits eine Instanz gibt, geben wir diese zurück
        return INSTANCE;

    }

    // DAOs in der Datenbank registrieren
    public abstract NoteDao getNoteDao();

    public abstract CategoryDao getCategoryDao();

}
