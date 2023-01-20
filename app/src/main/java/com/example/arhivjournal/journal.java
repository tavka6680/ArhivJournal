package com.example.arhivjournal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class journal extends AppCompatActivity {
    DBHelper dbHelper;
    final String LOG_TAG = "TAVKA";
    TableLayout tl1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

        Intent intent4 = getIntent();
        String WORDsearch = "";
        WORDsearch=intent4.getStringExtra("SearchWord");
            Log.d(LOG_TAG, "WORDsearch = " + WORDsearch);

/*
        dbHelper = new DBHelper(this);
        // создаем объект для данных
        ContentValues cv = new ContentValues();
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // подготовим данные для вставки в виде пар: наименование столбца - значение
        cv.put("idpacient", 1);
        cv.put("iddoctor", 1);
        cv.put("dateoperation", "2022.08.14");
        // вставляем запись и получаем ее ID
        long rowID = db.insert("tableJournals", null, cv);
        Log.d(LOG_TAG, "row inserted, ID = " + rowID);
        // закрываем подключение к БД
        dbHelper.close();

 */

        tl1=(TableLayout)findViewById(R.id.TableLayout01JOURNAL);
        tl1.setColumnShrinkable(0,true); //перенос слов на след строку
        tl1.setColumnShrinkable(1,true);

       // tl1.setColumnShrinkable(2,true);
        //tl1.setColumnShrinkable(3,true);
       // tl1.setColumnShrinkable(4,true);
        //tl1.setColumnShrinkable(5,true);
        tl1.setStretchAllColumns(true);
        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(this);
        //временно выводим в ол все данные из таблицы докторов
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sqlQuery = "select  fiopacient,  fiodoctor, operation, dateoperation, user from tableArchives order by dateoperation DESC";
        if (WORDsearch != null) {

             sqlQuery = "select  fiopacient,  fiodoctor, operation, dateoperation, user from tableArchives "+
                    " where fiopacient LIKE '%"+WORDsearch+"%' OR "+
                    " fiodoctor LIKE '%"+WORDsearch+"%'  OR "+
                    " operation LIKE '%"+WORDsearch+"%'  OR "+
                    " dateoperation LIKE '%"+WORDsearch+"%'  OR "+
                    " user LIKE '%"+WORDsearch+"%' "+
                                                           " order by dateoperation DESC";





        };



        Cursor c = db.rawQuery(sqlQuery, null);
        Log.d(LOG_TAG, "cuesor size = " + String.valueOf(c.getCount()));
        Boolean colorkey=false;
        int colonka=0;
        int stroka=0;
        if (c != null) {
            if (c.moveToFirst()) {
                String str;
                do {
                    TableRow tableRow = new TableRow(this);
                    if(colorkey==true)   tableRow.setBackgroundColor(Color.LTGRAY);
                    else tableRow.setBackgroundColor(Color.GRAY);
                    str = "";
                    for (String cn : c.getColumnNames()) {

                        TextView textView = new TextView(this);
                        textView.setGravity(Gravity.CENTER_HORIZONTAL);
                        if(colorkey==true)    textView.setBackgroundColor(Color.LTGRAY);
                       else textView.setBackgroundColor(Color.GRAY);
                       textView.setTextColor(Color.BLACK);
                        textView.setText(c.getString(c.getColumnIndex(cn)));
                    //    Log.d(LOG_TAG,  c.getString(c.getColumnIndex(cn)));
                       // textView.setTextSize(16);
                        //textView.setWidth(150);
                       // textView.setMinWidth(150);
                        textView.setPadding(0,5,10,5);
                        tableRow.addView(textView, colonka);
                        colonka++;
                    }
                    tl1.addView(tableRow, stroka);
                    stroka++;
                    colonka=0;
                    if(colorkey==true) colorkey=false;
                    else colorkey=true;
                } while (c.moveToNext());
            }}
        c.close();
        dbHelper.close();

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


    public  void ButtonOnClickJournalSearch(View v) {
        Intent intent4 = new Intent(this, journal.class);
        EditText et = (EditText) findViewById(R.id.editTextWORD);
        String text = et.getText().toString().trim();
        if (text.length()>0) {
            intent4.putExtra("SearchWord", text);
        }
        startActivity(intent4);

    };

}