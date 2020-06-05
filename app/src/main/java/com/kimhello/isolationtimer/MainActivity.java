package com.kimhello.isolationtimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements mRecyclerViewAdapder.OnstartDragListener{

    RecyclerView mRecyclerView; //메인에 액티비티 목록 출력
    LinearLayoutManager layoutManager; //레이아웃 매니저요
    mRecyclerViewAdapder mRecyclerViewAdapder; //이따 어댑터 클래스 만들기!!!!!!!!!!!!!!!!!!!!!!
    EditText editText; //액티비티 추가 입력
    Button addButton; //액티비티 추가 버튼
    private ArrayList<String> activityListDataSet = new ArrayList<>(); //입력한 데이터 저장하는 데이터 셋
    ItemTouchHelper mItemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_activityList_main);
        editText = (EditText) findViewById(R.id.edit_list_main);
        addButton = (Button) findViewById(R.id.btn_add_main);

        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);



        mRecyclerViewAdapder = new mRecyclerViewAdapder(this, this); //내가 어댑터 정의 , 데이터 셋에 데이터 넣어줘야함

        ItemTouchHelperCallback mCallback = new ItemTouchHelperCallback(mRecyclerViewAdapder);
        mItemTouchHelper = new ItemTouchHelper(mCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        mRecyclerView.setAdapter(mRecyclerViewAdapder);

        for (int i=0; i<20; i++) {
            mRecyclerViewAdapder.addItem(String.format("Activity %s", i));
        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_todo = editText.getText().toString(); // 에디트 텍스트에 입력한 스트링 가져오기
                mRecyclerViewAdapder.addItem(str_todo);
                mRecyclerViewAdapder.notifyDataSetChanged();
                editText.setText("");
            }
        });
    }

    @Override
    public void onStartDrag(com.kimhello.isolationtimer.mRecyclerViewAdapder.MyViewHolder holder) {
        mItemTouchHelper.startDrag(holder);
    }
}
