package com.example.arhivjournal;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentExcelPacient#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentExcelPacient extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";
    private static final String ARG_PARAM0 = "param0";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;
    private String mParam4;
    private String mParam5;
    private String mParam0;
    ArrayList<String> SpravochnikPacientIDadd = new ArrayList<>();
    ArrayList<String> SpravochnikPacientNAMEadd = new ArrayList<>();
    String LOG_TAG="TAVKA";
    ArrayList<String> SpravochnikDCardID = new ArrayList<>();
    ArrayList<String> SpravochnikCardNAME = new ArrayList<>();
    TextView ExelDocIDTextView02;
    SQLiteDatabase db;

    public FragmentExcelPacient() {        // Required empty public constructor
            }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentExcelPacient.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentExcelPacient newInstance(String param11, String param12, String param13, String param14, String param15, String param00) {

        FragmentExcelPacient fragment = new FragmentExcelPacient();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param11);
        args.putString(ARG_PARAM2, param12);
        args.putString(ARG_PARAM3, param13);
        args.putString(ARG_PARAM4, param14);
        args.putString(ARG_PARAM5, param15);
        args.putString(ARG_PARAM0, param00);
        fragment.setArguments(args);
        return fragment;
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TAVKA","onCreate");
        ExelDocIDTextView02=getActivity().findViewById(R.id.ExelDocIDTextView2);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
            mParam4 = getArguments().getString(ARG_PARAM4);
            mParam5 = getArguments().getString(ARG_PARAM5);
            mParam0 = getArguments().getString(ARG_PARAM0);
        }
        // подключаемся к БД
         db = DBHelper.getInstance(getContext()).getWritableDatabase();
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("TAVKA","onCreateView");
        View viewFragment=inflater.inflate(R.layout.fragment_excel_pacient, container, false);
        Button ButtonOk= viewFragment.findViewById(R.id.ExcelButtonSootvetstvuet);
        Button ButtonSaveNew= viewFragment.findViewById(R.id.ExcelButtonSaveNew);
        ////////////////////////////////////////////////////////if( ZagruzkaDayExcel.points[Integer.parseInt(mParam0)].equals("1") )

        if( zagruzka_day_excel_test2.points[Integer.parseInt(mParam0)].equals("1") )
        {
            ButtonOk.setEnabled(false);
            ButtonSaveNew.setEnabled(false);
        }

        if (getArguments() != null) {

            EditText textOnFragment1=viewFragment.findViewById(R.id.FragmentExcelPacientEditTextFIO);
            textOnFragment1.setText(mParam1);
            EditText textOnFragment2=viewFragment.findViewById(R.id.FragmentExcelPacientEditTextBirthday);
            textOnFragment2.setText(mParam2);
            EditText textOnFragment3=viewFragment.findViewById(R.id.FragmentExcelPacientEditTextCard);
            textOnFragment3.setText(mParam3);
            EditText textOnFragment4=viewFragment.findViewById(R.id.FragmentExcelPacientEditTextTipCard);
            textOnFragment4.setText(mParam4);
            EditText textOnFragment5=viewFragment.findViewById(R.id.FragmentExcelPacientEditTextMOBILKA);
            textOnFragment5.setText(mParam5);


            //v34  v35
           // подключаемся к БД
            //SQLiteDatabase db = DBHelper.getInstance(getContext()).getWritableDatabase();
            //заполняем  ArrayList  для spinner
            Cursor c2;
            String sqlQuery2 = "select tablePacients.id as id ,tablePacients.fio || ' (' || tablePacients.birthday ||'/'|| tableCardTip.name ||')' as fullnameinfo from tablePacients "+
                    " left join tableCardTip on tableCardTip.id=tablePacients.numbercard "+
                    " order by fio";
            int count01=0,count02=0,count03=-1;
            String stroka="";
            c2 = db.rawQuery(sqlQuery2, null);
            if (c2.getCount()>0) {
                c2.moveToFirst();
                do {
                    SpravochnikPacientIDadd.add(c2.getString(c2.getColumnIndex("id")));
                    stroka=c2.getString(c2.getColumnIndex("fullnameinfo"));
                    SpravochnikPacientNAMEadd.add(stroka);
                    count02=stroka.lastIndexOf(mParam1.trim());
                    if(count02!=-1){count03=count01; };
                    count01++;
                } while (c2.moveToNext());
            }
            c2.close();
           //  адаптер
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, SpravochnikPacientNAMEadd);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Spinner spinnerPacient = viewFragment.findViewById(R.id.ExcelSpinnerPacient);
            spinnerPacient.setAdapter(adapter2);
            if(count03!=-1){spinnerPacient.setSelection(count03);};

            //v36  v38

            Cursor ccard;
            String sqlQueryCard = "select id,name from tableCardTip  order by name";
            ccard = db.rawQuery(sqlQueryCard, null);
            if (ccard.getCount()>0) {
                ccard.moveToFirst();
                do {
                    SpravochnikDCardID.add(ccard.getString(ccard.getColumnIndex("id")));
                    SpravochnikCardNAME.add(ccard.getString(ccard.getColumnIndex("name")));
                } while (ccard.moveToNext());
            }
            ccard.close();

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, SpravochnikCardNAME);//
           adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Spinner etSpinnerCard = viewFragment.findViewById(R.id.ExcelSpinnerTipCard);
            etSpinnerCard.setAdapter(adapter);



       ////////////////////////    db.close();



