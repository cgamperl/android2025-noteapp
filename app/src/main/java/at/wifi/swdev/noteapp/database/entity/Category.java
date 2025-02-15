package at.wifi.swdev.noteapp.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class Category {

    public Category(String name, String color) {
        this.name = name;
        this.color = color;
    }

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String color;

}
