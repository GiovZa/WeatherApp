package edu.uiuc.cs427app;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
//firebase authentication
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser mUser = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String user_name = mUser.getEmail();
        String username = user_name.substring(0, user_name.indexOf("@"));

        // Load the theme before setting the content view
        ThemeManager.loadTheme(username, this, new ThemeManager.ThemeLoadCallback() {
            @Override
            public void onThemeLoaded() {
                // Once the theme is applied, set the content view
                setContentView(R.layout.activity_details);
                initializeUI(username);
            }
        });
    }

    private void initializeUI(String username) {
        // get email from firebase and display titl: Team 33-"username"
        setTitle(getString(R.string.app_name) + "-" + username);

        // Process the Intent payload that has opened this Activity and show the information accordingly
        String cityName = getIntent().getStringExtra("city");
        String welcome = "Welcome to " + cityName;
        String cityWeatherInfo = "Detailed information about the weather of " + cityName;

        // Initializing the GUI elements
        TextView welcomeMessage = findViewById(R.id.welcomeText);
        TextView cityInfoMessage = findViewById(R.id.cityInfo);

        welcomeMessage.setText(welcome);
        cityInfoMessage.setText(cityWeatherInfo);

        // Get the weather information from a Service that connects to a weather server and show the results
        Button buttonMap = findViewById(R.id.mapButton);
        buttonMap.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //Implement this (create an Intent that goes to a new Activity, which shows the map)
    }
}

