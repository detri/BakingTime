package com.detri.bakingtime.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.detri.bakingtime.R;
import com.detri.bakingtime.adapters.RecipeStepListAdapter;
import com.detri.bakingtime.models.RecipeStep;
import com.detri.bakingtime.viewmodels.RecipeStepDisplayViewModel;
import com.detri.bakingtime.viewmodels.RecipeStepListViewModel;
import com.detri.bakingtime.viewmodels.RecipeStepListViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class RecipeStepListFragment extends Fragment {
    private RecyclerView mRecipeStepListView;
    private RecipeStepListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private Observer<List<RecipeStep>> mObserver;
    private RecipeStepListViewModelFactory recipeStepListViewModelFactory;
    private RecipeStepListViewModel recipeStepListViewModel;
    private RecipeStepDisplayViewModel recipeStepDisplayViewModel;
    private List<RecipeStep> mRecipeSteps;
    private boolean mTwoPane;

    public RecipeStepListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipeStepDisplayViewModel = ViewModelProviders.of(getActivity()).get(RecipeStepDisplayViewModel.class);
        mObserver = new Observer<List<RecipeStep>>() {
            @Override
            public void onChanged(List<RecipeStep> recipeSteps) {
                if (recipeSteps != null) {
                    mAdapter.setRecipeSteps(recipeSteps);
                    mRecipeSteps = recipeSteps;
                }
            }
        };
        int recipeId = getActivity().getIntent().getIntExtra("recipe_id", -1);
        recipeStepListViewModelFactory = new RecipeStepListViewModelFactory(getActivity().getApplication(), recipeId);
        recipeStepListViewModel = ViewModelProviders.of(getActivity(), recipeStepListViewModelFactory).get(RecipeStepListViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRecipeStepListView = (RecyclerView) inflater.inflate(R.layout.fragment_recipe_step_list, container, false);

        if (mRecipeStepListView.getId() == R.id.rv_recipe_step_list_tablet) {
            mTwoPane = true;
        }

        mAdapter = new RecipeStepListAdapter(new ArrayList<RecipeStep>(), new RecipeStepListAdapter.ShowRecipeDetailClickListener() {
            @Override
            public void onClick(RecipeStep recipeStep) {
                recipeStepDisplayViewModel.selectStep(recipeStep);
                RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
                Bundle recipeStepListBundle = new Bundle();
                recipeStepListBundle.putParcelableArrayList("recipe_step_list", (ArrayList<RecipeStep>) mRecipeSteps);
                fragment.setArguments(recipeStepListBundle);
                int containerIdToReplace;
                if (mTwoPane) {
                    containerIdToReplace = R.id.fl_recipe_step_display_container;
                } else {
                    containerIdToReplace = R.id.fl_recipe_step_list_container;
                }
                getFragmentManager().beginTransaction()
                        .replace(containerIdToReplace, fragment)
                        .commit();
            }
        });
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecipeStepListView.setAdapter(mAdapter);
        mRecipeStepListView.setLayoutManager(mLayoutManager);

        recipeStepListViewModel.getRecipeSteps().observe(getViewLifecycleOwner(), mObserver);

        return mRecipeStepListView;
    }
}
