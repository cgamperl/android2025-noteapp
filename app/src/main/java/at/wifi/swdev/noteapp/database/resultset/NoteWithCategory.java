package at.wifi.swdev.noteapp.database.resultset;

import androidx.room.ColumnInfo;
import java.time.LocalDateTime;

public class NoteWithCategory {

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
}
