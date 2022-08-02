package com.example.fujitsu.solarview;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {
        FirebaseAuth auth;
        EditText Email;
        Button RegisterButton;
        String Password;
        TextView verification,passwordcreated;

    private static final String TAG = Register.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth=FirebaseAuth.getInstance();
        setContentView(R.layout.activity_register);

        Email = findViewById(R.id.RegisterEmail);
        RegisterButton = findViewById(R.id.RegisterButton);
        verification =findViewById(R.id.register_verification);
        passwordcreated= findViewById(R.id.passwordForgotten);
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email=Email.getText().toString();
                if (TextUtils.isEmpty(txt_email)){
                    Toast.makeText(Register.this,"Tous les champs sont obligatoires",Toast.LENGTH_LONG);

                    verification.setText("saisie incorrecte");
                    verification.setVisibility(View.VISIBLE);
                }
                else{
                Password=calculate_password();
                register(txt_email,Password);

                verification.setText("enregistrement terminé avec succes");
                verification.setVisibility(View.VISIBLE);
                    passwordcreated.setText("le mot de passe est: "+Password);
                    passwordcreated.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    public void register(String email, String password){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
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
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    //Change UI according to user data.
    public void updateUI(FirebaseUser account){
        if(account != null){
            startActivity(new Intent(this,Tables_act.class));
        }
    }
    public String calculate_password (){
        char table[]={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f','g','h'};
        int i;
        int max=17;
        int min=0;
        int range=max-min+1;
        String password="";
        for(i=0;i<6;i++){
            int random=  (int)(Math.random()*range)-min;
            password+=table[random];
        }
        return password;
    }
}
