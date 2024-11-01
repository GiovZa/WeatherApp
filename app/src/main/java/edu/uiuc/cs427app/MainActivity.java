package edu.uiuc.cs427app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;

import edu.uiuc.cs427app.databinding.ActivityMainBinding;

import android.widget.Button;

//UI
import android.content.SharedPreferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.User;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser mUser = mAuth.getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String user_name = mUser.getEmail();
        String username = user_name.substring(0, user_name.indexOf("@"));
        ThemeManager.loadTheme(username, this, new ThemeManager.ThemeLoadCallback() {
            @Override
            public void onThemeLoaded() {
                // Once the theme is applied, set the content view
                setContentView(R.layout.activity_main);
                initializeUI(username);
            }
        });
    }

    // Initializing the UI components
    // The list of locations should be customized per user (change the implementation so that
    // buttons are added to layout programmatically
    private void initializeUI(String username) {
        // Initialize UI components after setting the content view
        Button buttonChampaign = findViewById(R.id.buttonChampaign);
        Button buttonChicago = findViewById(R.id.buttonChicago);
        Button buttonLA = findViewById(R.id.buttonLA);
        Button buttonNew = findViewById(R.id.buttonAddLocation);
        Button buttonOut = findViewById(R.id.buttonSignOut);

        buttonChampaign.setOnClickListener(this);
        buttonChicago.setOnClickListener(this);
        buttonLA.setOnClickListener(this);
        buttonNew.setOnClickListener(this);
        buttonOut.setOnClickListener(this);

        // Set the title to show the username
        setTitle(getString(R.string.app_name) + "-" + username);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.buttonChampaign:
                intent = new Intent(this, DetailsActivity.class);
                intent.putExtra("city", "Champaign");
                startActivity(intent);
                break;
            case R.id.buttonChicago:
                intent = new Intent(this, DetailsActivity.class);
                intent.putExtra("city", "Chicago");
                startActivity(intent);
                break;
            case R.id.buttonLA:
                intent = new Intent(this, DetailsActivity.class);
                intent.putExtra("city", "Los Angeles");
                startActivity(intent);
                break;
            case R.id.buttonAddLocation:
                // Implement this action to add a new location to the list of locations
                break;
            case R.id.buttonSignOut:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }
}
