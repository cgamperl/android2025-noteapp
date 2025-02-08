package at.wifi.swdev.noteapp.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity(tableName = "notes")
public class Note {

    public Note() {
    }

    public Note(String title, String content) {
        this.title = title;
        this.content = content;
        priority = 1;
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        done = false;
    }

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title;
    public String content;
    public int priority;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
    public boolean done;
}
