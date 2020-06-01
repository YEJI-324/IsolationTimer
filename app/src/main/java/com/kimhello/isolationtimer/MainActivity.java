package com.kimhello.isolationtimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView rv_activityList_main; //메인에 액티비티 목록 출력
    LinearLayoutManager layoutManager; //레이아웃 매니저요
    RVAdapter rvAdapter; //이따 어댑터 클래스 만들기!!!!!!!!!!!!!!!!!!!!!!
    EditText edit_activityList_main; //액티비티 추가 입력
    Button btn_add_main; //액티비티 추가 버튼
    final ArrayList<String> ActivityListDataSet = new ArrayList<>(); //입력한 데이터 저장하는 데이터 셋

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv_activityList_main = (RecyclerView) findViewById(R.id.rv_activityList_main);
        edit_activityList_main = (EditText) findViewById(R.id.edit_list_main);
        btn_add_main = (Button) findViewById(R.id.btn_add_main);

        layoutManager = new LinearLayoutManager(this);
        rv_activityList_main.setLayoutManager(layoutManager);

        rvAdapter = new RVAdapter(); //내가 어댑터 정의 , 데이터 셋에 데이터 넣어줘야함
        rv_activityList_main.setAdapter(rvAdapter);
        btn_add_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_todo = edit_activityList_main.getText().toString(); // 에디트 텍스트에 입력한 스트링 가져오기
                rvAdapter.addItem(str_todo);
                rvAdapter.notifyDataSetChanged();
                edit_activityList_main.setText("");
            }
        });

    }
}
