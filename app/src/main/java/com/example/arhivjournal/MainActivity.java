package com.example.arhivjournal;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    final String LOG_TAG = "TAVKA";
    DBHelper dbHelper;
    TableLayout tl1;
  //  ScrollView aa;
  //  LinearLayout bb;
    int DIALOG_CHOISE = 1;
//    int DIALOG_MODIFY = 2;
 //   int DIALOG_ADD=3;
    int CARD_CHOISE_ID;
    String CARD_CHOISE_PACIENT_INFO;
    String CARD_CHOISE_DOC_INFO;
    SharedPreferences sPref;
    public static String sortTable="";
    public long startTime1 = System.currentTimeMillis();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);






        Intent intent12345 = getIntent();
        String sortTable=intent12345.getStringExtra( "sortirovka");
        try {
            if(sortTable.length()==0)  sortTable="1";
        } catch (Exception e) {
            sortTable="1";
            Log.d("TAVKA","sortTable03ERROR");
        }

        Log.d("TAVKA","sortTable03="+sortTable);





/*
        sPref = getSharedPreferences("PrefArhivJournal", MODE_PRIVATE);
        String savedText = sPref.getString("DoctorsAdress01", "");
        Log.d(LOG_TAG, "pref="+savedText);
        String savedText = sPref.getString("DoctorsAdress02", "");
        Log.d(LOG_TAG, "pref="+savedText);
        if (savedText.length()==0)
        {
            SharedPreferences.Editor ed = sPref.edit();
            ed.putString("DoctorsAdress01", "коломенская");
            ed.commit();
            ed.putString("DoctorsAdress02", "взлетная");
            ed.commit();
        }

 */

