package br.com.josuemleite.culinaria.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecipeResponse {
    @SerializedName("meals")
    private List<Recipe> recipes;

    public List<Recipe> getRecipes() {
        return recipes;
    }
}
