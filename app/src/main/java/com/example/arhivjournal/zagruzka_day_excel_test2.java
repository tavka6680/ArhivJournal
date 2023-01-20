package com.example.arhivjournal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class zagruzka_day_excel_test2 extends AppCompatActivity {
    TextView tvTest;
    Integer PAGE_COUNT =0;
    List<String[]> rowList = new ArrayList<String[]>();
    public static String points[] =  new String[66];
    DBHelper dbHelper2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zagruzka_day_excel_test2);
        Button butDownload = findViewById(R.id.buttonDownloadExcel);
        butDownload.setOnClickListener(butDownloadOnClick);

        Button  bbb2 = (Button) findViewById((R.id.ExcelbuttonShowAllExcel2));
        bbb2.setOnClickListener(ButtonExcelShowAll);

        Button  bbb3 = (Button) findViewById((R.id.ExcelbuttonClearAll2));
        bbb3.setOnClickListener(ButtonExcelClearAll);


        tvTest = findViewById(R.id.textViewTestExcel);
        for(int i=0;i<66;i++)        {     points[i]="0";       }

        dbHelper2 = new DBHelper(zagruzka_day_excel_test2.this);
    };


    View.OnClickListener ButtonExcelClearAll = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SQLiteDatabase db = dbHelper2.getWritableDatabase();
            int delCount = db.delete("tableExcelRequestDay", "saved is not 'TRUE'", null); //"saved is not 'TRUE'"
            //finish();
            db.close();

        }
    };


    View.OnClickListener ButtonExcelShowAll = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ViewPager2 pager3 = findViewById(R.id.ViewPager2ExcelTEST);

            FragmentStateAdapter pageAdapter3 = new zagruzka_day_excel_test2.ZZMyAdapterShowAll4(zagruzka_day_excel_test2.this);
            pager3.setAdapter(pageAdapter3);

        }
    };


