package com.detri.bakingtime.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.detri.bakingtime.db.RecipeRepository;
import com.detri.bakingtime.models.Recipe;

import java.util.List;

public class RecipeListViewModel extends AndroidViewModel {
    private RecipeRepository repo;

    public RecipeListViewModel(Application application) {
        super(application);
        repo = RecipeRepository.getInstance(application);
    }

    public LiveData<List<Recipe>> getRecipes() {
        return repo.getAllRecipes();
    }
}
