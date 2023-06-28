package br.com.josuemleite.culinaria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private Button btnCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCategories = findViewById(R.id.btnCategories);
        btnCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToCategories();
            }
        });

        showWelcomeMessage();
    }

    private void navigateToCategories() {
        Intent intent = new Intent(MainActivity.this, CategoriesActivity.class);
        startActivity(intent);
    }

    private void showWelcomeMessage() {
        View rootView = findViewById(android.R.id.content);
        Snackbar.make(rootView, "Bem-vindo ao Culinária!", Snackbar.LENGTH_LONG).show();
    }
}