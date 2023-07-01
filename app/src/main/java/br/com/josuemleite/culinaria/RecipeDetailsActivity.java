package br.com.josuemleite.culinaria;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Set;

import br.com.josuemleite.culinaria.api.ApiClient;
import br.com.josuemleite.culinaria.api.ApiService;
import br.com.josuemleite.culinaria.fragments.RecipeDetailsFragment;
import br.com.josuemleite.culinaria.model.Recipe;
import br.com.josuemleite.culinaria.model.RecipeDetailsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeDetailsActivity extends AppCompatActivity {

    private static final String FAVORITES_PREFERENCES = "favorites_preferences";
    private static final String FAVORITES_KEY = "favorites_key";

    private String recipeId;
    private String recipeTitle;
    private String recipeIngredients;
    private String recipeInstructions;
    private boolean isFavorite;

    private MenuItem favoriteMenuItem;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            recipeId = extras.getString("recipeId");
            recipeTitle = extras.getString("recipeTitle");
            loadRecipeDetails();
        }

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        View contentView = findViewById(android.R.id.content);
        registerForContextMenu(contentView);

        getSupportActionBar().setTitle(recipeTitle);

        preferences = getSharedPreferences(FAVORITES_PREFERENCES, MODE_PRIVATE);

        isFavorite = preferences.getBoolean(recipeId, false);

        setFavoriteIcon();

        showRecipeDetailsFragment();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu_recipe_details, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_share) {
            shareRecipe();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private void shareRecipe() {
        String recipeText = "Confira essa deliciosa receita: " + recipeTitle + "\n\n" +
                "Ingredientes:\n" + recipeIngredients + "\n\n" +
                "Instruções:\n" + recipeInstructions;

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Receita: " + recipeTitle);
        shareIntent.putExtra(Intent.EXTRA_TEXT, recipeText);

        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(shareIntent, "Compartilhar via"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recipe_details, menu);
        favoriteMenuItem = menu.findItem(R.id.menu_favorite);
        setFavoriteIcon();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_favorite) {
            toggleFavorite();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleFavorite() {
        SharedPreferences preferences = getSharedPreferences(FAVORITES_PREFERENCES, MODE_PRIVATE);
        Set<String> favorites = preferences.getStringSet(FAVORITES_KEY, new HashSet<>());

        if (isFavorite) {
            favorites.remove(recipeId);
        } else {
            favorites.add(recipeId);
        }

        preferences.edit().putStringSet(FAVORITES_KEY, favorites).apply();

        isFavorite = !isFavorite;
        setFavoriteIcon();
    }

    private void setFavoriteIcon() {
        if (favoriteMenuItem != null) {
            int favoriteIconResId = isFavorite ? R.drawable.ic_favorite : R.drawable.ic_favorite_border;
            favoriteMenuItem.setIcon(favoriteIconResId);
        }
    }

    private void showRecipeDetailsFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        RecipeDetailsFragment fragment = new RecipeDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("recipeId", recipeId);
        bundle.putString("recipeIngredients", recipeIngredients);
        bundle.putString("recipeInstructions", recipeInstructions);
        fragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.recipeDetailsContainer, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        preferences.edit().putBoolean(recipeId, isFavorite).apply();
    }

    public void loadRecipeDetails() {
        ApiService apiService = ApiClient.getInstance().getApiService();
        Call<RecipeDetailsResponse> call = apiService.getRecipeDetailsById(recipeId);
        call.enqueue(new Callback<RecipeDetailsResponse>() {
            @Override
            public void onResponse(Call<RecipeDetailsResponse> call, Response<RecipeDetailsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Recipe recipeDetails = response.body().getRecipeDetails().get(0);
                    recipeIngredients = recipeDetails.getIngredient1();
                    recipeInstructions = recipeDetails.getInstructions();
                }
            }

            @Override
            public void onFailure(Call<RecipeDetailsResponse> call, Throwable t) {
                Log.e("Login", "Erro ao conectar à API");
            }
        });
    }
}
