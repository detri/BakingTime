package com.detri.bakingtime.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.detri.bakingtime.R;
import com.detri.bakingtime.RecipeDisplayActivity;
import com.detri.bakingtime.models.Ingredient;
import com.detri.bakingtime.models.RecipeStep;

public class BakingTimeRecipeInfoWidgetProvider extends AppWidgetProvider {
    public static final String LAUNCH_RECIPE_ACTION = "launch_recipe";
    public static final String UPDATE_CURRENT_RECIPE_INGREDIENTS = "update_current_recipe_ingredients";
    private RecipeStep mCurrentIngredientStep;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Intent", "Intent received");
        if (intent.getAction().equals(LAUNCH_RECIPE_ACTION)) {
            Intent startActivityIntent = new Intent(context, RecipeDisplayActivity.class);
            startActivityIntent.putExtra("recipe_id", intent.getIntExtra("recipe_id", -1));
            context.startActivity(startActivityIntent);
        } else if (intent.getAction().equals(UPDATE_CURRENT_RECIPE_INGREDIENTS)) {
            mCurrentIngredientStep = intent.getParcelableExtra("recipe_step");
            int[] appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
            onUpdate(context, AppWidgetManager.getInstance(context), appWidgetIds);
        }
        super.onReceive(context, intent);
    }

    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_time_recipe_list);

        Log.d("Update", "Updating app widget");

        if (mCurrentIngredientStep != null) {
            Log.d("mCurrentIngredientStep", "not null");
            Intent launchIntent = new Intent(context, BakingTimeRecipeInfoWidgetProvider.class);
            launchIntent.setAction(LAUNCH_RECIPE_ACTION);
            launchIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            launchIntent.putExtra("recipe_id", mCurrentIngredientStep.getRecipeId());
            launchIntent.setData(Uri.parse(launchIntent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent launchPendingIntent = PendingIntent.getBroadcast(context, 0, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.tv_widget_ingredient_list, launchPendingIntent);

            StringBuilder ingredientListBuilder = new StringBuilder();
            for (Ingredient ingredient : mCurrentIngredientStep.getIngredientList()) {
                ingredientListBuilder.append(ingredient.getQuantity())
                        .append(" ")
                        .append(ingredient.getMeasure())
                        .append(" ")
                        .append(ingredient.getIngredient())
                        .append(" ")
                        .append("\n");
            }
            views.setTextViewText(R.id.tv_widget_ingredient_list, ingredientListBuilder.toString());
        }

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}
