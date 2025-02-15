package at.wifi.swdev.noteapp.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import at.wifi.swdev.noteapp.database.entity.Category;

@Dao
public interface CategoryDao {

    @Insert
    public void insert(Category category);

    @Update
    public void update(Category category);

    @Delete
    public void delete(Category category);

    @Query("SELECT * FROM categories")
    public List<Category> getAllCategories();
}
