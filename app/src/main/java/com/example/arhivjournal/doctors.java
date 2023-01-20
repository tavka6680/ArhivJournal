package com.example.arhivjournal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Locale;

public class doctors extends AppCompatActivity {
    final String LOG_TAG = "TAVKA";
    DBHelper dbHelper;
    EditText etFIO,etdepartment;
    int CHOISE_DOCTORS_ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors);
        CHOISE_DOCTORS_ID=0;
        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(this);
        etFIO = (EditText) findViewById(R.id.editTextFIO);
        etdepartment = (EditText) findViewById(R.id.editTextDepartment);
        TableLayout tlDOC= (TableLayout) findViewById(R.id.TableLayoutDOCTORS);
        //Для получения значений строки нужно переместить курсор на желаемую строку результата. Для этого используются методы moveToFirst(), moveToLast(), moveToNext()
        //Каждый из этих методов перемещает курсор на заданную строку, после чего можно прочитать значения этой строки по столбцам. Для этого используются методы-геттеры с индексом столбца в качестве аргумента, например getString(columnIndex: Int).
        tlDOC.setColumnCollapsed(0,true);//скрываем колонку
        tlDOC.setColumnCollapsed(2,true);//скрываем колонку
        tlDOC.setColumnCollapsed(3,true);//скрываем колонку
        tlDOC.setColumnShrinkable(1,true); //перенос слов на след строку
        tlDOC.setColumnShrinkable(4,true);
        tlDOC.setStretchAllColumns(true);
        int colonka=0;
        int stroka=0;
        boolean colorkey=true;
        //временно выводим в ол все данные из таблицы докторов
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sqlQuery = "select tableDoctors.id as id, tableDoctors.fio as fio, tableDoctors.department  as department, tableDoctorsAdress.id as departmentid,tableDoctorsAdress.name as departmentname from tableDoctors left join tableDoctorsAdress on tableDoctors.department=tableDoctorsAdress.id order by fio";
        Cursor c = db.rawQuery(sqlQuery, null);
        if (c != null) {
            if (c.moveToFirst()) {
                String str;
                do {
                    TableRow tr = new TableRow(this);
                    tr.setOnLongClickListener(TableLayoutDOCTORSonLongClickListener);
                    if(colorkey==true)   tr.setBackgroundColor(Color.LTGRAY);
                    else tr.setBackgroundColor(Color.GRAY);
                    str = "";
                    for (String cn : c.getColumnNames()) {
                        str = str.concat(cn + " = " + c.getString(c.getColumnIndex(cn)) + "; ");
                       // Log.d(LOG_TAG,str);
                        TextView textView = new TextView(this);
                        textView.setGravity(Gravity.CENTER_HORIZONTAL);
                        if(colorkey==true)    textView.setBackgroundColor(Color.LTGRAY);
                        else textView.setBackgroundColor(Color.GRAY);
                        textView.setTextColor(Color.BLACK);
                        textView.setText(c.getString(c.getColumnIndex(cn)));
                        //textView.setTextSize(16);
                        textView.setPadding(0,5,10,5);
                        tr.addView(textView, colonka);
                        colonka++;
                    }
                    tlDOC.addView(tr, stroka);
                    stroka++;
                    colonka=0;
                    if(colorkey==true) colorkey=false;
                    else colorkey=true;
                   // Log.d(LOG_TAG, str);
                } while (c.moveToNext());
            }}
        c.close();

      }

