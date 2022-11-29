package com.example.studymemo_final;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private ArrayList<MemoItem> mMemoItems;
    private Context mContext;
    private DBHelper mDBHelper;



    public CustomAdapter(ArrayList<MemoItem> mMemoItems, Context mContext){
        this.mMemoItems = mMemoItems;
        this.mContext = mContext;
        mDBHelper = new DBHelper(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holder = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);

        return new ViewHolder(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_title.setText(mMemoItems.get(position).getTitle());
        holder.tv_content.setText(mMemoItems.get(position).getContent());
        holder.tv_writeDate.setText(mMemoItems.get(position).getWriteDate());
    }

    @Override
    public int getItemCount() {
        return mMemoItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title;
        private TextView tv_content;
        private TextView tv_writeDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_title);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_writeDate = itemView.findViewById(R.id.tv_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int curPos = getAdapterPosition(); // 현재 리스트 클릭한 아이템 위치
                    MemoItem memoItem = mMemoItems.get(curPos);

                    String[] strChoiceItems = {"수정하기", "삭제하기"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("원하는 것을 선택해주세요 🤔");
                    builder.setItems(strChoiceItems, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int position) {
                            if(position == 0){
                                // 수정하기
                                Dialog dialog = new Dialog(mContext, com.google.android.material.R.style.Theme_Material3_Light_Dialog);
                                dialog.setContentView(R.layout.memo_update);
                                EditText et_title = dialog.findViewById(R.id.et_title);
                                EditText et_content = dialog.findViewById(R.id.et_content);
                                Button btn_ok = dialog.findViewById(R.id.btn_save);

                                et_content.setText(memoItem.getContent());
                                et_title.setText(memoItem.getTitle());
                                et_title.setSelection(et_title.getText().length());
                                btn_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        //update database
                                        String title = et_title.getText().toString();
                                        String content = et_content.getText().toString();
                                        String beforeTime = memoItem.getWriteDate();
                                        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                                        mDBHelper.UpdateMemo(title, content, currentTime, beforeTime);


                                        // update ui
                                        memoItem.setTitle(title);
                                        memoItem.setContent(content);
                                        memoItem.setWriteDate(currentTime);
                                        notifyItemChanged(curPos, memoItem);
                                        dialog.dismiss();
                                        Toast.makeText(mContext, "메모가 수정되었어요 😊", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                dialog.show();

                            }

                            else if(position==1){
                                // delete table
                                String beforeTime = memoItem.getWriteDate();
                                mDBHelper.deleteMemo(beforeTime);

                                // delete ui
                                mMemoItems.remove(curPos);
                                notifyItemRemoved(curPos);
                                Toast.makeText(mContext, "목록이 삭제되었어요 😉", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    builder.show();

                }
            });
        }
    }
    // 엑티비티에서 호출되는 함수이며, 현재 어댑터에 새로운 게시글 아이템을 전달받아 추가하는 목적이다.
    public void addItem(MemoItem _item){
        mMemoItems.add(0, _item);
        notifyItemInserted(0);
    }
}
