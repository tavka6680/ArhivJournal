package com.example.arhivjournal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GetListExcelDialog extends AppCompatActivity {
    DBHelper DBHelper;
ArrayList<Integer> ArrayID;
    ListView lv01;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_list_excel_dialog);



         lv01 = findViewById(R.id.ListViewExelGet);


        //Конструктор класса SimpleAdapter имеет следующий вид:
        //SimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to)
        //Для данных в параметре data используется коллекция Map-объектов или его наследников, например, HashMap.
        //Каждый Map содержит данные для отдельного элемента списка.
        //Чтобы адаптер понимал, какие данные нужно вставлять в View-компоненты каждого пункта списка, мы указываем два массива from и to.
        //В массиве from используем ключи из Map, а в массиве to – идентификаторы компонентов.
        // Адаптер последовательно перебирает все компоненты из массива to и сопоставляет им соответствующие значения из from.
        // Следите, чтобы в массиве to было не больше элементов, чем в from, иначе программа вызовет ошибку.

        ArrayList<HashMap<String, String>> arrayListAll = new ArrayList<>();
        HashMap<String, String> map;


        ArrayID = new ArrayList<Integer>();


        DBHelper DBHelperrrr=new DBHelper(this);
        SQLiteDatabase db= DBHelperrrr.getWritableDatabase();
        Cursor c;
        String sql=
        " select tableExcelRequestDay.id as excelid, tableExcelRequestDay.idpacient as idpacient,tableExcelRequestDay.iddoctor "+
                ",tableDoctors.fio as docfio,"+
                " tablePacients.fio || ' (' || tablePacients.birthday || '/' || tableCardTip.name ||')' as pacientfullname "+
                " from tableExcelRequestDay "+
                " left join tableDoctors on tableExcelRequestDay.iddoctor=tableDoctors.id "+
                " left join tablePacients on tableExcelRequestDay.idpacient=tablePacients.id "+
                " left join tableCardTip on tableCardTip.id=tablePacients.numbercard "+
                " where tableExcelRequestDay.saved is not 'TRUE'";
        c=db.rawQuery(sql,null);
        Log.d("TAVKA","c.getCount()="+ String.valueOf(c.getCount()));
        if (c.getCount()>0) {
            c.moveToFirst();
            do {
                Log.d("TAVKA",c.getString(c.getColumnIndex("pacientfullname"))+"----"+c.getString(c.getColumnIndex("docfio")));
                map = new HashMap<>();
                map.put("Pacient", c.getString(c.getColumnIndex("pacientfullname")));
                map.put("Doctor", c.getString(c.getColumnIndex("docfio")));
                arrayListAll.add(map);

                ArrayID.add( c.getInt(c.getColumnIndex("excelid")) );

            } while (c.moveToNext());
        }
        c.close();



        SimpleAdapter adapter = new SimpleAdapter(this, arrayListAll, android.R.layout.simple_list_item_2,
                new String[]{"Pacient", "Doctor"},
                new int[]{android.R.id.text1, android.R.id.text2});
        lv01.setAdapter(adapter);
        //lv01.setSelector( android.R.drawable.alert_dark_frame);
      // lv01.setOnItemClickListener(ListViewOnClick);



        lv01.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
               // Log.d("TAVKA",String.valueOf(i));
               // Log.d("TAVKA",String.valueOf(ArrayID.get(i)));
                // подключаемся к БД
                DBHelper = new DBHelper(GetListExcelDialog.this);
                SQLiteDatabase db = DBHelper.getWritableDatabase();

                Cursor c;
                String sql="select idpacient,iddoctor from tableExcelRequestDay where id="+String.valueOf(ArrayID.get(i));
                c=db.rawQuery(sql,null);
                //if (c.getCount()>0) {
                    c.moveToFirst();
                  //  do {
                    //    Log.d("TAVKA",c.getString(c.getColumnIndex("pacientfullname"))+"----"+c.getString(c.getColumnIndex("docfio")));
                       int id_pacient=c.getInt(c.getColumnIndex("idpacient"));
                       int id_doc=    c.getInt(c.getColumnIndex("iddoctor"));
                    //} while (c.moveToNext());
                //}

                Integer notfound=0;
                CheckBox cb = findViewById(R.id.AddCardNOTFOUNDCheckBox2);
                CheckBox cb2 = findViewById(R.id.AddCardOrderCheckBox2);
                if (cb.isChecked())notfound=1;
                if (cb2.isChecked())notfound=2;
                //current date
                SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
                String currentdate=String.valueOf(format.format(new Date()));
                // создаем объект для данных
                ContentValues cv1 = new ContentValues();
                cv1.put("idpacient",id_pacient);
                cv1.put("iddoctor",id_doc);
                cv1.put("dateoperation",currentdate);
                cv1.put("notfound",notfound);
                // вставляем запись и получаем ее ID
                long rowID1 = db.insert("tableJournals", null, cv1);
               // Log.d("TAVKA", "row inserted tableJournals, ID = " + rowID1);




                // получаем выбранный элемент
               TextView tv1 = (TextView) view.findViewById(android.R.id.text1);
               String selectedItemPacient = (String)tv1.getText();
                TextView tv2 = (TextView) view.findViewById(android.R.id.text2);
                String selectedItemDoc = (String)tv2.getText();

                //TextView textView = (TextView) view;
               // String strText = textView.getText().toString(); // получаем текст нажатого элемента





                format = new SimpleDateFormat("yyyy.MM.dd HH:mm");
                currentdate=String.valueOf(format.format(new Date()));
                ContentValues cv2 = new ContentValues();
                cv2.put("idpacient",id_pacient);
                cv2.put("fiopacient",selectedItemPacient);
                cv2.put("iddoctor",id_doc);
                cv2.put("fiodoctor",selectedItemDoc);
                cv2.put("operation","ADDxls");
                cv2.put("dateoperation",currentdate);
                if (cb.isChecked())cv2.put("user","NOT FOUND");
                if (cb2.isChecked())cv2.put("user","ORDER");

                // вставляем запись и получаем ее ID
                long rowID2 = db.insert("tableArchives", null, cv2);
                Log.d("TAVKA", "row inserted tableArchives, ID = " + rowID2);

                String sqlQuery4 = "update  tableExcelRequestDay "
                        + " set saved='TRUE'"
                        + " where  id=" + String.valueOf(ArrayID.get(i));
                db.execSQL(sqlQuery4);

                db.close();
                DBHelper.close();
                finish();

                Intent intent1 = new Intent(GetListExcelDialog.this, MainActivity.class);
                startActivity(intent1);

                return false;
            }


        });



    };


}