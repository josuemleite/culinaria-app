package br.com.josuemleite.culinaria.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecipeDetailsResponse {
    @SerializedName("meals")
    private List<RecipeDetails> recipeDetails;

    public List<RecipeDetails> getRecipeDetails() {
        return recipeDetails;
    }
}
