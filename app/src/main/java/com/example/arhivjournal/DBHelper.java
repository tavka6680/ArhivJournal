package com.example.arhivjournal;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    /////////для вразментов
    private static DBHelper instance;



    public static synchronized DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context.getApplicationContext());
        }
        return instance;
    }
//////////////////////

    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, "ArchivJournalDB", null, 5);  //dbversion
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // создаем таблицу с полями
        db.execSQL("create table tableDoctors ("
                + "id integer primary key autoincrement,"
                + "fio text,"
                + "department integer"//  + "department text"  //ВМЕСТО ЭТОГО БУДЕТ АДРЕС ОТДЕЛЕНИЯ КОЛОМЕНСКАЯ ИЛИ ВЗЛЕТНАЯ  //version 3
                + ");");

        db.execSQL("create table tablePacients ("
                + "id integer primary key autoincrement,"
                + "numbercard integer,"  //ВМЕСТО ЭТОГО БУДЕТ ТИП КАРТЫ
                + "fio text,"
                + "birthday text,"
                + "boxnumber integer"
                + ");");

        db.execSQL("create table tableJournals ("
                + "id integer primary key autoincrement,"
                + "idpacient integer,"
                + "iddoctor integer,"
                + "notfound integer,"
                + "dateoperation text"
                + ");");

        db.execSQL("create table tableArchives ("
                + "id integer primary key autoincrement,"
                + "idpacient integer,"
                + "numbercard integer,"
                + "fiopacient text,"
                + "birthday text,"
                + "boxnumber integer,"
                + "iddoctor integer,"
                + "fiodoctor text,"
                + "department text,"
                + "operation text,"
                + "dateoperation text,"
                + "user text"
                + ");");

        db.execSQL("create table tableDoctorsAdress ("          //version 2
                + "id integer primary key autoincrement,"
                + "name text"
                + ");");

        db.execSQL("create table tableCardTip ("                //version 2
                + "id integer primary key autoincrement,"
                + "name text"
                + ");");


        db.execSQL("create table tableExcelRequestDay ("
                + "id integer primary key autoincrement,"
                + "idpacient integer,"
                + "iddoctor integer,"
                + "saved Boolean"
                + ");");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      /*  if (oldVersion == 1 && newVersion == 2) {
            db.beginTransaction();
            try {
                db.execSQL("create table tableDoctorsAdress ("
                        + "id integer primary key autoincrement,"
                        + "name text"
                        + ");");

                db.execSQL("create table tableCardTip ("
                        + "id integer primary key autoincrement,"
                        + "name text"
                        + ");");
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }*/
/*
  if (oldVersion == 2 && newVersion == 3) {
            db.beginTransaction();
            try {
                db.execSQL("drop table tableDoctors;");
                db.execSQL("create table tableDoctors ("
                        + "id integer primary key autoincrement,"
                        + "fio text,"
                        + "department integer"
                        + ");");
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
*/


        if (oldVersion == 3 && newVersion == 4) {
            db.beginTransaction();
            try {

                db.execSQL("create table tableExcelRequestDay ("
                        + "id integer primary key autoincrement,"
                        + "idpacient integer,"
                        + "iddoctor integer,"
                        + "saved Boolean"
                        + ");");
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }

        if (oldVersion == 4 && newVersion == 5) {
            db.beginTransaction();
            try {
                db.execSQL("alter table tableJournals add column notfound integer;");
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }



    }
}
