package com.kimhello.isolationtimer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements mRecyclerViewAdapder.OnstartDragListener{

    RecyclerView mRecyclerView;
    LinearLayoutManager layoutManager;
    mRecyclerViewAdapder mAdapter;
    EditText add_item ,count_hour, count_min;
    TextView tv_timer;
    Button addButton, startButton, chartButton;
    private ArrayList<String> Items = new ArrayList<>();
    ItemTouchHelper mItemTouchHelper;
    SQLiteDatabase sqlDB;
    DBHelpter dbHelpter;

    String fileName = "timer_saved";
    String activity_name;
    int activity_index;

    private String conversionTime; // 타이머 돌릴 총 시간

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);

        // recyclerview
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_itemList_main);
        add_item = (EditText) findViewById(R.id.edit_list_main);
        addButton = (Button) findViewById(R.id.btn_add_main);
        // timer
        count_hour = (EditText) findViewById(R.id.edit_hour_main);
        tv_timer = (TextView) findViewById(R.id.tv_main);
        count_min = (EditText) findViewById(R.id.edit_min_main);
        startButton = (Button) findViewById(R.id.btn_start_main);
        // graph
        chartButton = (Button) findViewById(R.id.btn_chart_main);

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
                String str_item = add_item.getText().toString(); // 에디트 텍스트에 입력한 스트링 가져오기
                Log.d("item", "str_item = " +"."+str_item+".");
                if(str_item.length()==0) {
                    Toast.makeText(getApplicationContext(), "활동을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    add_item.clearFocus();
                }
                else{
                    Items.add(str_item);
                    dbHelpter.insert(Items.size() - 1, str_item);
                    mAdapter.notifyDataSetChanged();
                    add_item.setText("");
                    add_item.clearFocus();
                    Toast.makeText(getApplicationContext(), "'"+str_item +"'"+ " 활동이 추가되었습니다.", Toast.LENGTH_SHORT).show();
                    Log.d("dbinsert", "check update " + ItemDBContract.SQL_INSERT + "(" + (Items.size() - 1) + ", " + '"' + str_item + '"' + ", " + 0 + ")");
                }
            }
        });

        //set timer
        String init_timer =readTimer(fileName);
        count_hour.setText(init_timer.substring(0,2));
        count_min.setText(init_timer.substring(2,4));

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity_name = mAdapter.getFocusedName();
                activity_index = mAdapter.getFocusedIndex();
                //넘겨줄 값 : 액티비티 이름, 시 분 초 스트링
                if(activity_name==null) {
                    Toast.makeText(getApplicationContext(), "활동을 선택해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    try { //timer save
                        FileOutputStream outFs = openFileOutput(fileName, Context.MODE_PRIVATE);
                        String str = count_hour.getText().toString()+count_min.getText().toString();
                        outFs.write(str.getBytes());
                        outFs.close();
                        count_hour.setText(str.substring(0,2));
                        count_min.setText(str.substring(2,4));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(getApplicationContext(), LockActivity.class);
                    intent.putExtra("name_str", activity_name);
                    intent.putExtra("hour_str", count_hour.getText().toString());
                    intent.putExtra("min_str", count_min.getText().toString());
                    intent.putExtra("index", activity_index);
                    startActivityForResult(intent,0);
                }
            }
        });

        chartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PieChartActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            readTimer(fileName);
    }

    @Override
    public void onStartDrag(com.kimhello.isolationtimer.mRecyclerViewAdapder.MyViewHolder holder) {
        mItemTouchHelper.startDrag(holder);
    }

    String readTimer(String fName) {
        String timerStr = null;
        String defaultStr = "0000";
        FileInputStream inFs;
        try {
            inFs = openFileInput(fName);
            byte[] txt = new byte[500];
            inFs.read(txt);
            inFs.close();
            timerStr = (new String(txt)).trim();
        } catch (FileNotFoundException e) {
            return defaultStr;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return timerStr;
    }
}
