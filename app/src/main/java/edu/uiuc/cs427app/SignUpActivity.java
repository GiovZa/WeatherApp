package edu.uiuc.cs427app;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.content.Intent;
import android.util.Patterns;
import androidx.annotation.NonNull;
import android.widget.EditText;
import android.content.ServiceConnection;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    TextView txt_SignIn;
    TextInputEditText user_name, e_mail, pass_word, confirm_password;
    ProgressBar signUp_bar;
    MaterialButton btn_signUp;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        user_name=(TextInputEditText) findViewById(R.id.EditEmail);
        e_mail=findViewById(R.id.SignUpEmail);
        pass_word=(TextInputEditText) findViewById(R.id.SignUpPassword);
        confirm_password=findViewById(R.id.ConfirmPassword);
        signUp_bar=findViewById(R.id.SignUpBar);
        btn_signUp=findViewById(R.id.btnSignUp);
        mAuth=FirebaseAuth.getInstance();
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
                String email = user_name.getText().toString().trim();
                String password= pass_word.getText().toString().trim();
                String ConPassword= confirm_password.getText().toString().trim();
                String E_mail= e_mail.getText().toString().trim();
                if(email.isEmpty())
                {
                    user_name.setError("Username is empty");
                    user_name.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(E_mail).matches())
                {
                    e_mail.setError("Enter the valid email address");
                    e_mail.requestFocus();
                    return;
                }
                if(password.isEmpty())
                {
                    pass_word.setError("Enter the password");
                    pass_word.requestFocus();
                    return;
                }
                if(password.length()<6)
                {
                    pass_word.setError("Length of the password should be more than 6");
                    pass_word.requestFocus();
                    return;
                }
                if(!(password.equals(ConPassword)))
                {
                    confirm_password.setError("Password and Confirm password are different.");
                    confirm_password.requestFocus();
                    return;
                }


                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(SignUpActivity.this,"You are successfully SigUp", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(SignUpActivity.this,"SignUp Failed" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }

}

