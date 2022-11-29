package com.example.studymemo_final;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
//
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.toolbox.Volley;


public class RegisterActivity extends AppCompatActivity {

    private EditText edtsignID, edtsignPW;
    Button btnSignUp;
    SQLiteDatabase sqlDB;
    MyDBHelper myHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtsignID = findViewById(R.id.edtsignID);
        edtsignPW = findViewById(R.id.edtsignPW);
        btnSignUp = findViewById(R.id.btnSignUP);
        myHelper = new MyDBHelper(this);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = edtsignID.getText().toString().trim();
                String userPass = edtsignPW.getText().toString().trim();
                try {
                    if (!userID.isEmpty() && !userPass.isEmpty()) {
                        sqlDB = myHelper.getWritableDatabase();
                        sqlDB.execSQL("INSERT INTO user(userID, userPass) VALUES ( '" + userID + "', " + "'" + userPass + "');"); //catch
                        sqlDB.close();
                        Toast.makeText(getApplicationContext(), "가입 성공", Toast.LENGTH_SHORT).show();
//Dialog로 확인후 이동되게 처리
                        Intent in = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(in);
                    } else
                        Toast.makeText(getApplicationContext(), "ID, PW 입력 확인", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "가입 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
