package br.com.josuemleite.culinaria.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecipeDetailsResponse {
    @SerializedName("meals")
    private List<Recipe> recipeDetails;

    public List<Recipe> getRecipeDetails() {
        return recipeDetails;
    }
}