View.OnClickListener     butDownloadOnClick= new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        TextView tt = findViewById(R.id.ExelDocIDTextView2);
        tt.setText("-1");
        tvTest.setText(tvTest.getText()+"-butDownloadOnClick");
        Intent PickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        PickerIntent.setType("*/*");
        startActivityForResult(PickerIntent, 1);
    }
};


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String s="";
        tvTest.setText(tvTest.getText()+"-onActivityResult");
        if (requestCode==1) {
            tvTest.setText(tvTest.getText()+"-requestCode==1");
            if (resultCode == RESULT_OK)  {
                tvTest.setText(tvTest.getText()+"-resultCode == RESULT_OK");
                Uri chosenUri = data.getData();
                String src = chosenUri.getPath();
                InputStream stream = null;
                try {
                    tvTest.setText(tvTest.getText()+"-try");
                    stream  = getContentResolver().openInputStream(chosenUri);
                } catch (FileNotFoundException e) {
                    tvTest.setText(tvTest.getText()+"-catch");
                    e.printStackTrace();
                }
                try {
                    tvTest.setText(tvTest.getText()+"-try2");
                    String fiop,carta,tipcarta,datar,vrach,clockWord,mobilka;
                    XSSFWorkbook workbook = new XSSFWorkbook(stream);
                    XSSFSheet sheet = workbook.getSheetAt(0);
                    int rowsCount = sheet.getPhysicalNumberOfRows();
                    FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
                    tvTest.setText(tvTest.getText()+"-for1");
                    for (int r = 0; r<rowsCount; r++) {
                        tvTest.setText(tvTest.getText()+"-"+String.valueOf(r));
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
                        tvTest.setText(tvTest.getText()+"-for2");
                        if (r>=4)
                        {
                            for (int c = 0; c < cellsCount; c++) {
                                tvTest.setText(tvTest.getText() + "-" + String.valueOf(c));
                                String value="";
                                // Преобразуем содержимое каждой сетки в строковую форму
                                try {
                                    value = getCellAsString(row, c, formulaEvaluator);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                Log.d("TAVKA", value);
                                //s=s+value;
                                tvTest.setText(tvTest.getText() + "-" + value);

                                if (c==1){clockWord=value.trim().toLowerCase();};
                                if (c==2){fiop=value.trim().toLowerCase();};
                                if (c==4){datar=value.trim().toLowerCase();};
                                if (c==6){carta=value.trim().toLowerCase();};
                                if (c==8){mobilka=value.trim().toLowerCase();};
                                if (c==10){tipcarta=value.trim().toLowerCase();};
                                if("часы".equals(clockWord)    && c==2)
                                {
                                    vrach=value.trim().toLowerCase();
                                    tvTest.setText(tvTest.getText()+"r:"+r+" Врач:"+vrach);
                                    rowList.add(new String[] { vrach, "-", "-","-", "-"});

                                };


                            }
                        }
                        if(fiop.length()>2  && "часы".equals(clockWord) ==false)  //fiop!="" &&
                        {
                            carta=carta.replace(".0","");
                            fiop=fiop.replace(carta,"");
                            fiop=fiop.trim();
                            tvTest.setText(tvTest.getText()+"---"+fiop+"-"+datar+"-"+carta+"-"+tipcarta);
                            if (fiop.isEmpty()) fiop="-";
                            if (datar.isEmpty()) datar="-";
                            if (carta.isEmpty()) carta="-";
                            if (tipcarta.isEmpty()) tipcarta="-";
                            if (mobilka.isEmpty()) mobilka="-";

                            rowList.add(new String[] { fiop, datar, carta,tipcarta, mobilka});
                        };


                    }
                } catch (Exception e) {
                    tvTest.setText(tvTest.getText()+"-catch2");
                    /* proper exception handling to be here */
                }

                //если массив пациентов создался делаем фрагменты
               if (rowList.size()>0)
                {
                    tvTest.setText(tvTest.getText()+"-rowList.size()>0");
                    PAGE_COUNT= rowList.size();
                    ViewPager2 pager = findViewById(R.id.ViewPager2ExcelTEST);
                    //FragmentStateAdapter pageAdapter = new ZagruzkaDayExcel.ZZMyAdapter(this);
                    FragmentStateAdapter pageAdapter = new zagruzka_day_excel_test2.ZZMyAdapter2(this);
                    pager.setAdapter(pageAdapter);
                }


            }
        }
    };



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
        }
        return value;
    };





private class ZZMyAdapter2 extends FragmentStateAdapter {
        //   private Integer ExcelDocID1=0;




        public ZZMyAdapter2(FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }
        // private Integer ExcelDocID2=0;
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position==0) {
                FragmentExcelDoctor ffff1 = new FragmentExcelDoctor();
                Bundle args = new Bundle();
                String[] row=rowList.get(position);
                args.putString("param1", row[0].trim());
                ffff1.setArguments(args);
                return ffff1;
            }else
            if (position>0&&position<PAGE_COUNT) {
                String[] row=rowList.get(position);
                FragmentExcelPacient ffff2 = new FragmentExcelPacient();
                Bundle args2 = new Bundle();
                args2.putString("param1", row[0]);
                args2.putString("param2", row[1]);
                args2.putString("param3", row[2]);
                args2.putString("param4", row[3]);
                args2.putString("param5", row[4]);
                args2.putString("param0",  String.valueOf(position));
                ffff2.setArguments(args2);
                return ffff2;
            }else return null;

        }

        @Override
        public int getItemCount() {
            return PAGE_COUNT;
        }
    }



    private class ZZMyAdapterShowAll4 extends FragmentStateAdapter {

        public ZZMyAdapterShowAll4(zagruzka_day_excel_test2 fragmentActivity) {//View.OnClickListener onClickListener
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
                // Intent intent7 = new Intent(this, ZagruzkaDayExcel.class);
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