//v40  V41

                //нажание на кнопки вол фрагменте
            ButtonSaveNew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String DocID02= (String) ExelDocIDTextView02.getText();
                    if (DocID02.equals("-1"))
                    {
                        Toast.makeText(getActivity() , "Данные по врачу не подтвердены!", Toast.LENGTH_LONG).show();
                        return;
                    };
                    Spinner ExcelSpinnerTipCard=viewFragment.findViewById(R.id.ExcelSpinnerTipCard);
                    int pos=ExcelSpinnerTipCard.getSelectedItemPosition();
                    EditText fioPacient=viewFragment.findViewById(R.id.FragmentExcelPacientEditTextFIO);
                    EditText BirthdayPacient=viewFragment.findViewById(R.id.FragmentExcelPacientEditTextBirthday);
                    // создаем объект для данных
                    ContentValues cv = new ContentValues();
                    cv.put("fio", String.valueOf(fioPacient.getText()).toLowerCase());
                    cv.put("birthday", String.valueOf(BirthdayPacient.getText()).toLowerCase());
                    cv.put("numbercard", SpravochnikDCardID.get(pos));
                    // вставляем запись и получаем ее ID
                    ///////////////SQLiteDatabase db =DBHelper.getInstance(getContext()).getWritableDatabase();
                    long rowID = db.insert("tablePacients", null, cv);
//V43
                     //создаем объект для данных
                    ContentValues cv2 = new ContentValues();
                    cv2.put("idpacient", rowID);
                    int idCodtor=Integer.parseInt((String) ExelDocIDTextView02.getText());
                    cv2.put("iddoctor", idCodtor);
                    long rowID2 = db.insert("tableExcelRequestDay", null, cv2);

                    /////////////////db.close();

                    ButtonOk.setEnabled(false);
                    ButtonSaveNew.setEnabled(false);
                    zagruzka_day_excel_test2.points[Integer.parseInt(mParam0)]="1";

                }
            });


//V44

            ButtonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String DocID02= (String) ExelDocIDTextView02.getText();
                    if (DocID02.equals("-1"))
                    {
                        Toast.makeText(getActivity() , "Данные по врачу не подтвердены!", Toast.LENGTH_LONG).show();
                        return;
                    };
                    Spinner spinPacient=viewFragment.findViewById(R.id.ExcelSpinnerPacient);
                    int pos=spinPacient.getSelectedItemPosition();
                    int idpacient= Integer.parseInt(SpravochnikPacientIDadd.get(pos));
                    int idCodtor=Integer.parseInt((String) ExelDocIDTextView02.getText());

                    // создаем объект для данных
                    ContentValues cv = new ContentValues();
                    cv.put("idpacient", idpacient);
                    cv.put("iddoctor", idCodtor);
//V45
                    long rowID = db.insert("tableExcelRequestDay", null, cv);

                   //////////// db.close();
                    ButtonOk.setEnabled(false);
                    ButtonSaveNew.setEnabled(false);
                  ////////////////////////////////////////////////////////////////  ZagruzkaDayExcel.points[Integer.parseInt(mParam0)]="1";
                   zagruzka_day_excel_test2.points[Integer.parseInt(mParam0)]="1";
                }
            });






        }


        return viewFragment;



        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_excel_pacient, container, false);
    }
}