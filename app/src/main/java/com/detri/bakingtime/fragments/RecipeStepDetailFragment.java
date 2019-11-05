package com.detri.bakingtime.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.detri.bakingtime.R;
import com.detri.bakingtime.models.Ingredient;
import com.detri.bakingtime.models.RecipeStep;
import com.detri.bakingtime.viewmodels.RecipeStepDisplayViewModel;
import com.detri.bakingtime.viewmodels.RecipeStepListViewModel;
import com.detri.bakingtime.viewmodels.RecipeStepListViewModelFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

public class RecipeStepDetailFragment extends Fragment {
    private LinearLayout mRecipeStepDetailView;
    private FrameLayout mPreviousRecipe;
    private FrameLayout mNextRecipe;
    private RecipeStepDisplayViewModel recipeStepDisplayViewModel;
    private List<RecipeStep> mRecipeSteps;
    private RecipeStep mCurrentRecipeStep;
    private SimpleExoPlayer player;
    private PlayerView playerView;
    private TextView mLongDescription;
    private DataSource.Factory dataSourceFactory;
    private boolean mLandscape = false;
    private boolean mTwoPane = false;
    private long mCurrentPosition = 0;
    private boolean mPlayWhenReady = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getLong("current_position");
            mPlayWhenReady = savedInstanceState.getBoolean("play_when_ready");
        }

        recipeStepDisplayViewModel = ViewModelProviders.of(getActivity()).get(RecipeStepDisplayViewModel.class); }

    public void initPlayer() {
        super.onStart();
        player = ExoPlayerFactory.newSimpleInstance(getContext());
        dataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), "Baking Time"));
        playerView.setPlayer(player);
        observeSelectedStep();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initPlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initPlayer();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRecipeStepDetailView = (LinearLayout) inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);

        Bundle args = getArguments();
        if (args != null) {
            mRecipeSteps = getArguments().getParcelableArrayList("recipe_step_list");
            mCurrentRecipeStep = mRecipeSteps.get(0);
        } else {
            int recipeId = getActivity().getIntent().getIntExtra("recipe_id", -1);
            RecipeStepListViewModelFactory recipeStepListViewModelFactory = new RecipeStepListViewModelFactory(getActivity().getApplication(), recipeId);
            final RecipeStepListViewModel recipeStepListViewModel = ViewModelProviders.of(getActivity(), recipeStepListViewModelFactory).get(RecipeStepListViewModel.class);
            Observer<List<RecipeStep>> observer = new Observer<List<RecipeStep>>() {
                @Override
                public void onChanged(List<RecipeStep> recipeSteps) {
                    recipeStepListViewModel.getRecipeSteps().removeObserver(this);
                    mRecipeSteps = recipeSteps;
                    mCurrentRecipeStep = mRecipeSteps.get(0);
                }
            };
            recipeStepListViewModel.getRecipeSteps().observe(getViewLifecycleOwner(), observer);
        }

        if (mRecipeStepDetailView.getId() == R.id.ll_recipe_step_detail_tablet) {
            mTwoPane = true;
        }

        // determine landscape
        playerView = mRecipeStepDetailView.findViewById(R.id.exo_player_land);
        if (playerView != null) {
            mLandscape = true;
        } else {
            playerView = mRecipeStepDetailView.findViewById(R.id.exo_player);
            mPreviousRecipe = mRecipeStepDetailView.findViewById(R.id.fl_previous_step);
            mNextRecipe = mRecipeStepDetailView.findViewById(R.id.fl_next_step);
            mLongDescription = mRecipeStepDetailView.findViewById(R.id.tv_recipe_step_long_description);

            if (!mTwoPane) {
                mPreviousRecipe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int stepId = mCurrentRecipeStep.getStepId();
                        if (stepId == -1) {
                            return;
                        }
                        for (RecipeStep recipeStep : mRecipeSteps) {
                            if (recipeStep.getStepId() == stepId - 1) {
                                recipeStepDisplayViewModel.selectStep(recipeStep);
                            }
                        }
                    }
                });
                mNextRecipe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int stepId = mCurrentRecipeStep.getStepId();
                        if (stepId == mRecipeSteps.get(mRecipeSteps.size() - 1).getStepId()) {
                            return;
                        }
                        for (RecipeStep recipeStep : mRecipeSteps) {
                            if (recipeStep.getStepId() == stepId + 1) {
                                recipeStepDisplayViewModel.selectStep(recipeStep);
                            }
                        }
                    }
                });
            }
        }

        return mRecipeStepDetailView;
    }

    public void observeSelectedStep() {
        recipeStepDisplayViewModel.getSelectedStep().observe(getViewLifecycleOwner(), new Observer<RecipeStep>() {
            @Override
            public void onChanged(RecipeStep recipeStep) {
                mCurrentRecipeStep = recipeStep;
                Log.d("Current Recipe Step", "" + recipeStep.getStepId());

                player.stop(true);

                if (mTwoPane || !mLandscape) {
                    if (recipeStep.getStepId() == -1) {
                        for (Ingredient ingredient : recipeStep.getIngredientList()) {
                            mLongDescription.append("- " + ingredient.getQuantity() + " " + ingredient.getMeasure() + " " + ingredient.getIngredient() + "\n");
                        }
                    } else {
                        mLongDescription.setText(recipeStep.getDescription());
                        setPlayerVideo(recipeStep);
                    }
                } else {
                    setPlayerVideo(recipeStep);
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
            mCurrentPosition = player.getCurrentPosition();
            mPlayWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong("current_position", mCurrentPosition);
        outState.putBoolean("play_when_ready", mPlayWhenReady);
        super.onSaveInstanceState(outState);
    }

    public void setPlayerVideo(RecipeStep recipeStep) {
        Uri videoUri;
        if (!recipeStep.getVideoUrl().equals("")) {
            videoUri = Uri.parse(recipeStep.getVideoUrl());
        } else if (!recipeStep.getThumbnailUrl().equals("")) {
            videoUri = Uri.parse(recipeStep.getThumbnailUrl());
        } else {
            videoUri = null;
        }

        if (videoUri != null) {
            player.prepare(new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(videoUri));
            player.seekTo(mCurrentPosition);
            player.setPlayWhenReady(mPlayWhenReady);
        } else {
            Log.d("Video", "No video");
        }
    }
}
