package com.detri.bakingtime.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.detri.bakingtime.R;
import com.detri.bakingtime.adapters.RecipeListAdapter;
import com.detri.bakingtime.db.RecipeRepository;
import com.detri.bakingtime.executors.DiskIOExecutor;
import com.detri.bakingtime.models.Recipe;
import com.detri.bakingtime.models.RecipeResponse;
import com.detri.bakingtime.network.InitialRequestIdlingResource;
import com.detri.bakingtime.network.RecipeNetworkUtils;
import com.detri.bakingtime.network.RecipeRequestQueue;
import com.detri.bakingtime.viewmodels.RecipeListViewModel;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class RecipeListFragment extends Fragment {
    private RecyclerView mRecipeListView;
    private RecipeListViewModel mRecipeListViewModel;
    private RecipeListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Observer<List<Recipe>> mRecipeListObserver;
    private boolean mTwoPane = false;

    public RecipeListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitialRequestIdlingResource.increment();
        mRecipeListViewModel = ViewModelProviders.of(getActivity()).get(RecipeListViewModel.class);
        mAdapter = new RecipeListAdapter(new ArrayList<Recipe>());
        mRecipeListObserver = new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                if (recipes == null || recipes.size() == 0) {
                    fetchRecipeData();
                } else {
                    if (!InitialRequestIdlingResource.getIdlingResource().isIdleNow()) {
                        InitialRequestIdlingResource.decrement();
                    }
                    mAdapter.setRecipes(recipes);
                }
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRecipeListView = (RecyclerView) inflater.inflate(R.layout.fragment_recipe_list, container, false);
        mRecipeListView.setAdapter(mAdapter);

        if (mRecipeListView.getId() == R.id.rv_recipe_list_tablet) {
            mTwoPane = true;
            mLayoutManager = new GridLayoutManager(getActivity(), 3);
        } else {
            mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        }

        mRecipeListView.setLayoutManager(mLayoutManager);

        observeRecipes();

        return mRecipeListView;
    }

    private void observeRecipes() {
        mRecipeListViewModel.getRecipes().removeObservers(getViewLifecycleOwner());
        mRecipeListViewModel.getRecipes().observe(getViewLifecycleOwner(), mRecipeListObserver);
        Log.d("Observers", "Observing recipes");
    }

    private void fetchRecipeData() {
        RecipeRequestQueue.getInstance(getContext()).add(
                new JsonArrayRequest(RecipeNetworkUtils.RECIPE_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        RecipeResponse recipeResponse = RecipeNetworkUtils.jsonArrayToRecipeResponse(response);
                        populateRecipeDb(recipeResponse);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }));
    }

    private void populateRecipeDb(final RecipeResponse recipeResponse) {
        final RecipeRepository repo = RecipeRepository.getInstance(getContext());

        DiskIOExecutor.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                repo.insertRecipes(recipeResponse.getRecipeList());
                repo.insertRecipeSteps(recipeResponse.getRecipeStepList());
            }
        });
    }
}