/*
        db.execSQL("create table tableDoctorsAdress ("
                + "id integer primary key autoincrement,"
                + "name text"
                + ");");

        db.execSQL("create table tableCardTip ("
                + "id integer primary key autoincrement,"
                + "name text"
                + ");");

*/
/*
        aa=findViewById(R.id.Scroll01);
        aa.setOnLongClickListener(Scroll01nLongClickListener);
        bb=findViewById(R.id.MainAct);
        bb.setOnLongClickListener(Scroll01nLongClickListener);
*/

        tl1=(TableLayout)findViewById(R.id.TableLayout01);
    /*
        for (int i = 0; i < 5; i++) {                        //строки  strok
            TableRow tableRow = new TableRow(this);
            //tableRow.setPadding(0, 0, 0, 2);
            tableRow.setId(i); // here you can set unique id to TableRow for      // identification
            //if (i>0){tableRow.setId(Integer.parseInt(tmp01.get(a)));};
            //tableRow.setOnClickListener(TableLayout01OnClickListener); // set TableRow onClickListner
            //tableRow.setOnLongClickListener(TableLayout01OnLongClickListener);
            for (int j = 0; j < 4; j++) {                     //столбцы  column
                TextView textView = new TextView(this);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                textView.setBackgroundColor(Color.parseColor("#ffffff"));
                textView.setTextColor(Color.BLACK);
                //textView.setWidth(55);//texview_size
                textView.setText("11111");//tmp01.get(a)
                tableRow.addView(textView, j);
                //a++;
                //if (a>len_all){break;}
            }
            tl1.addView(tableRow, i);
            //if (a>len_all){break;}
        }

     */



        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(this);
        //временно выводим в ол все данные из таблицы докторов
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String sqlQuery0 = "select  * from tableDoctorsAdress";
        Cursor c0 = db.rawQuery(sqlQuery0, null);
        //Log.d(LOG_TAG, "tableDoctorsAdress ="+String.valueOf(c0.getCount()));
        if (c0.getCount()==0){
     //       Log.d(LOG_TAG, "tableDoctorsAdress is null");
            ContentValues cv20 = new ContentValues();
            cv20.put("name", "коломенская");
            // вставляем запись и получаем ее ID
            long rowID2 = db.insert("tableDoctorsAdress", null, cv20);
            cv20.put("name", "взлетная");
            rowID2 = db.insert("tableDoctorsAdress", null, cv20);
        }
        c0.close();

        String sqlQuery00 = "select  * from tableCardTip";
        Cursor c00 = db.rawQuery(sqlQuery00, null);
    //    Log.d(LOG_TAG, "tableCardTip ="+String.valueOf(c00.getCount()));
        if (c00.getCount()==0){
            Log.d(LOG_TAG, "tableCardTip is null");
            ContentValues cv020 = new ContentValues();
            cv020.put("name", "маленькая");
            long rowID20 = db.insert("tableCardTip", null, cv020);
            cv020.put("name", "пластик");
            rowID20 = db.insert("tableCardTip", null, cv020);
            cv020.put("name", "пластик А4 синий");
            rowID20 = db.insert("tableCardTip", null, cv020);
            cv020.put("name", "пластик А4 желный");
            rowID20 = db.insert("tableCardTip", null, cv020);
            cv020.put("name", "пластик А4 зеленый");
            rowID20 = db.insert("tableCardTip", null, cv020);
            cv020.put("name", "пластик А4 краный");
            rowID20 = db.insert("tableCardTip", null, cv020);
            cv020.put("name", "А4 зеленая");
            rowID20 = db.insert("tableCardTip", null, cv020);
            cv020.put("name", "А4 синяя");
            rowID20 = db.insert("tableCardTip", null, cv020);
            cv020.put("name", "А4 черная");
            rowID20 = db.insert("tableCardTip", null, cv020);
            cv020.put("name", "А4 серая");
            rowID20 = db.insert("tableCardTip", null, cv020);
            cv020.put("name", "А4 красная");
            rowID20 = db.insert("tableCardTip", null, cv020);
            cv020.put("name", "А4 желтая");
            rowID20 = db.insert("tableCardTip", null, cv020);
            cv020.put("name", "А4 розовая");
            rowID20 = db.insert("tableCardTip", null, cv020);
            cv020.put("name", "А4 фиолетовая");
            rowID20 = db.insert("tableCardTip", null, cv020);
            cv020.put("name", "А4 голубая");
            rowID20 = db.insert("tableCardTip", null, cv020);
            cv020.put("name", "Беременная");
            rowID20 = db.insert("tableCardTip", null, cv020);

        }
        c00.close();


       // String sqlQuery = "select * from tableJournals";
      /* String sqlQuery = "select " +
               "tableJournals.id,tableJournals.iddoctor,tableJournals.idpacient,"+
               "tablePacients.fio as f1,tablePacients.numbercard,tablePacients.birthday," +
               "tableDoctors.fio as f2,tableDoctors.department," +
               "tableJournals.dateoperation from tableJournals " +
               "left join tablePacients on tableJournals.idpacient=tablePacients.id "+
               "left join tableDoctors on tableJournals.iddoctor=tableDoctors.id " ;

       */
        //    \""
        String sqlQuery = "select " +
               "tableJournals.id,"+
             //   " tablePacients.fio || ' (' ||tablePacients.birthday|| '/'||tablePacients.numbercard ||')' as pacient,"+
                " tablePacients.fio || ' (' ||tablePacients.birthday|| '/'||tableCardTip.name ||')' as pacient,"+
            //    "tableDoctors.fio || ' (' || tableDoctors.department  ||')' as doctor," +
                "tableDoctors.fio || ' (' || tableDoctorsAdress.name  ||')' as doctor," +
                "tableJournals.dateoperation, "+
                "tableJournals.notfound "+
        " from tableJournals " +
                "left join tablePacients on tableJournals.idpacient=tablePacients.id "+
                "left join tableDoctors on tableJournals.iddoctor=tableDoctors.id "+
                "left join tableDoctorsAdress on tableDoctorsAdress.id=tableDoctors.department "+
                "left join tableCardTip on tableCardTip.id=tablePacients.numbercard " ;

        if(sortTable.equals("1")){sqlQuery=sqlQuery+ " order by tableDoctors.fio, tablePacients.fio ";};
        if(sortTable.equals("2")){sqlQuery=sqlQuery+ " order by tablePacients.fio ";};
        if(sortTable.equals("3")){sqlQuery=sqlQuery+ " order by tableJournals.dateoperation, tableDoctors.fio, tablePacients.fio ";};

        Cursor c = db.rawQuery(sqlQuery, null);
        int colonka_all=c.getColumnCount();
        int colonka=0;
        int stroka=0;
        //Для получения значений строки нужно переместить курсор на желаемую строку результата. Для этого используются методы moveToFirst(), moveToLast(), moveToNext()
        //Каждый из этих методов перемещает курсор на заданную строку, после чего можно прочитать значения этой строки по столбцам. Для этого используются методы-геттеры с индексом столбца в качестве аргумента, например getString(columnIndex: Int).
        //tl1.setColumnShrinkable(1,true); //перенос слов на след строку
        //tl1.setColumnShrinkable(2,true);
        tl1.setColumnCollapsed(0,true);//скрываем колонку
       // tl1.setStretchAllColumns(true);
        tl1.setShrinkAllColumns(true);
        boolean colorkey=true;

     //   if (c.getCount() == 0) {


