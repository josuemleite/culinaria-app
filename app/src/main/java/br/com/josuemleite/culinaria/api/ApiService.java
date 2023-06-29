package br.com.josuemleite.culinaria.api;

import br.com.josuemleite.culinaria.model.CategoryResponse;
import br.com.josuemleite.culinaria.model.RecipeDetailsResponse;
import br.com.josuemleite.culinaria.model.RecipeResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("categories.php")
    Call<CategoryResponse> getCategories();

    @GET("filter.php")
    Call<RecipeResponse> getRecipesByCategory(@Query("c") String category);

    @GET("lookup.php")
    Call<RecipeDetailsResponse> getRecipeDetailsById(@Query("i") String recipeId);
}
