package com.example.fujitsu.solarview;

import android.app.Activity;
import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    EditText email, password;
    Button connexion,forgotten;
    private FirebaseAuth auth;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        auth=FirebaseAuth.getInstance();

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser!=null) {
            updateUI(currentUser);
        }

        setContentView(R.layout.activity_main);

        email = findViewById(R.id.UserID);
        password = findViewById(R.id.password);
        connexion = findViewById(R.id.connexion);
        forgotten = findViewById(R.id.forgotten);

        connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password_txt=password.getText().toString();
                String email_txt=email.getText().toString();
                if (TextUtils.isEmpty(email_txt) || TextUtils.isEmpty(password_txt)){
                    Toast.makeText(MainActivity.this,"Tous les champs sont obligatoires",Toast.LENGTH_LONG);
                }
                else {

                    auth.signInWithEmailAndPassword(email_txt, password_txt)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = auth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }

                            }
                        });
                }
            }
        });
        forgotten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ForgottenActivity.class));
            }
        });
    }

    //Change UI according to user data.
    public void updateUI(FirebaseUser account){

        if(account != null){
            Toast.makeText(this,"connexion reussie",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this,listing.class));
            finish();
        }
        else {
            Toast.makeText(this,"utilisateur inconnu",Toast.LENGTH_LONG).show();
        }

    }

}