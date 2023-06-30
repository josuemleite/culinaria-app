package br.com.josuemleite.culinaria.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import br.com.josuemleite.culinaria.R;
import br.com.josuemleite.culinaria.api.ApiClient;
import br.com.josuemleite.culinaria.api.ApiService;
import br.com.josuemleite.culinaria.model.Recipe;
import br.com.josuemleite.culinaria.model.RecipeDetails;
import br.com.josuemleite.culinaria.model.RecipeDetailsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeDetailsFragment extends Fragment {

    private TextView recipeIngredientsTextView;
    private TextView recipeInstructionsTextView;

    private String recipeId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);

        recipeIngredientsTextView = rootView.findViewById(R.id.recipeIngredientsTextView);
        recipeInstructionsTextView = rootView.findViewById(R.id.recipeInstructionsTextView);

        // Obtenha os argumentos do fragmento
        Bundle args = getArguments();
        if (args != null) {
            recipeId = args.getString("recipeId");
            // Faça uma chamada à API para obter os detalhes completos da receita com base no ID
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

                        // Exiba os dados nos elementos do layout
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

        return rootView;
    }
}

