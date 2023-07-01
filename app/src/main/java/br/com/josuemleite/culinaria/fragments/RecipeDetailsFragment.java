package br.com.josuemleite.culinaria.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import br.com.josuemleite.culinaria.MainActivity;
import br.com.josuemleite.culinaria.R;
import br.com.josuemleite.culinaria.RecipeDetailsActivity;
import br.com.josuemleite.culinaria.api.ApiClient;
import br.com.josuemleite.culinaria.api.ApiService;
import br.com.josuemleite.culinaria.model.Recipe;
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

        Bundle args = getArguments();
        if (args != null) {
            recipeId = args.getString("recipeId");
            loadRecipeDetails();
        }

        return rootView;
    }

    public void loadRecipeDetails() {
        ApiService apiService = ApiClient.getInstance().getApiService();
        Call<RecipeDetailsResponse> call = apiService.getRecipeDetailsById(recipeId);
        call.enqueue(new Callback<RecipeDetailsResponse>() {
            @Override
            public void onResponse(Call<RecipeDetailsResponse> call, Response<RecipeDetailsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Recipe recipeDetails = response.body().getRecipeDetails().get(0);
                    String ingredients = recipeDetails.getIngredient1();
                    String instructions = recipeDetails.getInstructions();

                    recipeIngredientsTextView.setText(ingredients);
                    recipeInstructionsTextView.setText(instructions);
                }
            }

            @Override
            public void onFailure(Call<RecipeDetailsResponse> call, Throwable t) {
                Log.e("Login", "Erro ao conectar à API");
            }
        });
    }
}