/*
            LinearLayout mainLayout = (LinearLayout)findViewById(R.id.LinwarLayoout01);
            Button addButton =new Button(this);
            addButton.setText("add1");
            mainLayout.addView(addButton);

            ScrollView mainLayout2 = (ScrollView) findViewById(R.id.Scroll1);
            Button addButton2 =new Button(this);
            addButton2.setText("add2");
            mainLayout2.addView(addButton);


            TableLayout mainLayout3 = (TableLayout) findViewById(R.id.TableLayout01);
            Button addButton3 =new Button(this);
            addButton3.setText("add3");
            mainLayout3.addView(addButton);

 */
     //   }
        if (c.getCount() > 0) {
            Button Butdeath = findViewById(R.id.button01ButtonAddFirstLine);
            Butdeath.setVisibility(View.GONE);
            Button Butdeath2 = findViewById(R.id.button01ButtonAddFirstLine2);
            Butdeath2.setVisibility(View.GONE);
        }
//display size
        Display display1 = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display1.getRealSize(size);
        int scrWidth = size.x;
        int scrHeight = size.y;
        int PoleSizeX=(scrWidth-150)/2;
//Log.d(LOG_TAG,String.valueOf(scrWidth)+" "+String.valueOf(scrHeight));
  //      Log.d(LOG_TAG,String.valueOf(PoleSizeX));

        if (c != null) {
            if (c.moveToFirst()) {
                String str;
                do {
                    TableRow tableRow = new TableRow(this);
                    //tableRow.setId(stroka+1);
                    tableRow.setOnLongClickListener(TableLayout01nLongClickListener33);
                    tableRow.setOnClickListener(TableLayout01nDobleClickListener3333);
                    if(colorkey==true)   tableRow.setBackgroundColor(Color.LTGRAY);
                    else tableRow.setBackgroundColor(Color.GRAY);
                    str = "";
                    Integer notORfound=c.getInt(c.getColumnIndex("notfound"));

                    for (String cn : c.getColumnNames()) {
                        if (!cn.equals("notfound")){

                        TextView textView = new TextView(this);
                        textView.setGravity(Gravity.CENTER_HORIZONTAL);
                        if(colorkey==true)    textView.setBackgroundColor(Color.LTGRAY);
                        else textView.setBackgroundColor(Color.GRAY);
                            //Integer notORfound=c.getInt(c.getColumnIndex("notfound"));
                            if (notORfound==1)  textView.setBackgroundColor(Color.parseColor("#FFFB4848"));
                            if (notORfound==2)  textView.setBackgroundColor(Color.parseColor("#FF66B6FB"));
                        textView.setTextColor(Color.BLACK);
                        textView.setText(c.getString(c.getColumnIndex(cn)));
                        textView.setTextSize(16);
                        textView.setMaxWidth(PoleSizeX);
                        textView.setMinWidth(300);
                        if(colonka==3){textView.setMaxWidth(130);textView.setMinWidth(130);};
                        textView.setPadding(0,5,10,5);
                        //Log.d("TAVKA",sortTable);
                           // int xz=Integer.parseInt(sortTable);
                            //Log.d("TAVKA",String.valueOf(sortTable));
                        if( stroka==0 && colonka==Integer.parseInt(sortTable))
                            {
                                textView.setBackgroundColor(Color.GRAY);
                               // tableRow.setBackgroundColor(Color.GREEN);
                            }
                        tableRow.addView(textView, colonka);
                        colonka++;
                        }

                        else
                        {
                           // if (c.getInt(c.getColumnIndex(cn))==1)
                            if(notORfound==1)
                            {
                                tableRow.setBackgroundColor(Color.parseColor("#FFFB4848"));
                            }
                            if(notORfound==2)
                            {
                                tableRow.setBackgroundColor(Color.parseColor("#FF66B6FB"));
                            }

                        }



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
                //finish();
               // Intent intent7 = new Intent(this, ZagruzkaDayExcel.class);
                Intent intent7 = new Intent(this, zagruzka_day_excel_test2.class);
                startActivity(intent7);
                break;
            case 6:///office
                //finish();
                Intent intent6 = new Intent(this, Spravochniki.class);
                //intent6.putExtra("spravochnik","office");
                intent6.putExtra("spravochnik","1");
                startActivity(intent6);
                break;
            case 5://cards
                //finish();
                Intent intent5 = new Intent(this, Spravochniki.class);
                //intent5.putExtra("spravochnik","cards");
                intent5.putExtra("spravochnik","2");
                startActivity(intent5);
                break;
            case 4:
                //finish();
                Intent intent4 = new Intent(this, journal.class);
                startActivity(intent4);
                break;
            case 3:
                //finish();
                Intent intent3 = new Intent(this, doctors.class);
                startActivity(intent3);
                break;
            case 2:
                //finish();
                Intent intent2 = new Intent(this, patient.class);
                startActivity(intent2);
                break;
            case 1:
                //finish();
                Intent intent1 = new Intent(this, MainActivity.class);
                startActivity(intent1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private View.OnClickListener TableLayout01nDobleClickListener3333 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //startTime1
            long elapsedTime1 = 0;
            elapsedTime1 = System.currentTimeMillis() - startTime1;
          //  Log.d("TAVKA","elapsedTime1="+String.valueOf(elapsedTime1));
            if  (elapsedTime1<400)
            {
                Log.d("TAVkA","===="+sortTable);
                if(sortTable.equals("1")||sortTable.length()==0){sortTable="2";}
                else
                if(sortTable.equals("2")){sortTable="3";}
                else
                if(sortTable.equals("3")){sortTable="1";}


              Intent intent123 = new Intent(MainActivity.this,   MainActivity.class);
              intent123.putExtra("sortirovka", sortTable);//
              startActivity(intent123);
            }
            else  startTime1 = System.currentTimeMillis();
        }
    };


private View.OnLongClickListener TableLayout01nLongClickListener33 = new View.OnLongClickListener() {
    @Override
    public boolean onLongClick(View view) {


        //текущую строку и получаем все textview из строки
        TableRow row = (TableRow) view;
        int count = row.getChildCount();

        TextView child = (TextView) row.getChildAt(0);
        String text = child.getText().toString();
        CARD_CHOISE_ID=Integer.valueOf(text);
        TextView child2 = (TextView) row.getChildAt(1);
        String text2 = child2.getText().toString();
        CARD_CHOISE_PACIENT_INFO=text2;

        TextView child3 = (TextView) row.getChildAt(2);
        String text3 = child3.getText().toString();
        CARD_CHOISE_DOC_INFO=text3;


        showDialog(DIALOG_CHOISE);
       return false;
    }};


    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_CHOISE) {
                AlertDialog.Builder adb = new AlertDialog.Builder(this);
                // заголовок
                adb.setTitle("Выбор действия по карте");

            adb.setItems(new CharSequence[] {"Новая запись", "Новая запись (загруженные)", "Изменить запись", "Закрыть запись", "не найдена/найдена"},
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // The 'which' argument contains the index position
                            // of the selected item
                            switch (which) {
                                case 0:
                                    newJournalData();
                                    break;
                                case 1:
                                    ZagruzkaDayExcel();
                                    break;
                                case 2:
                                    ChanseJournalData();
                                    break;
                                case 3:
                                    CloseJournalData();
                                    break;
                                case 4:
                                    NotFoundorFound();
                                    break;
                            }
                        }
                    });

                // создаем диалог
               return adb.create();
            }


        return super.onCreateDialog(id);
    }


    void NotFoundorFound(){
        //CARD_CHOISE_ID
        SQLiteDatabase db234 = dbHelper.getWritableDatabase();
        Integer notORfound;
       // Log.d("TAVKA","01");
        String sqlQuery22 = "select notfound from tableJournals where id="+String.valueOf(CARD_CHOISE_ID);
        Cursor c22 = db234.rawQuery(sqlQuery22, null);
        //Log.d("TAVKA","02");
        if (c22.getCount()>0) {
          //  Log.d("TAVKA","03");
            c22.moveToFirst();
           // do {
            //    Log.d("TAVKA","04");
                //for (String cn : c22.getColumnNames()) { Log.d("TAVKA",cn);  }
                notORfound=c22.getInt(c22.getColumnIndex("notfound"));
              //  Log.d("TAVKA","05");
                if (notORfound==null || notORfound==0) {  // ставим что карта не найдена
                    notORfound=1;
                }
                else if (notORfound==1) {   // ставим что картанайдена
                    notORfound=0;
                }
            String sqlQuery33 = "update  tableJournals "
                    + " set notfound="+String.valueOf(notORfound)
                    + " where  id=" +String.valueOf(CARD_CHOISE_ID);
            db234.execSQL(sqlQuery33);

            //} while (c22.moveToNext());
            //Log.d("TAVKA","06");
          Log.d("TAVKA","notORfound="+String.valueOf(notORfound));

        }
        c22.close();
        db234.close();
        finish();
        Intent intent1 = new Intent(this, MainActivity.class);
        startActivity(intent1);
        dbHelper.close();
    }


    void ZagruzkaDayExcel(){
        Log.d(LOG_TAG, "Загрузка данных из excel");
        Intent intent1 = new Intent(this, GetListExcelDialog.class);
        startActivity(intent1);
    }


    void newJournalData() {
        Log.d(LOG_TAG, "Новая запись");
        //showDialog(DIALOG_ADD);

        Intent intent1 = new Intent(this, dialog_journal_add.class);
        startActivity(intent1);

    }

    void ChanseJournalData() {
        Log.d(LOG_TAG, "Изменить запись");
       // showDialog(DIALOG_MODIFY);
       // Log.d(LOG_TAG, "отправляем данные на редактирование= " + CARD_CHOISE_ID);
        Intent intent1 = new Intent(this, dialogJournalModifyDoctor.class);
        intent1.putExtra("cardid", String.valueOf(CARD_CHOISE_ID));
        intent1.putExtra("PacientInfo", CARD_CHOISE_PACIENT_INFO);
        startActivity(intent1);
    }

    void CloseJournalData() {
        //CARD_CHOISE_PACIENT_INFO
        //CARD_CHOISE_DOC_INFO
        Log.d(LOG_TAG, "Закрыть запись"+String.valueOf(CARD_CHOISE_ID));
        // удаляем по id
        // создаем объект для создания и управления версиями БД
        //dbHelper = new DBHelper(this);
        //временно выводим в ол все данные из таблицы докторов
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int delCount = db.delete("tableJournals", "id = " + CARD_CHOISE_ID, null);
        Log.d(LOG_TAG, "deleted rows count = " + delCount);



        //ЗАПОЛНЯЕМ ДАННЫЕ В ЖУРНАЛЕ ОПЕРАЦИЙ!!!!!!!!!!
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        String currentdate=String.valueOf(format.format(new Date()));
        ContentValues cv2 = new ContentValues();
        //cv2.put("idpacient",id_pacient);
        cv2.put("fiopacient",CARD_CHOISE_PACIENT_INFO);
        //cv2.put("iddoctor",id_doc);
        cv2.put("fiodoctor",CARD_CHOISE_DOC_INFO);
        cv2.put("operation","DEL");
        cv2.put("dateoperation",currentdate);
        cv2.put("user","local");
        // вставляем запись и получаем ее ID
        long rowID2 = db.insert("tableArchives", null, cv2);
        Log.d(LOG_TAG, "row inserted tableArchives, ID = " + rowID2);


        db.close();
        finish();
        Intent intent1 = new Intent(this, MainActivity.class);
        startActivity(intent1);
        dbHelper.close();
      }

/*
    public void LongClickActivity(View view) {
        Log.d(LOG_TAG, "Long Click");
    }


View.OnLongClickListener Scroll01nLongClickListener=new View.OnLongClickListener() {
    @Override
    public boolean onLongClick(View view) {
        Log.d(LOG_TAG, "Long Click!!!!!!!!!");
        return false;
    }
} ;

 */
public void    ButtonAddFirstLine   (View view) {
    Log.d(LOG_TAG, "   ButtonAddFirstLine   ");
    newJournalData();
}

    public void    ButtonAddFirstLine2   (View view) {
        Log.d(LOG_TAG, "   ButtonAddFirstLine2   ");
        ZagruzkaDayExcel();
        //zagruzka_day_excel_test2
    }

}