package at.wifi.swdev.noteapp.database.resultset;

import androidx.room.ColumnInfo;
import java.io.Serializable;
import java.time.LocalDateTime;
import at.wifi.swdev.noteapp.database.entity.Note;

public class NoteWithCategory implements Serializable {

    public int id;
    public String title;
    public String content;
    public int priority;
    @ColumnInfo(name = "category_id")
    public int categoryId;
    @ColumnInfo(name = "created_at")
    public LocalDateTime createdAt;
    @ColumnInfo(name = "updated_at")
    public LocalDateTime updatedAt;
    public boolean done;
    public String categoryName;
    public String categoryColor;

    /**
     * Erzeugt ein Objekt vom Typ Note aus der NoteWithCategory
     *
     * @return Note note
     */
    public Note toNote() {
        Note note = new Note();

        note.id = this.id;
        note.title = this.title;
        note.content = this.content;
        note.done = this.done;
        note.priority = this.priority;
        note.categoryId = this.categoryId;
        note.createdAt = this.createdAt;
        note.updatedAt = this.updatedAt;

        return note;
    }
}
