package com.detri.bakingtime.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.detri.bakingtime.dao.RecipeDao;
import com.detri.bakingtime.dao.RecipeStepDao;
import com.detri.bakingtime.models.Recipe;
import com.detri.bakingtime.models.RecipeStep;

@Database(entities = {Recipe.class, RecipeStep.class}, version = 1, exportSchema = false)
public abstract class RecipeDatabase extends RoomDatabase {
    public abstract RecipeDao recipeDao();
    public abstract RecipeStepDao recipeStepDao();
}
