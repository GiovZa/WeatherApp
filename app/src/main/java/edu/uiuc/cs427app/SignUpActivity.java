package edu.uiuc.cs427app;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.util.Patterns;
import androidx.annotation.NonNull;
import android.widget.EditText;
import android.content.ServiceConnection;
import com.google.android.material.textfield.TextInputEditText;


import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SignUpActivity extends AppCompatActivity {

    TextView txt_SignIn;
//    FirebaseAuth mAuth;
    TextInputEditText user_name, e_mail, pass_word, confirm_password;
    ProgressBar signUp_bar;
    MaterialButton btn_signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        user_name=findViewById(R.id.SignUpUsername);
        e_mail=findViewById(R.id.SignUpEmail);
        pass_word=findViewById(R.id.SignUpPassword);
        confirm_password=findViewById(R.id.ConfirmPassword);
        signUp_bar=findViewById(R.id.SignUpBar);
        btn_signUp=findViewById(R.id.btnSignUp);
//        mAuth=FirebaseAuth.getInstance();
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
                String SignUpUsername = String.valueOf(user_name.getText().toString().trim());
                String SignUpPassword= String.valueOf(pass_word.getText().toString().trim());
                String ConfirmPassword= String.valueOf(confirm_password.getText().toString().trim());
                String SignUpEmail= String.valueOf(e_mail.getText().toString().trim());
                if(SignUpUsername.isEmpty())
                {
                    user_name.setError("Username is empty");
                    user_name.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(SignUpEmail).matches())
                {
                    e_mail.setError("Enter the valid email address");
                    e_mail.requestFocus();
                    return;
                }
                if(SignUpPassword.isEmpty())
                {
                    pass_word.setError("Enter the password");
                    pass_word.requestFocus();
                    return;
                }
                if(SignUpPassword.length()<6)
                {
                    pass_word.setError("Length of the password should be more than 6");
                    pass_word.requestFocus();
                    return;
                }
                if(SignUpPassword != ConfirmPassword)
                {
                    confirm_password.setError("Password and Confirm password are different.");
                    confirm_password.requestFocus();
                    return;
                }

            }
        });

    }
}