package at.wifi.swdev.noteapp.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity(tableName = "notes")
public class Note implements Serializable {

    public Note() {
    }

    @Ignore
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
    @ColumnInfo(name = "created_at")
    public LocalDateTime createdAt;
    @ColumnInfo(name = "updated_at")
    public LocalDateTime updatedAt;
    public boolean done;
}
