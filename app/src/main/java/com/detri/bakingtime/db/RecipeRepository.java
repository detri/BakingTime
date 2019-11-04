package com.detri.bakingtime.db;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.detri.bakingtime.models.Recipe;
import com.detri.bakingtime.models.RecipeStep;

import java.util.List;

public class RecipeRepository {
    private RecipeDatabase recipeDb;
    private static RecipeRepository repo;

    public static RecipeRepository getInstance(Context context) {
        if (repo == null) {
            repo = new RecipeRepository(context);
        }
        return repo;
    }

    private RecipeRepository(Context context) {
        recipeDb = Room.databaseBuilder(context, RecipeDatabase.class, "baking-app").build();
    }

    public void insertRecipes(List<Recipe> recipes) {
        recipeDb.recipeDao().insertRecipes(recipes);
    }

    public LiveData<List<Recipe>> getAllRecipes() {
        return recipeDb.recipeDao().getAllRecipes();
    }

    public void insertRecipeSteps(List<RecipeStep> recipeSteps) {
        recipeDb.recipeStepDao().insertAll(recipeSteps);
    }

    public LiveData<List<RecipeStep>> getAllRecipeSteps(int recipeId) {
        return recipeDb.recipeStepDao().getAllByRecipe(recipeId);
    }

    public List<Recipe> remoteGetAllRecipes() {
        return recipeDb.recipeDao().remoteGetAllRecipes();
    }

    public RecipeStep remoteGetIngredientStep(int recipeId) {
        return recipeDb.recipeStepDao().getIngredientStepForRecipe(recipeId);
    }
}
