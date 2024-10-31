package edu.uiuc.cs427app;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.content.ServiceConnection;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

// Login Page with Firebase Authentication and signup page link
public class LoginActivity extends AppCompatActivity {

    // Firebase implementation for user authentication
    FirebaseAuth mAuth;

    // UI
    TextView txt_SignUp, loginErrorText;
    TextInputEditText user_name, pass_word;
    ProgressBar signIn_bar;
    MaterialButton btn_signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize
        user_name=findViewById(R.id.SignInUsername);
        pass_word=findViewById(R.id.SignInPassword);
        signIn_bar=findViewById(R.id.SignInBar);
        btn_signIn=findViewById(R.id.btnSignIn);
        loginErrorText = findViewById(R.id.loginErrorText);

        txt_SignUp = findViewById(R.id.txtSignUp);
        mAuth=FirebaseAuth.getInstance();
        mAuth.setLanguageCode("en");
        // display title:team 33, values/strings.xml
        setTitle(getString(R.string.app_name));

        //  "Sign Up" text clicked
        txt_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //  "Sign In" button clicked
        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve user input from the username and password fields
                String email = String.valueOf(user_name.getText().toString());
                String password= String.valueOf(pass_word.getText().toString().trim());

                // Hide the error message text
                loginErrorText.setVisibility(View.GONE);

                // username empty guard clause
                if(email.isEmpty())
                {
                    user_name.setError("Username is empty");
                    user_name.requestFocus();
                    return;
                } else {
                    email = email + "@gmail.com";
                }
                // password empty guard clause
                if(password.isEmpty())
                {
                    pass_word.setError("Password is empty");
                    pass_word.requestFocus();
                    return;
                }

                // Attempt to sign in using Firebase authentication
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        // If login is successful, navigate to MainActivity
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                    else
                    {
                        // If login fails, display error message
                        loginErrorText.setText("Email or password is invalid");
                        loginErrorText.setVisibility(View.VISIBLE);
                        Toast.makeText(LoginActivity.this,
                                "Please Check Your login Credentials",
                                Toast.LENGTH_SHORT).show();
                    }

                });
            }

        });

    }
}
