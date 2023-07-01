package br.com.josuemleite.culinaria.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Recipe implements Serializable {
    @SerializedName("idMeal")
    private String id;

    @SerializedName("strMeal")
    private String name;

    @SerializedName("strInstructions")
    private String instructions;

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

    public String getIngredient1() {
        return ingredient1;
    }
}