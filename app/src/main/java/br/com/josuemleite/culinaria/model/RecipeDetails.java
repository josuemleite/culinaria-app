package br.com.josuemleite.culinaria.model;

import com.google.gson.annotations.SerializedName;

public class RecipeDetails {
    @SerializedName("idMeal")
    private String id;

    @SerializedName("strMeal")
    private String name;

    @SerializedName("strInstructions")
    private String instructions;

    @SerializedName("strCategory")
    private String category;

    @SerializedName("strArea")
    private String area;

    @SerializedName("strMealThumb")
    private String thumbnailUrl;

    @SerializedName("strIngredient1")
    private String ingredient1;


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getCategory() {
        return category;
    }

    public String getArea() {
        return area;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getIngredient1() {
        return ingredient1;
    }
}
