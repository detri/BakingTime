package com.detri.bakingtime.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.detri.bakingtime.db.RecipeRepository;
import com.detri.bakingtime.models.RecipeStep;

import java.util.List;

public class RecipeStepListViewModel extends AndroidViewModel {
    private RecipeRepository repo;
    private int currentRecipe;

    public RecipeStepListViewModel(Application application, int recipeId) {
        super(application);
        repo = RecipeRepository.getInstance(application);
        currentRecipe = recipeId;
    }

    public LiveData<List<RecipeStep>> getRecipeSteps() {
        return repo.getAllRecipeSteps(currentRecipe);
    }
}
