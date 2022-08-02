package com.example.fujitsu.solarview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class Tables_act extends AppCompatActivity {
    Button  register,file;
    TableLayout tableAmbiance,tableEquipement,tableProduction;
    FirebaseDatabase myDB=FirebaseDatabase.getInstance();
    DatabaseReference ref= (DatabaseReference) myDB.getReference("data");
    ArrayList<data> datas = new ArrayList<>(20);
    data[] newdata = new data[1];

    protected listing  MainActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tables);
        tableAmbiance=findViewById(R.id.ambiance);
        tableEquipement=findViewById(R.id.tempers);
        tableProduction=findViewById(R.id.production);
        register=findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Tables_act.this,Register.class));
            }
        });
        file=findViewById(R.id.file);
        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Tables_act.this,FileDownload.class));
            }
        });

        // Read from the database
        ref.limitToLast(20).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    newdata[0] = postSnapshot.getValue(data.class);
                    datas.add(newdata[0]);
                }
                for (int i=0;i<datas.size();i++) {
                        data Data = datas.get(i);
                        if (tableAmbiance.getChildCount()>20){
                            tableAmbiance.removeViewAt(0);
                            tableEquipement.removeViewAt(0);
                            tableProduction.removeViewAt(0);
                        }
                        Context context=getApplicationContext();
                        newrow(tableAmbiance, getDate((long) Data.getData_datetime()), Data.getPyro(), Data.getAnemo(), Data.getHumidity(), Data.getAir_temper(),context);
                        newrow(tableEquipement, getDate((long) Data.getData_datetime()), Data.getPanel_temper(), Data.getThermo1(), Data.getThermo2(),context);
                        newrow(tableProduction, getDate((long) Data.getData_datetime()), Data.getCurrent_battery(), Data.getTensionBat(), Data.getCurrent_charge(), Data.getTensionPan(),context);

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
               }
        });
    }
    private String getDate(long time) {
        java.util.Calendar cal = java.util.Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("dd-MM-yyyy hh:mm", cal).toString();
        return date;
    }

    protected void newrow(TableLayout table, String date, float element2, float element3, float element4, float element5, Context context){
        TableRow row=  new TableRow(context)  ;
        TextView col1=new TextView(context),col2=new TextView(context),
                col3=new TextView(context) , col4=new TextView(context) ,
                col5=new TextView(context) ;

        col1.setText(date);col1.setPadding(8,0,0,8);col1.setLayoutParams(new TableRow.LayoutParams(1));col1.setGravity(Gravity.CENTER);
        col2.setText(""+element2);col2.setPadding(8,0,0,8);col2.setLayoutParams(new TableRow.LayoutParams(2));col2.setGravity(Gravity.CENTER);
        col3.setText(""+element3);col3.setPadding(8,0,0,8);col3.setLayoutParams(new TableRow.LayoutParams(3));col3.setGravity(Gravity.CENTER);
        col4.setText(""+element4);col4.setPadding(8,0,0,8);col4.setLayoutParams(new TableRow.LayoutParams(4));col4.setGravity(Gravity.CENTER);
        col5.setText(""+element5);col5.setPadding(8,0,0,8);col5.setLayoutParams(new TableRow.LayoutParams(5));col5.setGravity(Gravity.CENTER);
        row.addView(col1);
        row.addView(col2);
        row.addView(col3);
        row.addView(col4);
        row.addView(col5);
        table.addView(row);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    protected void newrow(TableLayout table, String date, float element2, float element3, float element4, Context context){
        TableRow row = new TableRow(context);
        TextView col1 = new TextView(context),col2 = new TextView(context),col3 = new TextView(context),col4 = new TextView(context);
        col1.setText(date);col1.setPadding(8,0,0,8);col1.setLayoutParams(new TableRow.LayoutParams(1));col1.setGravity(Gravity.CENTER);
        col2.setText(""+element2);col2.setPadding(8,0,0,8);col2.setLayoutParams(new TableRow.LayoutParams(2));col2.setGravity(Gravity.CENTER);
        col3.setText(""+element3);col3.setPadding(8,0,0,8);col3.setLayoutParams(new TableRow.LayoutParams(3));col3.setGravity(Gravity.CENTER);
        col4.setText(""+element4);col4.setPadding(8,0,0,8);col4.setLayoutParams(new TableRow.LayoutParams(4));col4.setGravity(Gravity.CENTER);
        row.addView(col1);
        row.addView(col2);
        row.addView(col3);
        row.addView(col4);
        table.addView(row);
    }
}
