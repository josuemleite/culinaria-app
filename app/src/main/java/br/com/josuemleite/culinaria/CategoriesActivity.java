package br.com.josuemleite.culinaria;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

import br.com.josuemleite.culinaria.adapter.CategoryAdapter;
import br.com.josuemleite.culinaria.api.ApiClient;
import br.com.josuemleite.culinaria.api.ApiService;
import br.com.josuemleite.culinaria.model.CategoryResponse;
import br.com.josuemleite.culinaria.model.Category;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriesActivity extends AppCompatActivity {

    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return true;
            }
        });

        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(this, categoryList);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoryRecyclerView.setAdapter(categoryAdapter);

        apiService = ApiClient.getInstance().getApiService();

        loadCategories();
    }

    private void loadCategories() {
        Call<CategoryResponse> call = apiService.getCategories();

        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful()) {
                    CategoryResponse categoryResponse = response.body();
                    if (categoryResponse != null) {
                        List<Category> categories = categoryResponse.getCategories();

                        categoryList.clear();
                        if (categories != null) {
                            categoryList.addAll(categories);
                        }

                        categoryAdapter.notifyDataSetChanged();
                    }
                } else {
                    Log.e("CategoriesActivity", "Erro na resposta da API: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                Log.e("CategoriesActivity", "Erro na chamada da API: " + t.getMessage());
            }
        });
    }
}
