package edu.uiuc.cs427app;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.Intent;
import android.util.Patterns;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import android.util.Log;
import java.util.regex.Pattern;

// UI Imports
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.content.SharedPreferences;

public class SignUpActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "AppSettings";
    private static final String THEME_KEY = "Theme";

    TextView txt_SignIn;
    TextInputEditText user_name, e_mail, pass_word, confirm_password;
    ProgressBar signUp_bar;
    MaterialButton btn_signUp;
    FirebaseAuth mAuth;

    // Password pattern (at least one digit, one lower case, one upper case, one special character, and 6-12 characters in length)
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{6,12}$"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        applySavedTheme();
        setContentView(R.layout.activity_sign_up);

        user_name = findViewById(R.id.EditEmail);
        e_mail = findViewById(R.id.SignUpEmail);
        pass_word = findViewById(R.id.SignUpPassword);
        confirm_password = findViewById(R.id.ConfirmPassword);
        signUp_bar = findViewById(R.id.SignUpBar);
        btn_signUp = findViewById(R.id.btnSignUp);
        txt_SignIn = findViewById(R.id.txtSignIn);
        mAuth = FirebaseAuth.getInstance();
        mAuth.setLanguageCode("en");

        // Initialize Spinner
        Spinner spinnerAppTheme = findViewById(R.id.spinnerAppTheme);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.layout_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAppTheme.setAdapter(adapter);

        // Set spinner to the current theme
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String currentTheme = preferences.getString(THEME_KEY, "Light Mode");
        int spinnerPosition = adapter.getPosition(currentTheme);
        spinnerAppTheme.setSelection(spinnerPosition);

        // Set up Spinner listener
        spinnerAppTheme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTheme = parent.getItemAtPosition(position).toString();
                SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                String currentTheme = preferences.getString(THEME_KEY, "Light Mode");
                if (!selectedTheme.equals(currentTheme)) {
                    saveThemePreference(selectedTheme);
                    applyThemeBasedOnSelection(selectedTheme);
                    recreate();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
            }
        });

        txt_SignIn = findViewById(R.id.txtSignIn);
        txt_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });

        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = user_name.getText().toString().trim();
                String password = pass_word.getText().toString().trim();
                String confirmPassword = confirm_password.getText().toString().trim();
                String email = e_mail.getText().toString().trim();

                if (username.isEmpty())
                {
                    user_name.setError("Username is empty");
                    user_name.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    e_mail.setError("Enter a valid email address");
                    e_mail.requestFocus();
                    return;
                }

                if (!validatePassword(password))
                {
                    pass_word.setError("Password must contain: 6-12 characters, at least one digit, one upper case, one lower case, and one special character.");
                    pass_word.requestFocus();
                    return;
                }

                if (!password.equals(confirmPassword))
                {
                    confirm_password.setError("Password and Confirm password are different.");
                    confirm_password.requestFocus();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(SignUpActivity.this, "You are successfully Signed Up", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                        }
                        else
                        {
                            Toast.makeText(SignUpActivity.this, "SignUp Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            // Logged error for debugging purposes
//                            Log.e("SignUpActivity", "SignUp Failed: " + task.getException().getMessage());
                        }
                    }
                });
            }
        });
    }


    // Applies the saved theme from preferences
    private void applySavedTheme() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String theme = preferences.getString(THEME_KEY, "Light Mode"); // Default to Light Mode if not set
        applyThemeBasedOnSelection(theme);
    }

    // Saves the selected theme to preferences
    private void saveThemePreference(String theme) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(THEME_KEY, theme);
        editor.apply();
    }

    // Applies the selected theme based on its name
    private void applyThemeBasedOnSelection(String selectedTheme) {
        switch (selectedTheme) {
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
    // Uses regex to check for numbers, special, upper, and lower chars
    private boolean validatePassword(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }
}
