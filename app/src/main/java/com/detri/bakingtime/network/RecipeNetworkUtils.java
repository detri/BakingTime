package com.detri.bakingtime.network;

import com.detri.bakingtime.models.Recipe;
import com.detri.bakingtime.models.RecipeResponse;
import com.detri.bakingtime.models.RecipeStep;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecipeNetworkUtils {
    public static final String RECIPE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    private static final String ID_KEY = "id";
    private static final String NAME_KEY = "name";
    private static final String INGREDIENTS_KEY = "ingredients";
    private static final String STEPS_KEY = "steps";
    private static final String SERVINGS_KEY = "servings";

    private static final String SHORT_DESCRIPTION_KEY = "shortDescription";
    private static final String DESCRIPTION_KEY = "description";
    private static final String VIDEO_URL_KEY = "videoURL";
    private static final String THUMBNAIL_URL_KEY = "thumbnailURL";

    public static RecipeResponse jsonArrayToRecipeResponse(JSONArray recipeArray) {
        List<Recipe> recipeList = new ArrayList<>();
        List<RecipeStep> recipeStepList = new ArrayList<>();

        RecipeResponse recipeResponse = new RecipeResponse(recipeList, recipeStepList);

        if (recipeArray == null) {
            return recipeResponse;
        }

        for (int i = 0; i < recipeArray.length(); i++) {
            JSONObject recipeObject = recipeArray.optJSONObject(i);
            Recipe recipe = jsonObjectToRecipe(recipeObject);
            int recipeId = recipe.getId();
            recipeList.add(recipe);
            recipeStepList.add(jsonObjectToIngredientsStep(recipeObject, recipeId));
            recipeStepList.addAll(jsonObjectToRecipeStepList(recipeObject, recipeId));
        }

        recipeResponse.setRecipeList(recipeList);
        recipeResponse.setRecipeStepList(recipeStepList);

        return recipeResponse;
    }

    private static Recipe jsonObjectToRecipe(JSONObject recipeObject) {
        int id = -1;
        String name = "Invalid Recipe";
        int servings = 0;

        Recipe recipe = new Recipe(
                id,
                name,
                servings
        );

        if (recipeObject == null) {
            return recipe;
        }

        recipe.setId(recipeObject.optInt(ID_KEY, id));
        recipe.setName(recipeObject.optString(NAME_KEY, name));
        recipe.setServings(recipeObject.optInt(SERVINGS_KEY, servings));

        return recipe;
    }

    private static RecipeStep jsonObjectToIngredientsStep(JSONObject recipeObject, int recipeId) {
        RecipeStep recipeStep = new RecipeStep(-1, null, null, null, null, recipeId, "[]");
        JSONArray ingredientsArray = recipeObject.optJSONArray(INGREDIENTS_KEY);
        if (ingredientsArray == null) {
            return recipeStep;
        }
        recipeStep.setIngredients(ingredientsArray.toString());
        recipeStep.setShortDescription("Recipe ingredients");
        return recipeStep;
    }

    private static List<RecipeStep> jsonObjectToRecipeStepList(JSONObject recipeObject, int recipeId) {
        List<RecipeStep> recipeStepList = new ArrayList<>();
        JSONArray recipeStepArray = recipeObject.optJSONArray(STEPS_KEY);

        if (recipeStepArray == null) {
            return recipeStepList;
        }

        for (int i = 0; i < recipeStepArray.length(); i++) {
            recipeStepList.add(jsonObjectToRecipeStep(recipeStepArray.optJSONObject(i), recipeId));
        }

        return recipeStepList;
    }

    private static RecipeStep jsonObjectToRecipeStep(JSONObject recipeStepObject, int recipeId) {
        int stepId = -100;
        String shortDescription = "No short description.";
        String description = "No description.";
        String videoUrl = "";
        String thumbnailUrl = "";

        RecipeStep recipeStep = new RecipeStep(stepId, shortDescription, description, videoUrl, thumbnailUrl, recipeId, "[]");

        if (recipeStepObject == null) {
            return recipeStep;
        }

        recipeStep.setStepId(recipeStepObject.optInt(ID_KEY, stepId));
        recipeStep.setShortDescription(recipeStepObject.optString(SHORT_DESCRIPTION_KEY, shortDescription));
        recipeStep.setDescription(recipeStepObject.optString(DESCRIPTION_KEY, description));
        recipeStep.setVideoUrl(recipeStepObject.optString(VIDEO_URL_KEY, videoUrl));
        recipeStep.setThumbnailUrl(recipeStepObject.optString(THUMBNAIL_URL_KEY, thumbnailUrl));

        return recipeStep;
    }
}
