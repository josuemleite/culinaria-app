package br.com.josuemleite.culinaria;

import android.content.SharedPreferences;
import android.os.Bundle;
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

import br.com.josuemleite.culinaria.fragments.RecipeDetailsFragment;
import br.com.josuemleite.culinaria.model.Recipe;

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

        // Verifique se há extras na Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String recipeJson = extras.getString("recipeJson");
            Gson gson = new Gson();
            Recipe recipe = gson.fromJson(recipeJson, Recipe.class);
            recipeId = recipe.getId();
            recipeTitle = recipe.getName();
            recipeInstructions = recipe.getInstructions();
        }

        // Configura a barra de ferramentas (AppBar)
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Exiba o título da receita na barra de ferramentas
        getSupportActionBar().setTitle(recipeTitle);

        // Obtenha as receitas favoritas armazenadas nas preferências compartilhadas
        preferences = getSharedPreferences(FAVORITES_PREFERENCES, MODE_PRIVATE);

        // Verifique se a receita atual está nos favoritos
        isFavorite = preferences.getBoolean(recipeId, false);

        // Exiba o ícone de favorito com base no estado atual
        setFavoriteIcon();

        // Exiba o Fragment dos detalhes da receita
        showRecipeDetailsFragment();
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
        // Obtenha as receitas favoritas armazenadas nas preferências compartilhadas
        SharedPreferences preferences = getSharedPreferences(FAVORITES_PREFERENCES, MODE_PRIVATE);
        Set<String> favorites = preferences.getStringSet(FAVORITES_KEY, new HashSet<>());

        if (isFavorite) {
            // Se a receita for favorita, remova-a das preferências compartilhadas
            favorites.remove(recipeId);
        } else {
            // Se a receita não for favorita, adicione-a às preferências compartilhadas
            favorites.add(recipeId);
        }

        // Atualize as preferências compartilhadas com a lista atualizada de favoritos
        preferences.edit().putStringSet(FAVORITES_KEY, favorites).apply();

        // Atualize o ícone de favorito
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

        // Crie uma instância do fragmento RecipeDetailsFragment e defina os argumentos
        RecipeDetailsFragment fragment = new RecipeDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("recipeId", recipeId);
        bundle.putString("recipeIngredients", recipeIngredients); // Passa os ingredientes para o fragmento
        bundle.putString("recipeInstructions", recipeInstructions); // Passa as instruções para o fragmento
        fragment.setArguments(bundle);

        // Substitua o conteúdo do recipeDetailsContainer pelo fragmento
        fragmentTransaction.replace(R.id.recipeDetailsContainer, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Salvar o estado de favoritos no SharedPreferences
        preferences.edit().putBoolean(recipeId, isFavorite).apply();
    }
}
