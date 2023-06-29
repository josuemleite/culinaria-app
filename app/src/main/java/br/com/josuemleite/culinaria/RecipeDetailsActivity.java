package br.com.josuemleite.culinaria;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.gson.Gson;

import java.util.List;

import br.com.josuemleite.culinaria.api.ApiClient;
import br.com.josuemleite.culinaria.api.ApiService;
import br.com.josuemleite.culinaria.model.Recipe;
import br.com.josuemleite.culinaria.model.RecipeDetails;
import br.com.josuemleite.culinaria.model.RecipeDetailsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeDetailsActivity extends AppCompatActivity {

    private TextView recipeTitleTextView;
    private TextView recipeIngredientsTitleTextView;
    private TextView recipeIngredientsTextView;
    private TextView recipeInstructionsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        recipeTitleTextView = findViewById(R.id.recipeTitleTextView);
        recipeIngredientsTitleTextView = findViewById(R.id.recipeIngredientsTitleTextView);
        recipeIngredientsTextView = findViewById(R.id.recipeIngredientsTextView);
        recipeInstructionsTextView = findViewById(R.id.recipeInstructionsTextView);

        Intent intent = getIntent();
        if (intent != null) {
            String recipeId = intent.getStringExtra("recipeId");
            if (recipeId != null) {
                // Realize uma chamada à API para obter os detalhes completos da receita com base no ID
                ApiService apiService = ApiClient.getInstance().getApiService();
                Call<RecipeDetailsResponse> call = apiService.getRecipeDetailsById(recipeId);
                call.enqueue(new Callback<RecipeDetailsResponse>() {
                    @Override
                    public void onResponse(Call<RecipeDetailsResponse> call, Response<RecipeDetailsResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            RecipeDetails recipeDetails = response.body().getRecipeDetails().get(0);
                            String title = recipeDetails.getName();
                            String ingredients = recipeDetails.getIngredient1();
                            String instructions = recipeDetails.getInstructions();

                            // Exibir os dados nos elementos do layout
                            recipeTitleTextView.setText(title);
                            recipeIngredientsTextView.setText(ingredients);
                            recipeInstructionsTextView.setText(instructions);
                        }
                    }

                    @Override
                    public void onFailure(Call<RecipeDetailsResponse> call, Throwable t) {
                        // Trate o erro na chamada da API, se necessário
                    }
                });
            }
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