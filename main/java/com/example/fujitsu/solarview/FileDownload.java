package com.example.fujitsu.solarview;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.widget.Toast.LENGTH_LONG;

public class FileDownload extends AppCompatActivity {
    Button  download,openFile;
    TextView  warning;
    EditText  First,Last;
    private static final String TAG="FileDownload";

    FirebaseDatabase myDB=FirebaseDatabase.getInstance();
    DatabaseReference ref= (DatabaseReference) myDB.getReference("data");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        download=findViewById(R.id.download);
        warning=findViewById(R.id.warning);
        First=findViewById(R.id.Fisrt);
        Last=findViewById(R.id.Last);
        openFile=findViewById(R.id.openFile);
        final java.io.File chemin = getApplication().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        final File[] file = {new File(chemin, "empty.csv")};
        openFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent=new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(FileProvider.getUriForFile(FileDownload.this,BuildConfig.APPLICATION_ID+".provider",file[0]),"text/csv");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                }
                catch (ActivityNotFoundException e){
                    //  no Activity to hAndle this kind of  file
                    }
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_First=First.getText().toString();
                String txt_Last=Last.getText().toString();

                if(TextUtils.isEmpty(txt_First)||TextUtils.isEmpty(txt_Last)){
                    Toast.makeText(FileDownload.this,"Tous les champs sont obligatoires", LENGTH_LONG);

                    warning.setText("saisie incorrecte");
                    warning.setVisibility(View.VISIBLE);
                }
                else{
                    try {
                        DateFormat formatter=new SimpleDateFormat("dd/MM/yyyy");
                        final DateFormat formatterTime=new SimpleDateFormat("dd_MM_yyyy_hh_mm");
                        Date dateFirst = (Date) formatter.parse(txt_First);
                        Date dateLast = (Date) formatter.parse(txt_Last);

                        Calendar  calendar=Calendar.getInstance();
                        final String date=formatterTime.format(calendar.getTime())+".csv";

                        file[0] = new File(chemin,date);

                        long timeLast=dateLast.getTime()/1000;
                        long timeFirst=dateFirst.getTime()/1000;
                        ref.orderByChild("data_datetime").startAt(timeFirst).endAt(timeLast).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String Data = "", Header = "";

                                Header = "Date;type de Test;Ensoleillement;Vitesse du vent;Temperature de l'air" +
                                        ";Temperature du panneau;Temperature Onduleur;Temperature Batteries;" +
                                        "Courant batteries;Courant Charge;Tension Panneau;Tension Batterie";
                                writeToFile(Header, file[0]);
                                int i = 0;
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    i++;
                                    data[] newdata = new data[1];
                                    newdata[0] = postSnapshot.getValue(data.class);
                                    String dataDate = getDate((long) newdata[0].getData_datetime());

                                    Data = dataDate + ";" + newdata[0].getTest_type() + ";" + newdata[0].getPyro()
                                            + ";" + newdata[0].getAnemo() + ";" + newdata[0].getAir_temper()
                                            + ";" + newdata[0].getPanel_temper() + ";" + newdata[0].getThermo1()
                                            + ";" + newdata[0].getThermo2() + ";" + newdata[0].getCurrent_battery()
                                            + ";" + newdata[0].getCurrent_charge() + ";" + newdata[0].getTensionPan()
                                            + ";" + newdata[0].getTensionBat();
                                    Log.d(TAG,Data);
                                    writeToFile(Data, file[0]);
                                }
                                if (i == 0) {
                                    Log.d(TAG,"data not found");
                                    warning.setText("aucune donnee trouvee ");
                                    warning.setVisibility(View.VISIBLE);
                                }
                                else {
                                    warning.setText("Votre fichier est disponible au nom de:" + date);
                                    warning.setVisibility(View.VISIBLE);
                                    openFile.setText("Ouvrir le fichier "+date);
                                    //openFile.setVisibility(View.VISIBLE);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    catch (java.text.ParseException e){
                        //  TODO Auto-generated catch block
                        e.printStackTrace();
                        Log.d(TAG,"saisie incorrecte");
                        warning.setText("saisie incorrecte");
                        warning.setVisibility(View.VISIBLE);
                    }
                    }
            }
        });
    }

    private String getDate(long time) {
        java.util.Calendar cal = java.util.Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String dated = android.text.format.DateFormat.format("dd-MM-yyyy hh:mm", cal).toString();
        return dated;
    }

    private void writeToFile(String data,File file) {
        try {
            PrintWriter outputStreamWriter = new PrintWriter(new FileWriter(file,true));
            outputStreamWriter.print(data);
            outputStreamWriter.print("\r\n");
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "FileDownload write failed: " + e.toString());
        }
    }
}
