package com.example.arhivjournal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class dialog_journal_add extends AppCompatActivity {
    Spinner spinnerDoc,spinnerPacient;
    final String LOG_TAG = "TAVKA";
    DBHelper dbHelper;
    ArrayList<String> SpravochnikDocIDadd = new ArrayList<>();
    ArrayList<String> SpravochnikDocNAMEadd = new ArrayList<>();
    ArrayList<String> SpravochnikPacientIDadd = new ArrayList<>();
    ArrayList<String> SpravochnikPacientNAMEadd = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_journal_add);
        spinnerDoc = findViewById(R.id.spinnerDoctorsAdd);
        spinnerPacient = findViewById(R.id.spinnerPacientAdd);
        Button btncancel = (Button) findViewById(R.id.buttoncancelADD);
        btncancel.setOnClickListener(OnClickListenerButtonCancelADD);
        Button btnsave = (Button) findViewById(R.id.buttonokADD);
        btnsave.setOnClickListener(OnClickListenerButtonOkADD);


        // подключаемся к БД
        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        //заполняем  ArrayList  для spinner
        Cursor c;
        String sqlQuery = "select tableDoctors.id as id,fio || ' (' || tableDoctorsAdress.name  ||')' as fullnameinfo from tableDoctors "+
                " left join tableDoctorsAdress on tableDoctorsAdress.id=tableDoctors.department "+
                " order by fio";
        Log.d(LOG_TAG, "sql="+sqlQuery);
        c = db.rawQuery(sqlQuery, null);
        if (c != null) {
            c.moveToFirst();
            do {
                SpravochnikDocIDadd.add(c.getString(c.getColumnIndex("id")));
                SpravochnikDocNAMEadd.add(c.getString(c.getColumnIndex("fullnameinfo")));
            } while (c.moveToNext());
        }
        Log.d(LOG_TAG, "count="+String.valueOf(c.getCount()));
        c.close();


        //заполняем  ArrayList  для spinner
        Cursor c2;
        String sqlQuery2 = "select tablePacients.id as id ,tablePacients.fio || ' (' || tablePacients.birthday ||'/'|| tableCardTip.name ||')' as fullnameinfo from tablePacients "+
                " left join tableCardTip on tableCardTip.id=tablePacients.numbercard "+
                " order by fio";
        //birthday
        //numbercard
        Log.d(LOG_TAG, "sql="+sqlQuery2);
        c2 = db.rawQuery(sqlQuery2, null);
        if (c2 != null) {
            c2.moveToFirst();
            do {
                SpravochnikPacientIDadd.add(c2.getString(c2.getColumnIndex("id")));
                SpravochnikPacientNAMEadd.add(c2.getString(c2.getColumnIndex("fullnameinfo")));
                Log.d(LOG_TAG, c2.getString(c2.getColumnIndex("fullnameinfo")));
            } while (c2.moveToNext());
        }
        Log.d(LOG_TAG, "count="+String.valueOf(c2.getCount()));
        c2.close();
        db.close();
        dbHelper.close();

       // адаптер
       ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, SpravochnikDocNAMEadd);//
       adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDoc.setAdapter(adapter);

        // адаптер
       ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, SpravochnikPacientNAMEadd);//
       adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       spinnerPacient.setAdapter(adapter2);

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1,0,0,"Выданные карты");
        menu.add(2,0,0,"Пациенты");
        menu.add(3,0,0,"Врачи");
        menu.add(4,0,0,"Журнал операций");
        menu.add(5,0,0,"Справчник карт");
        menu.add(6,0,0,"Справчник офисов");
        menu.add(7,0,0,"Загрузка данных на день (excel)");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Log.d(LOG_TAG, String.valueOf(item.getGroupId()));
        // Log.d(LOG_TAG, String.valueOf(item.getItemId()));
        //Log.d(LOG_TAG, String.valueOf(item.getOrder()));
        switch(item.getGroupId()){
            case 7:///dayexcel
                Intent intent7 = new Intent(this, zagruzka_day_excel_test2.class);
                startActivity(intent7);
                break;
            case 6:///office
                Intent intent6 = new Intent(this, Spravochniki.class);
                //intent6.putExtra("spravochnik","office");
                intent6.putExtra("spravochnik","1");
                startActivity(intent6);
                break;
            case 5://cards
                Intent intent5 = new Intent(this, Spravochniki.class);
                //intent5.putExtra("spravochnik","cards");
                intent5.putExtra("spravochnik","2");
                startActivity(intent5);
                break;
            case 4:
                Intent intent4 = new Intent(this, journal.class);
                startActivity(intent4);
                break;
            case 3:
                Intent intent3 = new Intent(this, doctors.class);
                startActivity(intent3);
                break;
            case 2:
                Intent intent2 = new Intent(this, patient.class);
                startActivity(intent2);
                break;
            case 1:
                Intent intent1 = new Intent(this, MainActivity.class);
                startActivity(intent1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }



View.OnClickListener OnClickListenerButtonCancelADD = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
       Intent  intent1 = new Intent(dialog_journal_add.this, MainActivity.class);
       startActivity(intent1);
    }
};

View.OnClickListener  OnClickListenerButtonOkADD= new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        int position=spinnerDoc.getSelectedItemPosition();
        int id_doc=Integer.parseInt(SpravochnikDocIDadd.get(position));
        int position2=spinnerPacient.getSelectedItemPosition();
        int id_pacient=Integer.parseInt(SpravochnikPacientIDadd.get(position2));


        // подключаемся к БД
        dbHelper = new DBHelper(dialog_journal_add.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //current date
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        String currentdate=String.valueOf(format.format(new Date()));

        Integer notfound=0;
        CheckBox cb = findViewById(R.id.AddCardNOTFOUNDCheckBox);
        CheckBox cb2 = findViewById(R.id.AddCardOrderCheckBox);
        if (cb.isChecked())notfound=1;
        if (cb2.isChecked())notfound=2;
        // создаем объект для данных
        ContentValues cv1 = new ContentValues();
        cv1.put("idpacient",id_pacient);
        cv1.put("iddoctor",id_doc);
        cv1.put("dateoperation",currentdate);
        cv1.put("notfound",notfound);
        // вставляем запись и получаем ее ID
        long rowID1 = db.insert("tableJournals", null, cv1);
        Log.d(LOG_TAG, "row inserted tableJournals, ID = " + rowID1);

        format = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        currentdate=String.valueOf(format.format(new Date()));
        ContentValues cv2 = new ContentValues();
        cv2.put("idpacient",id_pacient);
        cv2.put("fiopacient",SpravochnikPacientNAMEadd.get(position2));
        cv2.put("iddoctor",id_doc);
        cv2.put("fiodoctor",SpravochnikDocNAMEadd.get(position));
        cv2.put("operation","ADD");
        cv2.put("dateoperation",currentdate);
        //cv2.put("user","local");
        if (cb.isChecked())cv2.put("user","NOT FOUND");
        if (cb2.isChecked())cv2.put("user","ORDER");
        // вставляем запись и получаем ее ID
        long rowID2 = db.insert("tableArchives", null, cv2);
        Log.d(LOG_TAG, "row inserted tableArchives, ID = " + rowID2);


        db.close();
        dbHelper.close();
        finish();

        Intent  intent1 = new Intent(dialog_journal_add.this, MainActivity.class);
        startActivity(intent1);
    }
};



}
