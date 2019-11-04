package com.detri.bakingtime.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.detri.bakingtime.models.RecipeStep;

import java.util.List;

@Dao
public interface RecipeStepDao {
    @Query("SELECT * FROM recipestep WHERE recipeId = :recipeId ORDER BY stepId")
    LiveData<List<RecipeStep>> getAllByRecipe(int recipeId);

    @Query("SELECT * FROM recipestep WHERE recipeId = :recipeId AND stepId = -1")
    RecipeStep getIngredientStepForRecipe(int recipeId);

    @Insert
    void insertAll(List<RecipeStep> recipeSteps);
}
