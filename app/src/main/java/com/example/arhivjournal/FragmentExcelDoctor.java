package com.example.arhivjournal;

import static android.content.Context.MODE_PRIVATE;

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

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentExcelDoctor#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentExcelDoctor extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;


    ArrayList<String> SpravochnikDocIDadd = new ArrayList<>();
    ArrayList<String> SpravochnikDocNAMEadd = new ArrayList<>();

    ArrayList<String> SpravochnikDocOfficeIDadd = new ArrayList<>();
    ArrayList<String> SpravochnikDocOfficeNAMEadd = new ArrayList<>();

    Integer count03=-1;
    TextView ExelDocIDTextView01;
    public FragmentExcelDoctor() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentExcelDoctor.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentExcelDoctor newInstance(String param1) {
        FragmentExcelDoctor fragment = new FragmentExcelDoctor();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExelDocIDTextView01=getActivity().findViewById(R.id.ExelDocIDTextView2);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            // подключаемся к БД
            SQLiteDatabase db =DBHelper.getInstance(getContext()).getWritableDatabase();
            //заполняем  ArrayList  для spinner
            Cursor c;
            String sqlQuery = "select tableDoctors.id as id,fio || ' (' || tableDoctorsAdress.name  ||')' as fullnameinfo from tableDoctors "+
                    " left join tableDoctorsAdress on tableDoctorsAdress.id=tableDoctors.department "+
                    " order by fio";
            int count01=0,count02=0;
            String stroka="";
            c = db.rawQuery(sqlQuery, null);
            if (c != null) {
                c.moveToFirst();
                do {
                    SpravochnikDocIDadd.add(c.getString(c.getColumnIndex("id")));
                 stroka=c.getString(c.getColumnIndex("fullnameinfo"));
                    SpravochnikDocNAMEadd.add(stroka);
                    count02=stroka.lastIndexOf(mParam1.trim());
                    if(count02!=-1){count03=count01;};
                    count01++;

                } while (c.moveToNext());
            }
            c.close();
            ////////////////////////////////////////////////////////////
            //заполняем  ArrayList  для spinner  office
            Cursor c2;
            String sqlQuery2 = "select id,name from tableDoctorsAdress";
            String stroka2="";
            c2 = db.rawQuery(sqlQuery2, null);
            if (c2 != null) {
                c2.moveToFirst();
                do {
                    SpravochnikDocOfficeIDadd.add(c2.getString(c2.getColumnIndex("id")));
                   SpravochnikDocOfficeNAMEadd.add(c2.getString(c2.getColumnIndex("name")));
                } while (c2.moveToNext());
            }
            c2.close();
            db.close();
            ////////////////////////////////////////////////////////////
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View VievFragmentExelDoctors =  inflater.inflate(R.layout.fragment_excel_doctor, container, false);
        Button ButtonOk= VievFragmentExelDoctors.findViewById(R.id.ExcelButtonSootvetstvuet);
        Button ButtonSaveNew= VievFragmentExelDoctors.findViewById(R.id.ExcelButtonSaveNew);
        if (!ExelDocIDTextView01.getText().equals("-1")){
            ButtonOk.setEnabled(false);
            ButtonSaveNew.setEnabled(false);
        };
        if (getArguments() != null) {
            EditText EditExelDoctors = VievFragmentExelDoctors.findViewById(R.id.FragmentExcelDoctorEditTextFIO);
            EditExelDoctors.setText(mParam1);
            //adapter office
            // адаптер
            ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, SpravochnikDocOfficeNAMEadd);
            adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Spinner spinnerDoctorsOfices = VievFragmentExelDoctors.findViewById(R.id.ExcelSpinnerDocOffice);
            spinnerDoctorsOfices.setAdapter(adapter3);
            // адаптер
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, SpravochnikDocNAMEadd);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Spinner spinnerDoctors = VievFragmentExelDoctors.findViewById(R.id.ExcelSpinnerDoctors);
            spinnerDoctors.setAdapter(adapter2);
            if (count03!=-1){spinnerDoctors.setSelection(count03);}



            //нажание на кнопки вол фрагменте
            ButtonSaveNew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("TAVKA", "ButtonSaveNew");
                    // создаем объект для данных
                    ContentValues cv = new ContentValues();
                    int pos=spinnerDoctorsOfices.getSelectedItemPosition();
                    EditText fioDoc=VievFragmentExelDoctors.findViewById(R.id.FragmentExcelDoctorEditTextFIO);
                    cv.put("fio", String.valueOf(fioDoc.getText()).toLowerCase());
                    cv.put("department", SpravochnikDocOfficeIDadd.get(pos));
                    // вставляем запись и получаем ее ID
                    SQLiteDatabase db =DBHelper.getInstance(getContext()).getWritableDatabase();
                    long rowID = db.insert("tableDoctors", null, cv);
                    db.close();
                       Log.d("TAVKA", "row inserted, ID = " + rowID);
                    ExelDocIDTextView01.setText(String.valueOf(rowID));
                    ButtonOk.setEnabled(false);
                    ButtonSaveNew.setEnabled(false);
                }
            });



            ButtonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 int pos=spinnerDoctors.getSelectedItemPosition();
                    String iddoc= SpravochnikDocIDadd.get(pos);
                    ExelDocIDTextView01.setText(iddoc);
                    Log.d("TAVKA", "iddoc="+iddoc);
                    ButtonOk.setEnabled(false);
                    ButtonSaveNew.setEnabled(false);
                }
            });



        }


        // Inflate the layout for this fragment
        return VievFragmentExelDoctors;
    }
}