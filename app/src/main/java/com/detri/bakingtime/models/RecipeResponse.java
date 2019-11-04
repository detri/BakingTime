package com.detri.bakingtime.models;

import java.util.List;

public class RecipeResponse {
    private List<Recipe> recipeList;
    private List<RecipeStep> recipeStepList;

    public RecipeResponse(List<Recipe> recipes, List<RecipeStep> recipeSteps) {
        recipeList = recipes;
        recipeStepList = recipeSteps;
    }

    public List<Recipe> getRecipeList() {
        return recipeList;
    }

    public List<RecipeStep> getRecipeStepList() {
        return recipeStepList;
    }

    public void setRecipeList(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    public void setRecipeStepList(List<RecipeStep> recipeStepList) {
        this.recipeStepList = recipeStepList;
    }
}
