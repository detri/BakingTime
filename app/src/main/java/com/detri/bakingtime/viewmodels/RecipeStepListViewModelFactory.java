package com.detri.bakingtime.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class RecipeStepListViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {
    private int recipeId;
    private Application application;

    public RecipeStepListViewModelFactory(Application application, int recipeId) {
        super(application);
        this.recipeId = recipeId;
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        try {
            return modelClass.getConstructor(Application.class, int.class).newInstance(application, recipeId);
        } catch(Exception e) {
            e.printStackTrace();
            return super.create(modelClass);
        }
    }
}
