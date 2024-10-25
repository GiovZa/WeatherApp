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

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

 //   FirebaseAuth mAuth;
    TextView txt_SignUp;
    TextInputEditText user_name, pass_word;
    ProgressBar signIn_bar;
    MaterialButton btn_signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user_name=findViewById(R.id.SignInUsername);
        pass_word=findViewById(R.id.SignInPassword);
        signIn_bar=findViewById(R.id.SignInBar);
        btn_signIn=findViewById(R.id.btnSignIn);

        txt_SignUp = findViewById(R.id.txtSignUp);
//        mAuth=FirebaseAuth.getInstance();

        txt_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String SignInUsername = String.valueOf(user_name.getText().toString());
                String SignInPassword= String.valueOf(pass_word.getText().toString().trim());
                if(SignInUsername.isEmpty())
                {
                    user_name.setError("Username is empty");
                    user_name.requestFocus();
                    return;
                }
                if(SignInPassword.isEmpty())
                {
                    pass_word.setError("Password is empty");
                    pass_word.requestFocus();
                    return;
                }
                if(SignInPassword.length()<6)
                {
                    pass_word.setError("Length of password is more than 6");
                    pass_word.requestFocus();
                    return;
                }
            }

        });

    }
}