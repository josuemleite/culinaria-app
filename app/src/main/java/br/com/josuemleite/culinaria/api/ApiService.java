package br.com.josuemleite.culinaria.api;

import br.com.josuemleite.culinaria.model.CategoriesResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("categories.php")
    Call<CategoriesResponse> getCategories();
}
