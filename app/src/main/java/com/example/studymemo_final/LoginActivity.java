package com.example.studymemo_final;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText edtID;
    private EditText edtPW;
    Button btnLogin, btntoRegister;
    SQLiteDatabase sqlDB;
    MyDBHelper myHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtID = findViewById(R.id.edtID);
        edtPW = findViewById(R.id.edtPW);
        btnLogin = findViewById(R.id.btnLogin);
        btntoRegister = findViewById(R.id.btntoRegister);
        myHelper = new MyDBHelper(this);


        btntoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = edtID.getText().toString().trim();
                String userPass = edtPW.getText().toString().trim();
                sqlDB = myHelper.getReadableDatabase();
                Cursor cursor;
                cursor = sqlDB.rawQuery("SELECT userID, userPass FROM user where userID='"+userID+"' and userPass='"+userPass+"';", null);
                if(cursor.moveToFirst()){
//do {
//String id = cursor.getString(0);
// String pw = cursor.getString(1);
// } while(cursor.moveToNext());
                    Intent in = new Intent(LoginActivity.this, MemoActivity.class); //intent 메모로 수정
                    startActivity(in);
                }
                else{
//비밀번호나 id가 틀림, 로그인 실패
                    Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                }
                cursor.close();
                sqlDB.close();
            }
        });
    }
}