package com.detri.bakingtime.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.detri.bakingtime.R;
import com.detri.bakingtime.RecipeDisplayActivity;
import com.detri.bakingtime.models.RecipeStep;

import java.util.List;

public class RecipeStepListAdapter extends RecyclerView.Adapter<RecipeStepListAdapter.RecipeStepListViewHolder> {
    private List<RecipeStep> mDataset;
    private ShowRecipeDetailClickListener listener;

    public RecipeStepListAdapter(List<RecipeStep> recipeSteps, ShowRecipeDetailClickListener listener) {
        mDataset = recipeSteps;
        this.listener = listener;
    }

    public static class RecipeStepListViewHolder extends RecyclerView.ViewHolder {
        public FrameLayout mRecipeStep;
        public TextView mRecipeStepDescription;

        public RecipeStepListViewHolder(View view) {
            super(view);
            mRecipeStep = (FrameLayout) view;
            mRecipeStepDescription = view.findViewById(R.id.tv_recipe_step_short_description);
        }
    }

    @NonNull
    @Override
    public RecipeStepListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_step_list_item, parent, false);
        return new RecipeStepListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepListViewHolder holder, int position) {
        final RecipeStep recipeStep = mDataset.get(position);

        float density = holder.mRecipeStep.getContext().getResources().getDisplayMetrics().density;
        int margin = Math.round(16 * density);
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.mRecipeStep.getLayoutParams();

        if (position == mDataset.size() - 1) {
            layoutParams.setMargins(0, margin, 0, margin);
            holder.mRecipeStep.setLayoutParams(layoutParams);
        } else {
            layoutParams.setMargins(0, margin, 0, 0);
            holder.mRecipeStep.setLayoutParams(layoutParams);
        }

        holder.mRecipeStepDescription.setText(recipeStep.getShortDescription());
        holder.mRecipeStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(recipeStep);
            }
        });
    }

    public interface ShowRecipeDetailClickListener {
        void onClick(RecipeStep recipeStep);
    }

    @Override
    public int getItemCount() {
        if (mDataset == null) {
            return 0;
        }
        return mDataset.size();
    }

    public void setRecipeSteps(List<RecipeStep> recipeSteps) {
        mDataset.clear();
        mDataset.addAll(recipeSteps);
        notifyDataSetChanged();
    }
}
