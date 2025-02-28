package at.wifi.swdev.noteapp.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import at.wifi.swdev.noteapp.database.entity.Note;
import at.wifi.swdev.noteapp.database.resultset.NoteWithCategory;

@Dao
public interface NoteDao {

    // CRUD-Operationen (Create, Read, Update, Delete)

    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("SELECT * FROM notes ORDER BY done ASC")
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT n.*, c.name AS categoryName, c.color AS categoryColor, c.id AS categoryId FROM notes n JOIN categories c ON n.category_id = c.id ORDER BY n.done ASC")
    LiveData<List<NoteWithCategory>> getAllNotesWithCategory();

}
