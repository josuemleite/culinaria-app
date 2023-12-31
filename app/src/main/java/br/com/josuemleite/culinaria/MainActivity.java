package br.com.josuemleite.culinaria;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;

    private Button btnSignIn;
    private Button btnCategories;

    FirebaseAuth auth;
    FirebaseDatabase database;
    GoogleSignInClient mGoogleSignInClient;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignIn = findViewById(R.id.btnSignIn);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Criando conta");
        progressDialog.setMessage("Nós estamos criando a tua conta");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        btnCategories = findViewById(R.id.btnCategories);
        btnCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToCategories();
            }
        });

        showWelcomeMessage();

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            navigateToCategories();
        }
    }

    private void signIn() {
        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    private void navigateToCategories() {
        Intent intent = new Intent(MainActivity.this, CategoriesActivity.class);
        startActivity(intent);
    }

    private void showWelcomeMessage() {
        View rootView = findViewById(android.R.id.content);
        Snackbar.make(rootView, "Bem-vindo ao Culinária!", Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuth(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(MainActivity.this, "Erro ao fazer Login", Toast.LENGTH_SHORT).show();
                Log.e("Login", "Erro ao fazer Login", e);
            }
        }
    }

    private void firebaseAuth(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();

                    Users users = new Users();
                    users.setUserId(user.getUid());
                    users.setName(user.getDisplayName());
                    users.setProfile(user.getPhotoUrl().toString());

                    database.getReference().child("Users").child(user.getUid()).setValue(users);

                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Erro", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}