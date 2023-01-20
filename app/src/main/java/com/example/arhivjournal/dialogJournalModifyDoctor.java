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
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class dialogJournalModifyDoctor extends AppCompatActivity {
    Button btnok,btncancel;
    Spinner sp01;
    ArrayList<String> SpravochnikDocID = new ArrayList<>();
    ArrayList<String> SpravochnikDocNAME = new ArrayList<>();
    final String LOG_TAG = "TAVKA";
    DBHelper dbHelper;
    String  CardIdMody;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_journal_modify_doctor);
        Button btncancel = (Button) findViewById(R.id.buttoncancel);
        btncancel.setOnClickListener(OnClickListenerButtonCancel);

        Button btnsave = (Button) findViewById(R.id.buttonok);
        btnsave.setOnClickListener(OnClickListenerButtonOk);

//получаем даные из активити основного
        Intent intent = getIntent();
        CardIdMody = intent.getStringExtra("cardid");
        String PacientInfoAll = intent.getStringExtra("PacientInfo");
        Log.d(LOG_TAG, "получены данные на редактирование= " + CardIdMody);
        Log.d(LOG_TAG, "получены данные на редактирование2= " + PacientInfoAll);
        TextView TextWiewPacient = findViewById(R.id.p02);
        TextWiewPacient.setText(PacientInfoAll);

        dbHelper = new DBHelper(this);
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //получаем данные по выделенной записи
        Cursor c2;
        String sqlQuery2 = "select idpacient,iddoctor from tableJournals where id="+ String.valueOf(CardIdMody);
        c2 = db.rawQuery(sqlQuery2, null);
        c2.moveToFirst();
        int idDoc= c2.getInt(c2.getColumnIndex("iddoctor"));
        int idPacient= c2.getInt(c2.getColumnIndex("idpacient"));
        c2.close();

        //заполняем  ArrayList  для spinner
        int count_id_doc=0;
        int my_id_doc=0;
        Cursor c;
        String sqlQuery = "select tableDoctors.id as id,fio || ' (' || tableDoctorsAdress.name  ||')' as fullnameinfo from tableDoctors "+
                " left join tableDoctorsAdress on tableDoctorsAdress.id=tableDoctors.department "+
                " order by fio";

        c = db.rawQuery(sqlQuery, null);
        if (c != null) {
            c.moveToFirst();
            //if (c.moveToFirst()) {
            //   String str;
            do {
                SpravochnikDocID.add(c.getString(c.getColumnIndex("id")));
                SpravochnikDocNAME.add(c.getString(c.getColumnIndex("fullnameinfo")));
                if( Integer.parseInt(c.getString(c.getColumnIndex("id")))==(idDoc) ){my_id_doc=count_id_doc;};
                count_id_doc++;
            } while (c.moveToNext());
        }
        // }
        c.close();
        db.close();
        dbHelper.close();


        sp01 = findViewById(R.id.spinnerDoctorsMody);
        // адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, SpravochnikDocNAME);//
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp01.setAdapter(adapter);
        // заголовок
        //sp01.setPrompt("Title");
        // выделяем элемент
        sp01.setSelection(my_id_doc);


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



    View.OnClickListener OnClickListenerButtonCancel = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent1 = new Intent(dialogJournalModifyDoctor.this, MainActivity.class);
            startActivity(intent1);
        }
    };

    View.OnClickListener OnClickListenerButtonOk = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //ArrayList<String> SpravochnikDocID = new ArrayList<>();
            //ArrayList<String> SpravochnikDocNAME = new ArrayList<>();
            int position=sp01.getSelectedItemPosition();
            Log.d(LOG_TAG, "spinner pos="+String.valueOf(position));
            int id_doc=Integer.parseInt(SpravochnikDocID.get(position));
            Log.d(LOG_TAG, "id doctors="+String.valueOf(id_doc));


            // подключаемся к БД
            dbHelper = new DBHelper(dialogJournalModifyDoctor.this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            //current date
            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
            String currentdate=String.valueOf(format.format(new Date()));


            Log.d(LOG_TAG, "currentdate="+currentdate);
            String sqlQuery = "update  tableJournals "
                    + " set iddoctor=" + String.valueOf(id_doc)
                    + " ,dateoperation=\""+currentdate+"\""
                    + " where  id=" + String.valueOf(CardIdMody);
            Log.d(LOG_TAG, "sql="+sqlQuery);
            db.execSQL(sqlQuery);


            //ЗАПОЛНЯЕМ ДАННЫЕ В ЖУРНАЛЕ ОПЕРАЦИЙ!!!!!!!!!!
            format = new SimpleDateFormat("yyyy.MM.dd HH:mm");
            currentdate=String.valueOf(format.format(new Date()));
            ContentValues cv2 = new ContentValues();
        //    cv2.put("idpacient",id_pacient);
            TextView TVp02=findViewById(R.id.p02);
            cv2.put("fiopacient", (String) TVp02.getText());
            cv2.put("iddoctor",id_doc);
            cv2.put("fiodoctor",SpravochnikDocNAME.get(position));
            cv2.put("operation","MODY");
            cv2.put("dateoperation",currentdate);
            cv2.put("user","local");
            // вставляем запись и получаем ее ID
            long rowID2 = db.insert("tableArchives", null, cv2);
            Log.d(LOG_TAG, "row inserted tableArchives, ID = " + rowID2);



            db.close();
            dbHelper.close();
            finish();
            Intent intent1 = new Intent(dialogJournalModifyDoctor.this, MainActivity.class);
            startActivity(intent1);
        }
    };




}