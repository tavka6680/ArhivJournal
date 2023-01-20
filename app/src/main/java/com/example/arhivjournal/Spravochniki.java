package com.example.arhivjournal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

public class Spravochniki extends AppCompatActivity {
    DBHelper dbHelper;
    final String LOG_TAG = "TAVKA";
    EditText ETname, ETid;
    Integer TIP_SPRAVOCHNIKA;



    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spravochniki);
        Intent intent = getIntent();
        TIP_SPRAVOCHNIKA = Integer.parseInt(intent.getStringExtra("spravochnik"));
        //cards
        //office
        ETname = (EditText) findViewById(R.id.editTextNAMEspr);
        ETid = (EditText) findViewById(R.id.editTextIDspr);
        Log.d(LOG_TAG,"-"+TIP_SPRAVOCHNIKA+"-");
        String sqlQuery = "";
        if (TIP_SPRAVOCHNIKA == 1) {//"office"
            sqlQuery = "select id,name from tableDoctorsAdress order by name";
            Log.d(LOG_TAG,"sqlQuery1");
        }
        if (TIP_SPRAVOCHNIKA == 2) {//2
            sqlQuery = "select id,name from tableCardTip order by name";
            Log.d(LOG_TAG,"sqlQuery2");
        }
        TableLayout tlDOC = (TableLayout) findViewById(R.id.TableLayoutspravochnik);
        //tlDOC.setColumnCollapsed(0, true);//скрываем колонку
        //tlDOC.setStretchAllColumns(true);
        int colonka = 0;
        int stroka = 0;
        boolean colorkey = true;
        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

       // Log.d(LOG_TAG,sqlQuery);
        Cursor c = db.rawQuery(sqlQuery, null);
        if (c != null) {
            if (c.moveToFirst()) {
                String str;
                do {
                    TableRow tr = new TableRow(this);
                    tr.setOnLongClickListener(TableLayoutSPRAVOCHNIKISonLongClickListener);
                    if (colorkey == true) tr.setBackgroundColor(Color.LTGRAY);
                    else tr.setBackgroundColor(Color.GRAY);
                    str = "";
                    for (String cn : c.getColumnNames()) {
                       // str = str.concat(cn + " = " + c.getString(c.getColumnIndex(cn)) + "; ");
                        //Log.d(LOG_TAG,str);
                        TextView textView = new TextView(this);
                        textView.setGravity(Gravity.CENTER_HORIZONTAL);
                        if (colorkey == true) textView.setBackgroundColor(Color.LTGRAY);
                        else textView.setBackgroundColor(Color.GRAY);
                        textView.setTextColor(Color.BLACK);
                        textView.setText(c.getString(c.getColumnIndex(cn)));
                        textView.setPadding(0, 5, 10, 5);
                        tr.addView(textView, colonka);
                        colonka++;
                    }
                    tlDOC.addView(tr, stroka);
                    stroka++;
                    colonka = 0;
                    if (colorkey == true) colorkey = false;
                    else colorkey = true;
                   // Log.d(LOG_TAG, str);
                } while (c.moveToNext());
            }
        }
        c.close();


    }


    View.OnLongClickListener TableLayoutSPRAVOCHNIKISonLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            //текущую строку и получаем все textview из строки
            TableRow row = (TableRow) view;
            int count = row.getChildCount();

            TextView child = (TextView) row.getChildAt(0);
            ETid.setText(child.getText().toString());
            //Log.d(LOG_TAG, child.getText().toString());
            TextView child2 = (TextView) row.getChildAt(1);
            ETname.setText(child2.getText().toString());
            //Log.d(LOG_TAG, child2.getText().toString());
            return false;
        }
    };


    public void buttonNEWspravochnik(View v) {
        ETname.setText("");
        ETid.setText("");
    }



    public void buttonSAVEspravochnik(View v) {
        if (ETname.getText().toString().trim().length()==0 ) return;

        if (TIP_SPRAVOCHNIKA == 1) {
            if (ETid.getText().toString().trim().length() == 0) {  //add
                Log.d(LOG_TAG, "add tableDoctorsAdress");
                ContentValues cv = new ContentValues();
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                cv.put("name", String.valueOf(ETname.getText().toString()));
                long rowID = db.insert("tableDoctorsAdress", null, cv);
                Log.d(LOG_TAG, "row inserted, ID = " + rowID);
                db.close();
            }
            if (ETid.getText().toString().trim().length() != 0) {  //mody
                Log.d(LOG_TAG, "mody tableDoctorsAdress");
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String sqlQuery = "update  tableDoctorsAdress "
                        + " set name=\"" + ETname.getText().toString() +"\""
                        + " where  id=" + ETid.getText().toString();

                db.execSQL(sqlQuery);
                db.close();
            }
            finish();
            dbHelper.close();
            Intent intent6 = new Intent(this, Spravochniki.class);
            intent6.putExtra("spravochnik","1");  //cards
            startActivity(intent6);
        }


        if (TIP_SPRAVOCHNIKA == 2) {
            if (ETid.getText().toString().trim().length() == 0) {  //add
                ContentValues cv = new ContentValues();
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                cv.put("name", ETname.getText().toString());
                long rowID = db.insert("tableCardTip", null, cv);
                Log.d(LOG_TAG, "row inserted, ID = " + rowID);
                db.close();
            }
            if (ETid.getText().toString().trim().length()!= 0) {  //mody
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String sqlQuery = "update  tableCardTip "
                        + " set name=\"" + ETname.getText().toString() +"\""
                        + " where  id=" + ETid.getText().toString();
                Log.d(LOG_TAG, "sql= " + sqlQuery);
                db.execSQL(sqlQuery);
                db.close();
            }
            finish();
            dbHelper.close();
            Intent intent6 = new Intent(this, Spravochniki.class);
            intent6.putExtra("spravochnik","2");  //
            startActivity(intent6);
        }

    }




    public void buttonDELspravochnik(View v) {
        if (ETname.getText().toString().trim().length()==0 ) return;

        if (TIP_SPRAVOCHNIKA == 1) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            int delCount = db.delete("tableDoctorsAdress", "id = " + ETid.getText(), null);////tableCardTip
            Log.d(LOG_TAG, "deleted rows count = " + delCount);

            finish();
            dbHelper.close();
            Intent intent6 = new Intent(this, Spravochniki.class);
            intent6.putExtra("spravochnik","1");  //cards
            startActivity(intent6);
        }
        if (TIP_SPRAVOCHNIKA == 2) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            int delCount = db.delete("tableCardTip", "id = " + ETid.getText(), null);////
            Log.d(LOG_TAG, "deleted rows count = " + delCount);

            finish();
            dbHelper.close();
            Intent intent6 = new Intent(this, Spravochniki.class);
            intent6.putExtra("spravochnik","2");  //
            startActivity(intent6);
        }


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


}