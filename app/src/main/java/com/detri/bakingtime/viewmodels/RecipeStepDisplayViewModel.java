package com.detri.bakingtime.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.detri.bakingtime.models.RecipeStep;

public class RecipeStepDisplayViewModel extends AndroidViewModel {
    private MutableLiveData<RecipeStep> mSelectedStep;

    public RecipeStepDisplayViewModel(Application application) {
        super(application);
        mSelectedStep = new MutableLiveData<>();
    }

    public void selectStep(RecipeStep recipeStep) {
        mSelectedStep.postValue(recipeStep);
    }

    public LiveData<RecipeStep> getSelectedStep() {
        return mSelectedStep;
    }
}
