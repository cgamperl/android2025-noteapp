package at.wifi.swdev.noteapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import at.wifi.swdev.noteapp.database.AppDatabase;
import at.wifi.swdev.noteapp.database.dao.CategoryDao;
import at.wifi.swdev.noteapp.database.entity.Category;

public class CategoryViewModel extends AndroidViewModel {

    private CategoryDao categoryDao;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        categoryDao = AppDatabase.getInstance(application).getCategoryDao();
    }

    public List<Category> getAllCategories() {
        return categoryDao.getAllCategories();
    }

    public void insert(Category category) {
        AppDatabase.databaseWriteExecutor.execute(() -> categoryDao.insert(category));
    }

    public void update(Category category) {
        AppDatabase.databaseWriteExecutor.execute(() -> categoryDao.update(category));
    }

    public void delete(Category category) {
        AppDatabase.databaseWriteExecutor.execute(() -> categoryDao.delete(category));
    }


}
