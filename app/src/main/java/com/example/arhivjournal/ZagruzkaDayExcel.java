package com.example.arhivjournal;

import android.app.TabActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
//import com.blankj.utilcode.util.LogUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ZagruzkaDayExcel extends AppCompatActivity {  //extends TabActivity {
    private static String LOG_TAG= "TAVKA";
    DBHelper dbHelper;
    public static String points[] =  new String[50];

    //ScrollView Scroll01;
    //LinearLayout LinearLayout01;
    //RelativeLayout RelativeLayout01;

    Integer PAGE_COUNT =0;
    List<String[]> rowList = new ArrayList<String[]>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zagruzka_day_excel);

        Button bbb = (Button) findViewById(R.id.ExcelbuttonDownloadExcel);
        bbb.setOnClickListener(ButtonDownloadExcelOnClick);

        Button  bbb2 = (Button) findViewById((R.id.ExcelbuttonShowAllExcel));
        bbb2.setOnClickListener(ButtonExcelShowAll);

        Button  bbb3 = (Button) findViewById((R.id.ExcelbuttonClearAll));
        bbb3.setOnClickListener(ButtonExcelClearAll);

        //Scroll01 =(ScrollView) findViewById(R.id.ExcelScrollView);
        //LinearLayout01 = (LinearLayout)findViewById(R.id.ExcelLayout01);
       // RelativeLayout01= (LinearLayout)findViewById(R.id.ExcelLayout01);

        for(int i=0;i<50;i++)        {     points[i]="0";       }

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
                Intent intent7 = new Intent(this, ZagruzkaDayExcel.class);
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


    View.OnClickListener ButtonExcelClearAll = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dbHelper = new DBHelper(ZagruzkaDayExcel.this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            int delCount = db.delete("tableExcelRequestDay", "saved is not 'TRUE'", null); //"saved is not 'TRUE'"
             Log.d(LOG_TAG, "deleted rows count = " + delCount);
            //finish();
            db.close();

        }
    };


   View.OnClickListener ButtonExcelShowAll = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ViewPager2 pager3 = findViewById(R.id.ExcelViewPager2);
            FragmentStateAdapter pageAdapter3 = new ZZMyAdapterShowAll(ZagruzkaDayExcel.this);
            pager3.setAdapter(pageAdapter3);

        }
    };

    View.OnClickListener ButtonDownloadExcelOnClick=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(LOG_TAG, "ZagruzkaDayExcel");
            TextView TextViewTest=findViewById(R.id.textViewTEST);
            TextViewTest.setText("TEST:");
            TextViewTest.setText(TextViewTest.getText()+"-A1-");
           Intent PickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
            TextViewTest.setText(TextViewTest.getText()+"-A2-");
            PickerIntent.setType("*/*");
            TextViewTest.setText(TextViewTest.getText()+"-A3-");
            startActivityForResult(PickerIntent, 1);
            TextViewTest.setText(TextViewTest.getText()+"-A4-");
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TextView TextViewTest=findViewById(R.id.textViewTEST);
        rowList.clear();
        //switch (requestCode) {
         //   case 1: {
       // TextViewTest.setText(TextViewTest.getText()+"-01-");
        if (requestCode==1){
         //   TextViewTest.setText(TextViewTest.getText()+"-02-");
                if (resultCode == RESULT_OK)  {
           //         TextViewTest.setText(TextViewTest.getText()+"-03-");
                    Uri chosenUri = data.getData();
                    String src = chosenUri.getPath();
                    Log.d(LOG_TAG, src);
             //       TextViewTest.setText(TextViewTest.getText()+"-031-"+src);
                    InputStream stream = null;
                    try {
               //         TextViewTest.setText(TextViewTest.getText()+"-04-");
                         stream  = getContentResolver().openInputStream(chosenUri);
                 //       TextViewTest.setText(TextViewTest.getText()+"-041-");
                    } catch (FileNotFoundException e) {
                   //     TextViewTest.setText(TextViewTest.getText()+"-05-");
                        e.printStackTrace();
                        Log.d(LOG_TAG, "1111111111111111111111111111111111111111111111111");
                     //   TextViewTest.setText(TextViewTest.getText()+"-051-");
                    }
                    try {
                       // TextViewTest.setText(TextViewTest.getText()+"-06-");
                        XSSFWorkbook workbook = new XSSFWorkbook(stream);
                        XSSFSheet sheet = workbook.getSheetAt(0);
                        //TextViewTest.setText(TextViewTest.getText()+"-061-");
                        // получаем TabHost
                        //TabHost tabHost = getTabHost();
                        // инициализация была выполнена в getTabHost  // метод setup вызывать не нужно
                       // TabHost.TabSpec tabSpec;
                       // TabHost tabHost = (TabHost) findViewById(R.id.ExcelTabHost);
                       // tabHost.setup(this.getLocal);
                       // TabHost.TabSpec tabSpec;


                        int rowsCount = sheet.getPhysicalNumberOfRows();
                        FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
                        String fiop,carta,tipcarta,datar,vrach,clockWord,mobilka;
                       // TextViewTest.setText(TextViewTest.getText()+"-062-");
                        for (int r = 0; r<rowsCount; r++) {
                            TextViewTest.setText(TextViewTest.getText()+"-07-");
                            Row row = sheet.getRow(r);
                            int cellsCount = row.getPhysicalNumberOfCells();
                            fiop="";
                            carta="";
                            tipcarta="";
                            datar="";
                            vrach="";
                            clockWord="";
                            mobilka="";
                            // Читаем содержимое одной строки за раз
                            if (r>=4){
                            for (int c = 0; c<cellsCount; c++)
                            {
                                TextViewTest.setText(TextViewTest.getText()+"-08-");
                                // Преобразуем содержимое каждой сетки в строковую форму
                                String value = getCellAsString(row, c, formulaEvaluator);
                                //String cellInfo = "r:"+r+"; c:"+c+"; v:"+value;
                                if (c==1){clockWord=value.trim().toLowerCase();};
                                if (c==2){fiop=value.trim().toLowerCase();};
                                if (c==4){datar=value.trim().toLowerCase();};
                                if (c==6){carta=value.trim().toLowerCase();};
                                if (c==8){mobilka=value.trim().toLowerCase();};
                                if (c==10){tipcarta=value.trim().toLowerCase();};
                               //нашли врача
                                if("часы".equals(clockWord)    && c==2)
                                {
                                    TextViewTest.setText(TextViewTest.getText()+"-09-");
                                    vrach=value.trim().toLowerCase();
                                    Log.d(LOG_TAG, "r:"+r+" Врач:"+vrach);
                                    TextViewTest.setText(TextViewTest.getText()+"r:"+r+" Врач:"+vrach);
                                    rowList.add(new String[] { vrach, "2", "3","4", "5"});
                                 //  tabSpec = tabHost.newTabSpec("tag1");
                                 //   tabSpec.setIndicator("Вкладка 1");
                                  //  tabSpec.setContent(new Intent(this, ExcelTabPacient.class));
                                   // tabHost.addTab(tabSpec);


                                    //LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    /*
                                    TextView tv01 = new TextView(this);
                                    tv01.setText("Врач:");
                                    tv01.setTextColor(Color.BLACK );
                                    LinearLayout01.addView(tv01);
                                     */
                                    /*
                                    EditText etDoctor = new EditText(this);
                                    etDoctor.setText(vrach);
                                    //etDoctor.setId(1);
                                    etDoctor.setBackgroundColor(Color.WHITE);
                                    etDoctor.setTextColor(Color.BLACK );
                                    LinearLayout01.addView(etDoctor);

                                    Spinner SpinnerDoctor = new Spinner(this);
                                    //SpinnerDoctor.setId(101);
                                    LinearLayout01.addView(SpinnerDoctor);

                                    Button btnNew = new Button(this);
                                    btnNew.setText("Ok");
                                    LinearLayout01.addView(btnNew);

                                    Button btnNew2 = new Button(this);
                                    btnNew2.setText("new");
                                    LinearLayout01.addView(btnNew2);

                                     */
                                };
                            }
                            }
                            if(fiop!="" && fiop.length()>2  && "часы".equals(clockWord) ==false)
                            {
                                TextViewTest.setText(TextViewTest.getText()+"-10-");
                                carta=carta.replace(".0","");
                                fiop=fiop.replace(carta,"");
                                fiop=fiop.trim();
                               // Log.d(LOG_TAG, "-"+fiop+"-"+datar+"-"+carta+"-"+tipcarta+" "+mobilka);
                                TextViewTest.setText(TextViewTest.getText()+"---"+fiop+"-"+datar+"-"+carta+"-"+tipcarta);
                                rowList.add(new String[] { fiop, datar, carta,tipcarta, mobilka });



                            };
                        }
                    } catch (Exception e) {
                        /* proper exception handling to be here */
                        Log.d(LOG_TAG, e.toString());
                        Log.d(LOG_TAG, "222222222222222222222222222222222222222222222222222");
                        TextViewTest.setText(TextViewTest.getText()+"-11-");
                    }
                    //если массив пациентов создался делаем фрагменты
                    if (rowList.size()>0)
                    {
                        TextViewTest.setText(TextViewTest.getText()+"-12-");
                        Log.d(LOG_TAG,"rowlist size="+String.valueOf(rowList.size()));
                                PAGE_COUNT= rowList.size();
                        ViewPager2 pager = findViewById(R.id.ExcelViewPager2);
                        FragmentStateAdapter pageAdapter = new ZZMyAdapter(this);
                        pager.setAdapter(pageAdapter);
                    }
                }
            }
        //}

    }



    private class ZZMyAdapterShowAll extends FragmentStateAdapter {

        public ZZMyAdapterShowAll(ZagruzkaDayExcel fragmentActivity) {//View.OnClickListener onClickListener
            super((FragmentActivity) fragmentActivity);
        }


        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return new FragmentExcelShowAll();
        }

        @Override
        public int getItemCount() {
            return 1;
        }
    }




    private class ZZMyAdapter extends FragmentStateAdapter {
   //   private Integer ExcelDocID1=0;




public ZZMyAdapter(FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }
       // private Integer ExcelDocID2=0;
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            // Integer ExcelDocID3=0;
            if (position==0) {
                FragmentExcelDoctor ffff1 = new FragmentExcelDoctor();
                Bundle args = new Bundle();
                String[] row=rowList.get(position);
                args.putString("param1", row[0].trim());
                //args.putString("param2", "argument2");
                ffff1.setArguments(args);
                return ffff1;
            }else
            if (position>0&&position<PAGE_COUNT) {
                String[] row=rowList.get(position);
                Log.d(LOG_TAG,"row[0]="+row[0]);
                FragmentExcelPacient ffff2 = new FragmentExcelPacient();
                Bundle args = new Bundle();
                args.putString("param1", row[0]);
                args.putString("param2", row[1]);
                args.putString("param3", row[2]);
                args.putString("param4", row[3]);
                args.putString("param5", row[4]);
                args.putString("param0",  String.valueOf(position));
                ffff2.setArguments(args);
                return ffff2;
            }else return null;

//            return null;
        }

        @Override
        public int getItemCount() {
            return PAGE_COUNT;
        }
    }


    /*
     * Прочтите содержимое каждой строки в файле Excel
     * @param row
     * @param c
     * @param formulaEvaluator
     * @return
     */
    private static String getCellAsString(Row row, int c, FormulaEvaluator formulaEvaluator) {
        String value = "";
        try {
            Cell cell = row.getCell(c);
            CellValue cellValue = formulaEvaluator.evaluate(cell);
            switch (cellValue.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    value = ""+cellValue.getBooleanValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    double numericValue = cellValue.getNumberValue();
                    if(HSSFDateUtil.isCellDateFormatted(cell)) {
                        double date = cellValue.getNumberValue();
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
                        value = formatter.format(HSSFDateUtil.getJavaDate(date));
                    } else {
                        value = ""+numericValue;
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = ""+cellValue.getStringValue();
                    break;
                default:
                    break;
            }
        } catch (NullPointerException e) {
            /* proper error handling should be here */

        //    Log.d(LOG_TAG, e.toString());
          //  Log.d(LOG_TAG, "333333333333333333333333333333333333333333333333333333333333");
        }
        return value;
    }



}