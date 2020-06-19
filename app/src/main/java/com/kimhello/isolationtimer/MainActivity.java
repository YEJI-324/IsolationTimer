package com.kimhello.isolationtimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements mRecyclerViewAdapder.OnstartDragListener{

    RecyclerView mRecyclerView;
    LinearLayoutManager layoutManager;
    mRecyclerViewAdapder mAdapter;
    EditText add_item ,count_hour, count_min;
    Button addButton;
    ToggleButton startButton;
    private ArrayList<String> Items = new ArrayList<>();
    ItemTouchHelper mItemTouchHelper;
    SQLiteDatabase sqlDB;
    DBHelpter dbHelpter;

    private String conversionTime; // 타이머 돌릴 총 시간
    public CountDownTimer mCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // recyclerview
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_itemList_main);
        add_item = (EditText) findViewById(R.id.edit_list_main);
        addButton = (Button) findViewById(R.id.btn_add_main);
        // timer
        count_hour = (EditText) findViewById(R.id.edit_hour_main);
        count_min = (EditText) findViewById(R.id.edit_min_main);
        startButton = (ToggleButton) findViewById(R.id.btn_start_main);

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
                Items.add(str_item);
                dbHelpter.insert(Items.size()-1, str_item);
                mAdapter.notifyDataSetChanged();
                add_item.setText("");
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startButton.isChecked()) {
                    conversionTime = count_hour.getText().toString() + count_min.getText().toString();
                    countDown(conversionTime);
                    countStart();
                    count_hour.clearFocus();
                    count_min.clearFocus();
                    count_hour.setFocusableInTouchMode(false);
                    count_min.setFocusableInTouchMode(false);
                    startButton.setText("RESET");
                }
                else {
                    countReset();
                    startButton.setText("START");
                }
            }
        });
    }

    @Override
    public void onStartDrag(com.kimhello.isolationtimer.mRecyclerViewAdapder.MyViewHolder holder) {
        mItemTouchHelper.startDrag(holder);
    }

    public void countDown(String time) {
        long conversionTime = 0;

        String getHour = time.substring(0,2);
        String getMin = time.substring(2,4);

        if(getHour.substring(0,1) == "0") {
            getHour = getHour.substring(1,2);
        }

        if (getMin.substring(0,1)=="0") {
            getMin=getMin.substring(1,2);
        }

        //time conversion
        conversionTime = Long.valueOf(getHour) * 1000 * 3600 + Long.valueOf(getMin) * 60 *1000;

        mCountDownTimer = new CountDownTimer(conversionTime, 1000) {
            // 특정 시간마다 뷰 변경
            @Override
            public void onTick(long millisUnitlFinished) {
                // per hour
                String hour = String.valueOf(millisUnitlFinished/(60*60*1000));

                // per minute
                long getMin = millisUnitlFinished - (millisUnitlFinished/(60*60*1000));
                String min = String.valueOf(getMin / (60*1000));

                // 시간이 한자리면 0을 붙임
                if(hour.length() == 1) {
                    hour = "0"+hour;
                }

                if(min.length()==1) {
                    min = "0" + min;
                }

                count_hour.setText(hour);
                count_min.setText(min);
            }

            // 제한시간 종료시
            @Override
            public void onFinish() {
                count_hour.setText("00");
                count_min.setText("00");
                startButton.setText("START");
                count_hour.setFocusableInTouchMode(true);
                count_min.setFocusableInTouchMode(true);
                Toast.makeText(count_hour.getContext(), "00활동 시간 끝", Toast.LENGTH_SHORT).show();
            }
        };
    }

    public void countStart() {
        mCountDownTimer.start();
    }

    public void countReset() {
        mCountDownTimer.cancel();
    }
}
