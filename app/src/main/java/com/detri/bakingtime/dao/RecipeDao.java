package com.detri.bakingtime.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.detri.bakingtime.models.Recipe;

import java.util.List;

@Dao
public interface RecipeDao {
    @Insert
    void insertRecipes(List<Recipe> recipes);

    @Query("SELECT * FROM recipe")
    LiveData<List<Recipe>> getAllRecipes();

    @Query("SELECT * FROM recipe")
    List<Recipe> remoteGetAllRecipes();
}
