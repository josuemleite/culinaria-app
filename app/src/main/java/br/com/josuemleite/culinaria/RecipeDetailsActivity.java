package br.com.josuemleite.culinaria;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.gson.Gson;

import java.util.List;

import br.com.josuemleite.culinaria.model.Recipe;
import br.com.josuemleite.culinaria.model.RecipeDetails;
import br.com.josuemleite.culinaria.model.RecipeDetailsResponse;

public class RecipeDetailsActivity extends AppCompatActivity {

    private TextView recipeInstructionsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        recipeInstructionsTextView = findViewById(R.id.recipeInstructionsTextView);

        String recipeJson = getIntent().getStringExtra("recipeDetails");
        Log.d("RecipeDetailsActivity", "Recipe JSON: " + recipeJson);

        Recipe recipeDetails = new Gson().fromJson(recipeJson, Recipe.class);

        if (recipeDetails != null) {
            String instructions = recipeDetails.getName();
            recipeInstructionsTextView.setText(instructions);
        }

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return true;
            }
        });
    }
}