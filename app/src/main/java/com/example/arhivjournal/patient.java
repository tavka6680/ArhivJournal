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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class patient extends AppCompatActivity {
    final String LOG_TAG = "TAVKA";
    DBHelper dbHelper;
    EditText etFIO,etNumCard,Etbirthday,etBoxNum;
    Spinner etSpinnerCard;
    ArrayList<String> SpravochnikDCardID = new ArrayList<>();
    ArrayList<String> SpravochnikCardNAME = new ArrayList<>();
    int CHOISE_PACIENTS_ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        Intent intent4 = getIntent();
        String WORDsearch = "";
        WORDsearch=intent4.getStringExtra("SearchWord");
        Log.d(LOG_TAG, "WORDsearch = " + WORDsearch);

        CHOISE_PACIENTS_ID=0;
        etFIO = (EditText) findViewById(R.id.editTextFIO);
        etNumCard = (EditText) findViewById(R.id.editTextCardNum);
        Etbirthday = (EditText) findViewById(R.id.editTextbirthday);
        etBoxNum = (EditText) findViewById(R.id.editTextboxnumber);
        etSpinnerCard = (Spinner) findViewById(R.id.editTextCardNumSpinner);

        TableLayout tableLayoutPacient01 = (TableLayout) findViewById(R.id.TableLayoutPacient);
        tableLayoutPacient01.setColumnCollapsed(0,true);//скрываем колонку
        tableLayoutPacient01.setColumnCollapsed(1,true);//скрываем колонку
        tableLayoutPacient01.setStretchAllColumns(true);
        tableLayoutPacient01.setColumnShrinkable(2,true);
        tableLayoutPacient01.setColumnShrinkable(5,true);
        /*
        tlDOC.setColumnCollapsed(0,true);//скрываем колонку
        tlDOC.setColumnShrinkable(1,true); //перенос слов на след строку
        tlDOC.setColumnShrinkable(2,true);
        tlDOC.setStretchAllColumns(true);
         */
        int colonka=0;
        int stroka=0;
        boolean colorkey=true;
        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(this);
        //временно выводим в ол все данные из таблицы докторов
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();



        //заполняем  ArrayList  для spinner
        int count_id_doc=0;
        int my_id_doc=0;
        Cursor ccard;
        String sqlQueryCard = "select id,name from tableCardTip order by name";
        ccard = db.rawQuery(sqlQueryCard, null);
        if (ccard != null) {
            ccard.moveToFirst();
            //if (c.moveToFirst()) {
            //   String str;
            do {
                SpravochnikDCardID.add(ccard.getString(ccard.getColumnIndex("id")));
                SpravochnikCardNAME.add(ccard.getString(ccard.getColumnIndex("name")));
                //if( Integer.parseInt(ccard.getString(ccard.getColumnIndex("id")))==(idDoc) ){my_id_doc=count_id_doc;};
                count_id_doc++;
            } while (ccard.moveToNext());
        }
        // }
        ccard.close();
        // адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, SpravochnikCardNAME);//
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etSpinnerCard.setAdapter(adapter);
         // выделяем элемент
        //etSpinnerCard.setSelection(my_id_doc);



        String sqlQuery = "select tablePacients.id as id,tablePacients.numbercard,tablePacients.fio,tablePacients.birthday,tablePacients.boxnumber,tableCardTip.name from tablePacients "+
                " left join tableCardTip on tableCardTip.id=tablePacients.numbercard ";
        if (WORDsearch != null) sqlQuery=sqlQuery+" where tablePacients.fio LIKE '%"+WORDsearch+"%'   ";
        sqlQuery=sqlQuery+" order by fio";
        Cursor c = db.rawQuery(sqlQuery, null);
        if (c != null) {
            if (c.moveToFirst()) {
                String str;
                do {
                    TableRow tableRowPacient = new TableRow(this);
                    tableRowPacient.setOnLongClickListener(TableLayoutPACIENTSonLongClickListener);
                    if(colorkey==true)   tableRowPacient.setBackgroundColor(Color.LTGRAY);
                    else tableRowPacient.setBackgroundColor(Color.GRAY);
                    str = "";
                    for (String cn : c.getColumnNames()) {
                        str = str.concat(cn + " = " + c.getString(c.getColumnIndex(cn)) + "; ");
                        TextView textView = new TextView(this);
                        textView.setGravity(Gravity.CENTER_HORIZONTAL);
                        if(colorkey==true)    textView.setBackgroundColor(Color.LTGRAY);
                        else textView.setBackgroundColor(Color.GRAY);
                        textView.setTextColor(Color.BLACK);
                        textView.setText(c.getString(c.getColumnIndex(cn)));
                        //textView.setTextSize(16);
                        textView.setPadding(0,5,10,5);
                        tableRowPacient.addView(textView, colonka);
                        colonka++;
                    }
                    Log.d(LOG_TAG, str);
                    tableLayoutPacient01.addView(tableRowPacient,stroka);
                    stroka++;
                    colonka=0;
                    if(colorkey==true) colorkey=false;
                    else colorkey=true;
                } while (c.moveToNext());
            }}
        c.close();
        Log.d(LOG_TAG, "str");
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



    public void buttonSAVEpacient(View v) {
        int position=etSpinnerCard.getSelectedItemPosition();
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // получаем данные из полей ввода
        String fio = etFIO.getText().toString();
        //String NumCard = etNumCard.getText().toString();
        String NumCard = SpravochnikDCardID.get(position);
        String birthday = Etbirthday.getText().toString();
        String boxnumber = etBoxNum.getText().toString();

        if (CHOISE_PACIENTS_ID==0) {
            // создаем объект для данных
            ContentValues cv = new ContentValues();

            // подготовим данные для вставки в виде пар: наименование столбца - значение
            cv.put("numbercard", NumCard);
            cv.put("fio", fio.toLowerCase());
            cv.put("birthday", birthday);
            cv.put("boxnumber", boxnumber);

            // вставляем запись и получаем ее ID
            long rowID = db.insert("tablePacients", null, cv);
            Log.d(LOG_TAG, "row inserted, ID = " + rowID);
        }
        else
        {
            String sqlQuery = "update  tablePacients "
                    + " set numbercard=\"" + NumCard+"\""
                    + " ,fio=\""+fio.toLowerCase()+"\""
                    + " ,birthday=\""+birthday+"\""
                    + " ,boxnumber=\""+boxnumber+"\""
                    + " where  id=" + String.valueOf(CHOISE_PACIENTS_ID);
            Log.d(LOG_TAG, "sql="+sqlQuery);
            db.execSQL(sqlQuery);
        }
        // закрываем подключение к БД
        db.close();
        dbHelper.close();


        Intent intent1 = new Intent(this, patient.class);
        startActivity(intent1);
     }

     public void buttonNEWpacient(View v){
         CHOISE_PACIENTS_ID=0;
         etFIO.setText("");
         etNumCard.setText("");
         Etbirthday.setText("");
         etBoxNum.setText("");
     }

     public void buttonDELpacient(View v){
         if (CHOISE_PACIENTS_ID>0) {
             SQLiteDatabase db = dbHelper.getWritableDatabase();
             int delCount = db.delete("tablePacients", "id = " + String.valueOf(CHOISE_PACIENTS_ID), null);
             Log.d(LOG_TAG, "deleted rows count = " + delCount);
             finish();
             dbHelper.close();
             Intent intent1 = new Intent(this, patient.class);
             startActivity(intent1);
         }
     }

    public void buttonFILEpacient(View v){
        Intent intent4 = new Intent(this, patient.class);
        EditText et = (EditText) findViewById(R.id.editTextFIO);
        String text = et.getText().toString().trim();
        if (text.length()>0) {
            intent4.putExtra("SearchWord", text);
        }
        startActivity(intent4);

    }



    View.OnLongClickListener TableLayoutPACIENTSonLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            //текущую строку и получаем все textview из строки
            TableRow row = (TableRow) view;
            int count = row.getChildCount();

            TextView child = (TextView) row.getChildAt(0);
            String text = child.getText().toString();
            CHOISE_PACIENTS_ID=Integer.valueOf(text);

            TextView child2 = (TextView) row.getChildAt(1);
            etNumCard.setText(child2.getText().toString());
            Integer search = Integer.parseInt(child2.getText().toString());
            for (int i = 0; i < SpravochnikDCardID.size(); i++) {
                if (Integer.parseInt(SpravochnikDCardID.get(i))==search ) {
                //Log.d(LOG_TAG, SpravochnikDCardID.get(i).toString());
                    etSpinnerCard.setSelection(i);
                }
            }

            TextView child3 = (TextView) row.getChildAt(2);
            etFIO.setText(child3.getText().toString());
            TextView child4 = (TextView) row.getChildAt(3);
            Etbirthday.setText(child4.getText().toString());
            TextView child5 = (TextView) row.getChildAt(4);
            etBoxNum.setText(child5.getText().toString());
            return false;
        }
    };
}