View.OnLongClickListener TableLayoutDOCTORSonLongClickListener = new View.OnLongClickListener() {
    @Override
    public boolean onLongClick(View view) {
        //текущую строку и получаем все textview из строки
        TableRow row = (TableRow) view;
        int count = row.getChildCount();

        TextView child = (TextView) row.getChildAt(0);
        String text = child.getText().toString();
        CHOISE_DOCTORS_ID=Integer.valueOf(text);
        TextView child2 = (TextView) row.getChildAt(1);
        etFIO.setText(child2.getText().toString());
       // TextView child3 = (TextView) row.getChildAt(2);
       // etdepartment.setText(child3.getText().toString());
        return false;
    }
}     ;

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



    public void buttonSAVEdoc(View v) {
        int departmen;
        // создаем объект для данных
        ContentValues cv = new ContentValues();
        // получаем данные из полей ввода
        String fio = etFIO.getText().toString();
       // String departmen = etdepartment.getText().toString();

        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //new infodmation for db!!!!!!!!!!!!!!!!!!!
        if (CHOISE_DOCTORS_ID==0) {
            String sqlQuery0 = "select  id from tableDoctorsAdress";
            Cursor c0 = db.rawQuery(sqlQuery0, null);
            if (c0 != null) {
                if (c0.moveToFirst()) {
                    do {
                        for (String cn : c0.getColumnNames()) {
                         departmen=Integer.parseInt   (c0.getString(c0.getColumnIndex(cn)));
                            // подготовим данные для вставки в виде пар: наименование столбца - значение
                            cv.put("fio", fio.toLowerCase());
                            cv.put("department", departmen);
                            // вставляем запись и получаем ее ID
                            long rowID = db.insert("tableDoctors", null, cv);
                         //   Log.d(LOG_TAG, "row inserted, ID = " + rowID);

                        }
                    } while (c0.moveToNext());
                }}
            c0.close();




        }
        else  //mody information in db!!!!!!!!!!!!!!!!
        {
            String sqlQuery = "update  tableDoctors "
                    + " set fio=\"" + fio.toLowerCase()+"\""
        //            + " ,department=\""+departmen+"\""
                    + " where  id=" + String.valueOf(CHOISE_DOCTORS_ID);
            //Log.d(LOG_TAG, "sql="+sqlQuery);
            db.execSQL(sqlQuery);


        }
        // закрываем подключение к БД
        db.close();

        finish();
        dbHelper.close();
        Intent intent1 = new Intent(this, doctors.class);
        startActivity(intent1);

    }

    public  void buttonDELdoc(View v){
        if (CHOISE_DOCTORS_ID>0) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            int delCount = db.delete("tableDoctors", "id = " + String.valueOf(CHOISE_DOCTORS_ID), null);
           // Log.d(LOG_TAG, "deleted rows count = " + delCount);

            finish();
            dbHelper.close();
            Intent intent1 = new Intent(this, doctors.class);
            startActivity(intent1);
        }
    }

    public  void buttonNEWdoc(View v){
        CHOISE_DOCTORS_ID=0;
        etFIO.setText("");
       // etdepartment.setText("");
    }


    public  void buttonFILESdoc(View v){
       // int CHOOSE_FILE_REQUESTCODE = 8777;
       // int PICKFILE_RESULT_CODE = 8778;

        Intent PickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        PickerIntent.setType("*/*");
        startActivityForResult(PickerIntent, 1);

      //  Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
       // chooseFile.setType("*/*");
        //chooseFile = Intent.createChooser(chooseFile, "Choose a file");
       // startActivityForResult(chooseFile, 1);

/*


// проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }
        // получаем путь к SD
        File sdPathTemp = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPathTemp = new File(sdPathTemp.getAbsolutePath() + "/" + "Download");//
        // создаем каталог
        sdPathTemp.mkdirs();
        // формируем объект File, который содержит путь к файлу
        File sdFileTemp = new File(sdPathTemp, "TempArhivJournal");
        try {
           // Log.d(LOG_TAG, "Файл3");
            // открываем поток для записи
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFileTemp));
            // пишем данные
            bw.write("В этот каталог нужно поместить фал VRACHI.txt");
            // закрываем поток
            bw.close();
            Log.d(LOG_TAG, "Файл записан на SD: " + sdFileTemp.getAbsolutePath());
        } catch (IOException e) {
         //   Log.d(LOG_TAG, "Файл4");
            e.printStackTrace();
        }





    // проверяем доступность SD
    if (!Environment.getExternalStorageState().equals(
            Environment.MEDIA_MOUNTED)) {
        Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
        return;
    }
    // получаем путь к SD
    File sdPath = Environment.getExternalStorageDirectory();
    sdPath = new File(sdPath.getAbsolutePath() + "/" + "Download");
    String FILENAME = "123";
    File sdFile = new File(sdPath, FILENAME);
    Log.d(LOG_TAG,sdFile.toString());
    Toast.makeText(this, sdFile.toString(), Toast.LENGTH_LONG).show();

    int departmen;
    ContentValues cv = new ContentValues();
    String sqlzzz = "";
    String FioFromFile = "";
    try {
        DBHelper dbHelperFindDoctors = new DBHelper(this);
        // открываем поток для чтения
        /////////BufferedReader br = new BufferedReader(new InputStreamReader( openFileInput(FILENAME)));
        BufferedReader br = new BufferedReader(new FileReader(sdFile));
        FioFromFile = "";
        while ((FioFromFile = br.readLine().toLowerCase()) != null) {
            Log.d(LOG_TAG,FioFromFile.toString());
            SQLiteDatabase dbFindDoctors = dbHelperFindDoctors.getWritableDatabase();
            sqlzzz = "select id from tableDoctors where fio=\"" + FioFromFile.toString() + "\"";
            Cursor cursorFindDoctors = dbFindDoctors.rawQuery(sqlzzz, null);
            if (cursorFindDoctors.getCount() <= 0) {
                String sqlQuerySPR = "select  id from tableDoctorsAdress";
                SQLiteDatabase dbSPR = dbHelperFindDoctors.getWritableDatabase();
                Cursor CursorSPR = dbSPR.rawQuery(sqlQuerySPR, null);
                if (CursorSPR != null) {
                    if (CursorSPR.moveToFirst()) {
                        do {
                            for (String cn : CursorSPR.getColumnNames()) {
                                departmen = Integer.parseInt(CursorSPR.getString(CursorSPR.getColumnIndex(cn)));
                                cv.put("fio", FioFromFile);
                                cv.put("department", departmen);
                                SQLiteDatabase dbINSERT = dbHelperFindDoctors.getWritableDatabase();
                                long rowID = dbINSERT.insert("tableDoctors", null, cv);
                                dbINSERT.close();
                            }
                        } while (CursorSPR.moveToNext());
                    }
                    CursorSPR.close();
                    dbSPR.close();

                }

            }  // if(curs.getCount()<=0)
            cursorFindDoctors.close();
            dbFindDoctors.close();
            dbHelperFindDoctors.close();
        }

    } catch (FileNotFoundException e) {
        Log.d(LOG_TAG,"FileNotFoundException");
        e.printStackTrace();
    } catch (IOException e) {
        Log.d(LOG_TAG,"FileNotFoundException 2");
        e.printStackTrace();
    }





        finish();
        Intent intent1 = new Intent(this, doctors.class);
        startActivity(intent1);


 */

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        int departmen;
        ContentValues cv = new ContentValues();
        String sql111111 = "";
        String FioFromFile = "";
        DBHelper dbHelperFindDoctors = new DBHelper(this);

        String sql222222 = "select  id from tableDoctorsAdress";
        SQLiteDatabase db222222 = dbHelperFindDoctors.getWritableDatabase();
        Cursor сursor222222 = db222222.rawQuery(sql222222, null);
        SQLiteDatabase db111111 = dbHelperFindDoctors.getWritableDatabase();
        SQLiteDatabase db333333 = dbHelperFindDoctors.getWritableDatabase();
        switch (requestCode) {
            case 1: {
                if (resultCode == RESULT_OK)  {
                    Uri chosenUri = data.getData();
                    String src = chosenUri.getPath();
                    try {
                        InputStream in = getContentResolver().openInputStream(chosenUri);
                        BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    while ((FioFromFile = br.readLine()) != null) {
                        if (FioFromFile.trim().length()>0) {
                            FioFromFile = FioFromFile.toLowerCase();
                            Log.d(LOG_TAG, "from file=" + FioFromFile.toString());
                            ////////////////////////
                            ///////////////////////
                            //SQLiteDatabase db111111 = dbHelperFindDoctors.getWritableDatabase();
                            sql111111 = "select id from tableDoctors where fio=\"" + FioFromFile.toString() + "\"";
                            Cursor cursor111111 = db111111.rawQuery(sql111111, null);
                            if (cursor111111.getCount() <= 0) {
                                if (сursor222222 != null) {
                                    сursor222222.moveToFirst();
                                    if (сursor222222.moveToFirst()) {
                                        do {
                                            for (String cn : сursor222222.getColumnNames()) {
                                                departmen = Integer.parseInt(сursor222222.getString(сursor222222.getColumnIndex(cn)));
                                                cv.put("fio", FioFromFile);
                                                cv.put("department", departmen);

                                                long rowID = db333333.insert("tableDoctors", null, cv);


                                            }
                                        } while (сursor222222.moveToNext());
                                    }

                                }
                            }
                            cursor111111.close();
//                            db111111.close();
                            ////////////////////
                            /////////////////////
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                   e.printStackTrace();
                }
                  //  break;
                }
            }
        }
        db333333.close();
        db111111.close();
        сursor222222.close();
        db222222.close();

        dbHelperFindDoctors.close();
        finish();
        Intent intent1 = new Intent(this, doctors.class);
        startActivity(intent1);
    };

}