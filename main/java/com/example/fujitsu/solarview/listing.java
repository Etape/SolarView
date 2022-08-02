package com.example.fujitsu.solarview;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class listing extends AppCompatActivity {
    TextView pyroValue,anemoValue,update,humidityValue,air_temperValue,panelTemperValue,thermobatValue,thermoOndValue
            ,TensionPanValue,TensionBatValue,currentbatValue,currentChargeValue,testValue;

    Long tsLong;
    double last;
    private MainActivity main;
    Button graphics;
    FirebaseDatabase myDB=FirebaseDatabase.getInstance();
    DatabaseReference ref= myDB.getReference("data");

    int delay=0;
    final data[] newdata = new data[1];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listing);

        graphics = (Button)findViewById(R.id.graphics_button);
        pyroValue=findViewById(R.id.pyro);
        anemoValue=findViewById(R.id.anemo);
        update=findViewById(R.id.update);
        humidityValue=findViewById(R.id.humidity);
        air_temperValue=findViewById(R.id.air_temper);
        panelTemperValue=findViewById(R.id.panel_temper);
        thermobatValue=findViewById(R.id.thermoBat);
        thermoOndValue=findViewById(R.id.thermoOnduleur);
        TensionBatValue=findViewById(R.id.tensionBat);
        TensionPanValue=findViewById(R.id.tensionPan);
        currentbatValue=findViewById(R.id.current_bat);
        currentChargeValue=findViewById(R.id.current_charge);


        testValue=findViewById(R.id.testValue);
        graphics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Tables_act.class));
            }
        });

        tsLong = System.currentTimeMillis()/1000;

        ref.addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {

                newdata[0] = dataSnapshot.getValue(data.class);
                pyroValue.setText("" + newdata[0].getPyro() + "W/m2");
                anemoValue.setText("" + newdata[0].getAnemo() + "m/s");
                String test_type = newdata[0].getTest_type();
                testValue.setText("" + test_type);
                air_temperValue.setText("" + newdata[0].getAir_temper() + "°C");
                humidityValue.setText("" + newdata[0].getHumidity() + "%");
                panelTemperValue.setText("" + newdata[0].getPanel_temper() + "°C");
                currentbatValue.setText("" + newdata[0].getCurrent_battery() + "A");
                currentChargeValue.setText("" + newdata[0].getCurrent_charge() + "A");
                TensionBatValue.setText("" + newdata[0].getTensionBat() + "V");
                TensionPanValue.setText("" + newdata[0].getTensionPan() + "V");
                thermobatValue.setText("" + newdata[0].getThermo1() + "°C");
                thermoOndValue.setText("" + newdata[0].getThermo2() + "°C");
                last = newdata[0].getData_datetime();
               update.setText(getDate((long) last));
            }
            private String getDate(long time) {
                java.util.Calendar cal = java.util.Calendar.getInstance(Locale.ENGLISH);
                cal.setTimeInMillis(time * 1000);
                String date = DateFormat.format("dd-MM-yyyy hh:mm", cal).toString();
                return date;
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
