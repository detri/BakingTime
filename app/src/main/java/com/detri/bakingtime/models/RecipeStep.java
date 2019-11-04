package com.detri.bakingtime.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Entity(foreignKeys = @ForeignKey(entity = Recipe.class,
                                  parentColumns = "id",
                                  childColumns = "recipeId",
                                  onDelete = ForeignKey.CASCADE))
public class RecipeStep implements Parcelable {
    @Ignore
    private List<Ingredient> cachedIngredients;

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo
    private int stepId;

    @ColumnInfo
    private String shortDescription;

    @ColumnInfo
    private String description;

    @ColumnInfo
    private String videoUrl;

    @ColumnInfo
    private String thumbnailUrl;

    @ColumnInfo
    private String ingredients;

    @ColumnInfo(index = true)
    private int recipeId;

    public RecipeStep(int stepId, String shortDescription, String description, String videoUrl, String thumbnailUrl, int recipeId, String ingredients) {
        this.stepId = stepId;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.recipeId = recipeId;
        this.ingredients = ingredients;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStepId() {
        return stepId;
    }

    public void setStepId(int stepId) {
        this.stepId = stepId;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public List<Ingredient> getIngredientList() {
        if (cachedIngredients == null) {
            JSONArray ingredientsArray;
            try {
                ingredientsArray = new JSONArray(ingredients);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

            cachedIngredients = jsonArrayToIngredientList(ingredientsArray);
        }

        return cachedIngredients;
    }

    private List<Ingredient> jsonArrayToIngredientList(JSONArray ingredientsArray) {
        List<Ingredient> ingredientList = new ArrayList<>();

        int quantity = 0;
        String measure = "";
        String ingredient = "";

        for (int i = 0; i < ingredientsArray.length(); i++) {
            JSONObject ingredientObject = ingredientsArray.optJSONObject(i);
            if (ingredientObject == null) {
                continue;
            }
            Ingredient ingredientToAdd = new Ingredient(quantity, measure, ingredient);
            ingredientToAdd.setQuantity(ingredientObject.optInt("quantity", quantity));
            ingredientToAdd.setMeasure(ingredientObject.optString("measure", measure));
            ingredientToAdd.setIngredient(ingredientObject.optString("ingredient", ingredient));
            ingredientList.add(ingredientToAdd);
        }

        return ingredientList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(stepId);
        dest.writeInt(recipeId);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoUrl);
        dest.writeString(thumbnailUrl);
        dest.writeString(ingredients);
    }

    public static Parcelable.Creator<RecipeStep> CREATOR = new Parcelable.Creator<RecipeStep>() {
        @Override
        public RecipeStep createFromParcel(Parcel source) {
            return new RecipeStep(source);
        }

        @Override
        public RecipeStep[] newArray(int size) {
            return new RecipeStep[size];
        }
    };

    @Ignore
    public RecipeStep(Parcel in) {
        id = in.readInt();
        stepId = in.readInt();
        recipeId = in.readInt();
        shortDescription = in.readString();
        description = in.readString();
        videoUrl = in.readString();
        thumbnailUrl = in.readString();
        ingredients = in.readString();
    }
}
