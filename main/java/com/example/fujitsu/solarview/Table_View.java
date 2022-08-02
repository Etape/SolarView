package com.example.fujitsu.solarview;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class Table_View extends AppCompatActivity {
    TextView pyroValue,anemoValue,update,humidityValue,air_temperValue,panelTemperValue,thermobatValue,thermoOndValue
            ,TensionPanValue,TensionBatValue,currentbatValue,currentChargeValue,testValue;

    Button register;

    FirebaseDatabase myDB=FirebaseDatabase.getInstance();
    DatabaseReference ref= myDB.getReference("data");

    final data[] newdata = new data[1];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tables);
        register=findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Table_View.this,Tables_act.class);
                startActivity(intent);
            }
        });
    }
}
