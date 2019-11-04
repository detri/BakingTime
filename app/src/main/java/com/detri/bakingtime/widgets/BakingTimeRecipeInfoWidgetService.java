package com.detri.bakingtime.widgets;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.detri.bakingtime.R;
import com.detri.bakingtime.RecipeDisplayActivity;
import com.detri.bakingtime.db.RecipeRepository;
import com.detri.bakingtime.executors.DiskIOExecutor;
import com.detri.bakingtime.models.Ingredient;
import com.detri.bakingtime.models.Recipe;
import com.detri.bakingtime.models.RecipeStep;

import java.util.ArrayList;
import java.util.List;

public class BakingTimeRecipeInfoWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new BakingTimeRecipeInfoRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

class BakingTimeRecipeInfoRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private RecipeRepository repo;
    private List<Recipe> mRecipes = new ArrayList<>();
    private Context mContext;
    private int mAppWidgetId;

    public BakingTimeRecipeInfoRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        repo = RecipeRepository.getInstance(mContext);

        DiskIOExecutor.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                mRecipes = repo.remoteGetAllRecipes();
            }
        });
    }

    @Override
    public void onDestroy() {
        mRecipes.clear();
    }

    @Override
    public int getCount() {
        if (mRecipes == null) {
            return 0;
        }
        return mRecipes.size();
    }

    @Override
    public RemoteViews getViewAt(final int position) {
        final RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_recipe_list_item);
        rv.setTextViewText(R.id.tv_recipe_name, mRecipes.get(position).getName());
        rv.setTextViewText(R.id.tv_recipe_servings, mContext.getString(R.string.makes_x_servings).replace("{x}", String.valueOf(mRecipes.get(position).getServings())));
        Intent fillInIntent = new Intent();
        fillInIntent.putExtra("recipe_id", mRecipes.get(position).getId());
        rv.setOnClickFillInIntent(R.id.ll_recipe_list_item, fillInIntent);

        RecipeStep ingredientStep = repo.remoteGetIngredientStep(mRecipes.get(position).getId());
        String ingredientText = "";
        StringBuilder stringBuilder = new StringBuilder(ingredientText);
        for (Ingredient ingredient : ingredientStep.getIngredientList()) {
            stringBuilder.append(ingredient.getQuantity())
                    .append(" ")
                    .append(ingredient.getMeasure())
                    .append(" ")
                    .append(ingredient.getIngredient())
                    .append("\n");
        }
        ingredientText = stringBuilder.toString();
        rv.setTextViewText(R.id.tv_current_ingredients, ingredientText);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public long getItemId(int position) {
        return mRecipes.get(position).getId();
    }
}
