package at.wifi.swdev.noteapp.database.resultset;

import java.time.LocalDateTime;

public class NoteWithCategory {

    public int id;
    public String title;
    public String content;
    public int priority;
    public int categoryId;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
    public boolean done;
    public String categoryName;
    public String categoryColor;
}
