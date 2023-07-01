package br.com.josuemleite.culinaria;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import br.com.josuemleite.culinaria.api.ApiClient;
import br.com.josuemleite.culinaria.api.ApiService;
import br.com.josuemleite.culinaria.model.Recipe;
import br.com.josuemleite.culinaria.model.RecipeResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.gson.Gson;

import java.util.List;

import br.com.josuemleite.culinaria.adapter.RecipeAdapter;

public class RecipesListActivity extends AppCompatActivity implements RecipeAdapter.OnItemClickListener {

    private RecyclerView recipesRecyclerView;
    private RecipeAdapter recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);

        recipesRecyclerView = findViewById(R.id.recipesRecyclerView);
        recipesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        String selectedCategory = getIntent().getStringExtra("categoryName");

        ApiService apiService = ApiClient.getInstance().getApiService();

        Call<RecipeResponse> call = apiService.getRecipesByCategory(selectedCategory);
        call.enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                if (response.isSuccessful()) {
                    List<Recipe> recipes = response.body().getRecipes();
                    recipeAdapter = new RecipeAdapter(recipes, RecipesListActivity.this);
                    recipesRecyclerView.setAdapter(recipeAdapter);
                }
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {
                Toast.makeText(RecipesListActivity.this, "Erro ao consumir API", Toast.LENGTH_SHORT).show();
            }
        });

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

    @Override
    public void onItemClick(Recipe recipe) {
        String recipeId = recipe.getId();
        String recipeTitle = recipe.getName();

        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra("recipeId", recipeId);
        intent.putExtra("recipeTitle", recipeTitle);
        startActivity(intent);
    }
}