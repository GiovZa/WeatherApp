package edu.uiuc.cs427app;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;

import edu.uiuc.cs427app.databinding.ActivityMainBinding;

import android.widget.Button;

//UI
import android.content.SharedPreferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String PREFS_NAME = "AppSettings";
    private static final String THEME_KEY = "Theme";

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser mUser = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        applySavedTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing the UI components
        // The list of locations should be customized per user (change the implementation so that
        // buttons are added to layout programmatically
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

        // get email from firebase and display title: team 33-"username"
        mAuth=FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        String user_name = mUser.getEmail();
        String username = user_name.substring(0, user_name.indexOf("@"));
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

    private void applySavedTheme() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String theme = preferences.getString(THEME_KEY, "Light Mode");
        switch (theme) {
            case "Dark Mode":
                setTheme(R.style.AppTheme_Dark);
                break;
            case "Red Theme":
                setTheme(R.style.AppTheme_Red);
                break;
            case "Blue Theme":
                setTheme(R.style.AppTheme_Blue);
                break;
            case "Light Mode":
            default:
                setTheme(R.style.AppTheme_Light);
                break;
        }
    }
}

