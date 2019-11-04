package com.detri.bakingtime.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.detri.bakingtime.R;
import com.detri.bakingtime.RecipeDisplayActivity;
import com.detri.bakingtime.models.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeListViewHolder> {
    private List<Recipe> mDataset;

    public RecipeListAdapter(List<Recipe> recipes) {
        mDataset = recipes;
    }

    public static class RecipeListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_recipe_name) public TextView mRecipeName;
        @BindView(R.id.tv_recipe_servings) public TextView mRecipeServings;
        public LinearLayout mRecipeItem;

        public RecipeListViewHolder(View view) {
            super(view);
            mRecipeItem = (LinearLayout) view;
            ButterKnife.bind(this, view);
        }
    }

    @NonNull
    @Override
    public RecipeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, parent, false);
        return new RecipeListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeListViewHolder holder, int position) {
        final Recipe recipe = mDataset.get(position);

        float density = holder.mRecipeItem.getContext().getResources().getDisplayMetrics().density;
        int margin = Math.round(16 * density);
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.mRecipeItem.getLayoutParams();

        if (position == mDataset.size() - 1) {
            layoutParams.setMargins(0, margin, 0, margin);
            holder.mRecipeItem.setLayoutParams(layoutParams);
        } else {
            layoutParams.setMargins(0, margin, 0, 0);
            holder.mRecipeItem.setLayoutParams(layoutParams);
        }

        holder.mRecipeName.setText(recipe.getName());
        holder.mRecipeServings.setText(holder.mRecipeServings.getContext().getString(R.string.makes_x_servings).replace("{x}", String.valueOf(recipe.getServings())));
        holder.mRecipeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent displayRecipeIntent = new Intent(context, RecipeDisplayActivity.class);
                displayRecipeIntent.putExtra("recipe_id", recipe.getId());
                context.startActivity(displayRecipeIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDataset == null) {
            return 0;
        }
        return mDataset.size();
    }

    public void setRecipes(List<Recipe> recipes) {
        mDataset.clear();
        mDataset.addAll(recipes);
        notifyDataSetChanged();
    }
}
