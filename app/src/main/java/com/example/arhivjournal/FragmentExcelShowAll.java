package com.example.arhivjournal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class FragmentExcelShowAll extends Fragment {
    SQLiteDatabase db;


    public FragmentExcelShowAll() {
        // Required empty public constructor
    }


    public static FragmentExcelShowAll newInstance(String param1, String param2) {
        FragmentExcelShowAll fragment = new FragmentExcelShowAll();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View ViewFragment=inflater.inflate(R.layout.fragment_excel_show_all, container, false);
        Log.d("TAVKA", "1111111111111111111111111111111");
      TableLayout tlDOC= (TableLayout) ViewFragment.findViewById(R.id.TableLayoutExcelShowAll);
        tlDOC.setColumnShrinkable(1,true); //перенос слов на след строку
        tlDOC.setColumnShrinkable(2,true);
        tlDOC.setStretchAllColumns(true);
        tlDOC.setColumnCollapsed(0,true);

        db=DBHelper.getInstance(getContext()).getWritableDatabase();
//заполняем  ArrayList  для spinner
         Cursor c;

        String sqlQuery =   " select tableExcelRequestDay.saved as saved,tableExcelRequestDay.id as excelid, tableExcelRequestDay.idpacient as idpacient,tableExcelRequestDay.iddoctor "+
                            ",tableDoctors.fio as docfio,"+
                            " tablePacients.fio || ' (' || tablePacients.birthday  ||')' as pacientfullname "+
                //" ,tableExcelRequestDay.saved as saved "+
                            " from tableExcelRequestDay "+
                            " left join tableDoctors on tableExcelRequestDay.iddoctor=tableDoctors.id "+
                           " left join tablePacients on tableExcelRequestDay.idpacient=tablePacients.id "+
                            " where tableExcelRequestDay.saved is not 'TRUE'";



        c = db.rawQuery(sqlQuery, null);
        if (c.getCount()> 0) {
            c.moveToFirst();
            int i=0;
            do {
                TableRow tr = new TableRow(getActivity());

                TextView textView0 = new TextView(getActivity());
                textView0.setText( c.getString(c.getColumnIndex("excelid")) );
                textView0.setPadding(0,5,10,5);
                tr.addView(textView0,0);

                TextView textView1 = new TextView(getActivity());
                textView1.setText( c.getString(c.getColumnIndex("docfio")) );
                textView1.setPadding(0,5,10,5);
                tr.addView(textView1,1);

                TextView textView2 = new TextView(getActivity());
                textView2.setText( c.getString(c.getColumnIndex("pacientfullname")) );
                textView2.setPadding(0,5,10,5);
                tr.addView(textView2,2);
/*
                TextView textView3 = new TextView(getActivity());
                textView3.setText( c.getString(c.getColumnIndex("saved")) );
                textView3.setPadding(0,5,10,5);
                tr.addView(textView3,2);


 */
                tlDOC.addView(tr,i);
                i++;


            } while (c.moveToNext());
        }


        c.close();


db.close();


        // Inflate the layout for this fragment
        return ViewFragment;
    }
}