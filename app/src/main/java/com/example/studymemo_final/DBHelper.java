package com.example.studymemo_final;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "study_memo.db";

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 데이터 베이스가 생성이 될 때 호출
        // 데이터 베이스 -> 테이블 -> 컬럼 -> 값
        db.execSQL("create table if not exists StudyMemo " +
                "(id integer primary key autoincrement, title text not null, content text not null, writeDate text not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion , int newVersion) {
        onCreate(db);
    }

    // SELECT 문 (할일 목록들을 조회)
    public ArrayList<MemoItem> getStudyMemo() {
        ArrayList<MemoItem> memoItems = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from StudyMemo order by writeDate desc", null);
        if(cursor.getCount() != 0){
            // 조회한 데이터가 있을 떄 내부 수행
            while(cursor.moveToNext()){
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
                @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex("content"));
                @SuppressLint("Range") String writeDate = cursor.getString(cursor.getColumnIndex("writeDate"));

                MemoItem memoItem = new MemoItem();
                memoItem.setId(id);
                memoItem.setTitle(title);
                memoItem.setContent(content);
                memoItem.setWriteDate(writeDate);
                memoItems.add(memoItem);

            }
        }
        cursor.close();
        return memoItems;
    }

    // INSERT 문 (메모를 db에 넣는다.)
    public void insertMemo(String _title, String _content, String _writeDate){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("insert into StudyMemo (title, content, writeDate) values('" +_title + "', '" +_content+"', '"+_writeDate+"'); ");
    }

    // UPDATE 문 (수정)
    public void UpdateMemo(String _title, String _content, String _writeDate, String _beforeDate){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("update StudyMemo set title='"+_title+"', content='"+_content+"', writeDate='"+_writeDate+"' where writeDate='"+_beforeDate+"'");
    }

    // DELETE 문 (할 일 목록을 제거한다.)
    public void deleteMemo(String _beforeDate){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from StudyMemo where writeDate ='" +_beforeDate + "'");
    }
}
