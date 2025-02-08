package at.wifi.swdev.noteapp.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import at.wifi.swdev.noteapp.database.entity.Note;

@Dao
public interface NoteDao {

    // CRUD-Operationen (Create, Read, Update, Delete)

    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("SELECT * FROM notes")
    LiveData<List<Note>> getAllNotes();
}
