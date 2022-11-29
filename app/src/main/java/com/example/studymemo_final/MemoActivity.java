package com.example.studymemo_final;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MemoActivity extends AppCompatActivity {
    private RecyclerView mRv_memo;
    private FloatingActionButton mBtn_write;
    private ArrayList<MemoItem> mMemoItems;
    private DBHelper mDBHelper;
    private CustomAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);
        mRv_memo = findViewById(R.id.rv_memo);
        GridLayoutManager gridLayoutManager
                = new GridLayoutManager(this,2);

        mRv_memo.setLayoutManager(gridLayoutManager);
        setInit();

    }

    private void setInit(){
        mDBHelper = new DBHelper(this);
        mRv_memo = findViewById(R.id.rv_memo);
        mBtn_write = findViewById(R.id.btn_write);
        mMemoItems = new ArrayList<>();

        loadRecentDB();

        mBtn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 팝업 창 띄우기
                Dialog dialog = new Dialog(MemoActivity.this, com.google.android.material.R.style.Theme_Material3_Light_Dialog);
                dialog.setContentView(R.layout.memo_update);
                EditText et_title = dialog.findViewById(R.id.et_title);
                EditText et_content = dialog.findViewById(R.id.et_content);
                Button btn_ok = dialog.findViewById(R.id.btn_save);
                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Insert Database
                        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                        mDBHelper.insertMemo(et_title.getText().toString(), et_content.getText().toString(), currentTime);

                        // insert ui
                        MemoItem item = new MemoItem();
                        item.setTitle(et_title.getText().toString());
                        String content = et_content.getText().toString();
                        item.setContent(content);
                        item.setWriteDate(currentTime);

                        mAdapter.addItem(item);
                        mRv_memo.smoothScrollToPosition(0);
                        dialog.dismiss();
                        Toast.makeText(MemoActivity.this, "메모를 작성했습니다 📝", Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.show();

            }
        });
    }

    private void loadRecentDB() {
        mMemoItems = mDBHelper.getStudyMemo();
        if(mAdapter == null){
            mAdapter = new CustomAdapter(mMemoItems, this);
            mRv_memo.setHasFixedSize(true);
            mRv_memo.setAdapter(mAdapter);
        }
    }

}