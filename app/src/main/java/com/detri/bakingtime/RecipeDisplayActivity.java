package com.detri.bakingtime;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.detri.bakingtime.fragments.RecipeStepDetailFragment;
import com.detri.bakingtime.fragments.RecipeStepListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDisplayActivity extends AppCompatActivity {
    @BindView(R.id.ll_full_recipe_display)
    @Nullable
    LinearLayout mFullRecipeDisplay;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_display);
        ButterKnife.bind(this);

        if (mFullRecipeDisplay != null) {
            mTwoPane = true;
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.fl_recipe_step_list_container, new RecipeStepListFragment())
                    .commit();
            if (mTwoPane) {
                fragmentManager.beginTransaction()
                        .add(R.id.fl_recipe_step_display_container, new RecipeStepDetailFragment())
                        .commit();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
