package edu.uiuc.cs427app;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;

import edu.uiuc.cs427app.databinding.ActivityMainBinding;

import android.widget.Button;

//UI
import android.content.SharedPreferences;

import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.app.AlertDialog;
import android.widget.LinearLayout;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser mUser = mAuth.getCurrentUser();

    private LinearLayout locationContainer1;
    private LinearLayout locationContainer2;
    //Create database variable
    private DatabaseReference databaseReference;

    // Gets email and sets up original screen and layout
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
        locationContainer1 = findViewById(R.id.locationContainer1);
        locationContainer2 = findViewById(R.id.locationContainer2);
        // Create Button for Adding Location
        Button buttonNew = findViewById(R.id.buttonAddLocation);
        Button buttonOut = findViewById(R.id.buttonSignOut);

        buttonNew.setOnClickListener(this);
        buttonOut.setOnClickListener(this);

        // get email from firebase and display title: team 33-"username"
        setTitle(getString(R.string.app_name) + "-" + username);

        //show saved locations
        DatabaseReference citiesRef = FirebaseDatabase.getInstance().getReference().child("users").child(username).child("cities");
        citiesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot citySnapshot : snapshot.getChildren()) {
                        // Get the city name from the snapshot
                        String city = citySnapshot.getValue(String.class);
                        addLocationButton(city);
                        addDeleteLocationButton(city);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Creates onclick for adding and signout buttons
    @Override
    public void onClick(View view) {
      Intent intent;
        switch (view.getId()) {
            case R.id.buttonAddLocation:
                showAddLocationDialog();
                break;

            case R.id.buttonSignOut:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    // Popup dialog after add location button is pressed to add a new location
    private void showAddLocationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add a Location");

        final EditText input = new EditText(this);
        input.setHint("Enter city name");
        builder.setView(input);

        // Creates button and lets you add city to firebase, calls other functions as well
        builder.setPositiveButton("Add", (dialog, which) -> {
            String city = input.getText().toString().trim();

            if (!city.isEmpty()) {
                addCityToFirebase(city);
                addLocationButton(city);
                addDeleteLocationButton(city);
                Toast.makeText(this, city + " added!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please enter a city name.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
    //Adding cities to the firebase database
    private void addCityToFirebase(String city) {
        // Generate a unique key for each city and save under the user's ID
        String userEmail = mUser.getEmail();
        String userId = userEmail.substring(0, userEmail.indexOf("@"));
        databaseReference = FirebaseDatabase.getInstance().getReference();
        String cityId = databaseReference.child("users").child(userId).child("cities").push().getKey(); //databaseReference.push().getKey();
        if (cityId != null) {
            databaseReference.child("users").child(userId).child("cities").child(cityId).setValue(city)//(cityId).setValue(city)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "City added to Firebase!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to add city: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
    // Adds location button to go to further information screen for next Milestone
    private void addLocationButton(String city) {
        // Create a new button for the city
        Button locationButton = new Button(this);
        locationButton.setText(city);

        // Set up an OnClickListener to handle navigation to the location details
        locationButton.setOnClickListener(view -> {
            Toast.makeText(this, "Selected " + city, Toast.LENGTH_SHORT).show();

        });

        // Add the button to the container
        locationContainer1.addView(locationButton);
    }
    // Adds delete button to be used to delete city from Firebase
    private void addDeleteLocationButton(String city) {
        // Create a new button for the city
        Button deleteLocationButton = new Button(this);
        deleteLocationButton.setText("Delete");

        // Set up an OnClickListener to handle navigation to the location details
        deleteLocationButton.setOnClickListener(view -> {
            removeCityFromFirebase(city);
        });

        // Add the button to the container
        locationContainer2.addView(deleteLocationButton);
    }
    // To remove city from Firebase, uses listener to query since no direct way due to asynchronous
    private void removeCityFromFirebase(String city) {
        String userEmail = mUser.getEmail();
        String userId = userEmail.substring(0, userEmail.indexOf("@"));
        DatabaseReference citiesRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(userId).child("cities");

        // Query cities list to find equivalent city
        Query citiesQuery = citiesRef.orderByValue().equalTo(city);

        citiesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot citySnapshot : dataSnapshot.getChildren()) {
                    // Remove the city using its unique key
                    citySnapshot.getRef().removeValue()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getApplicationContext(), "City removed from Firebase!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getApplicationContext(), "Failed to remove city: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }
            }

            // If DB error, log it
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("RemoveCity", "onCancelled", databaseError.toException());
            }
        });
    }
}
