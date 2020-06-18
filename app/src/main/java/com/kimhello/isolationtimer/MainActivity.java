package com.kimhello.isolationtimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements mRecyclerViewAdapder.OnstartDragListener{

    RecyclerView mRecyclerView; //메인에 액티비티 목록 출력
    LinearLayoutManager layoutManager; //레이아웃 매니저요
    mRecyclerViewAdapder mAdapter;
    EditText editText; //액티비티 추가 입력
    Button addButton; //액티비티 추가 버튼
    private ArrayList<String> Items = new ArrayList<>(); //입력한 데이터 저장하는 데이터 셋
    ItemTouchHelper mItemTouchHelper;
    SQLiteDatabase sqlDB;
    DBHelpter dbHelpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_activityList_main);
        editText = (EditText) findViewById(R.id.edit_list_main);
        addButton = (Button) findViewById(R.id.btn_add_main);

        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        dbHelpter = DBHelpter.getInstance(getApplicationContext());
        if(dbHelpter.getAll()!=null) { Items = dbHelpter.getAll();}

        mAdapter = new mRecyclerViewAdapder(Items, this, this); //내가 어댑터 정의 , 데이터 셋에 데이터 넣어줘야함

        ItemTouchHelperCallback mCallback = new ItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(mCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        mRecyclerView.setAdapter(mAdapter);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_item = editText.getText().toString(); // 에디트 텍스트에 입력한 스트링 가져오기
                Items.add(str_item);
                dbHelpter.insert(Items.size()-1, str_item);
                mAdapter.notifyDataSetChanged();
                editText.setText("");
            }
        });
    }

    @Override
    public void onStartDrag(com.kimhello.isolationtimer.mRecyclerViewAdapder.MyViewHolder holder) {
        mItemTouchHelper.startDrag(holder);
    }
}
