package com.detri.bakingtime.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.detri.bakingtime.R;
import com.detri.bakingtime.RecipeDisplayActivity;

public class BakingTimeRecipeInfoWidgetProvider extends AppWidgetProvider {
    public static final String LAUNCH_RECIPE_ACTION = "launch_recipe";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(LAUNCH_RECIPE_ACTION)) {
            Intent startActivityIntent = new Intent(context, RecipeDisplayActivity.class);
            startActivityIntent.putExtra("recipe_id", intent.getIntExtra("recipe_id", -1));
            context.startActivity(startActivityIntent);
        }
        super.onReceive(context, intent);
    }

    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_time_recipe_list);

        Intent intent = new Intent(context, BakingTimeRecipeInfoWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        views.setRemoteAdapter(R.id.sv_recipe_list, intent);

        views.setEmptyView(R.id.sv_recipe_list, R.id.empty_view);

        Intent launchIntent = new Intent(context, BakingTimeRecipeInfoWidgetProvider.class);
        launchIntent.setAction(LAUNCH_RECIPE_ACTION);
        launchIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        launchIntent.setData(Uri.parse(launchIntent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent launchPendingIntent = PendingIntent.getBroadcast(context, 0, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.sv_recipe_list, launchPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}
