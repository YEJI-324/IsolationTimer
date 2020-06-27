package com.kimhello.isolationtimer;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class LockActivity extends AppCompatActivity {
    TextView count_hour, count_min, count_sec, noti_name;
    String name_str, hour_str, min_str;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);

        count_hour = (TextView) findViewById(R.id.count_hour_lock);
        count_min = (TextView) findViewById(R.id.count_min_lock);
        count_sec = (TextView) findViewById(R.id.count_sec_lock);
        noti_name = (TextView)findViewById(R.id.noti_lock);

        Intent inIntent = getIntent();
        name_str = inIntent.getStringExtra("name_str");
        hour_str = inIntent.getStringExtra("hour_str");
        min_str = inIntent.getStringExtra("min_str");

        String conversionTime = hour_str + min_str;

        if(name_str==null) {
            Toast.makeText(getApplicationContext(), "활동을 선택해주세요.", Toast.LENGTH_SHORT).show();
        }
        else {
            countDown(conversionTime);
            noti_name.setText(name_str+" 활동 중 입니다.");
        }
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

        new CountDownTimer(conversionTime, 1000) {
            // 특정 시간마다 뷰 변경
            @Override
            public void onTick(long millisUnitlFinished) {
                // per hour
                String hour = String.valueOf(millisUnitlFinished/(60*60*1000));

                // per minute
                long getMin = millisUnitlFinished - (millisUnitlFinished/(60*60*1000));
                String min = String.valueOf(getMin / (60*1000));

                // per sec
                String sec = String.valueOf((getMin % (60*1000))/1000);

                // 시간이 한자리면 0을 붙임
                if(hour.length() == 1) {
                    hour = "0"+hour;
                }

                if(min.length()==1) {
                    min = "0" + min;
                }

                if(sec.length()==1) {
                    sec = "0" + sec;
                }

                count_hour.setText(hour);
                count_min.setText(min);
                count_sec.setText(sec);
            }

            // 제한시간 종료시
            @Override
            public void onFinish() {
                count_hour.setText("00");
                count_min.setText("00");
                count_sec.setText("00");
                Intent outIntent = new Intent(getApplicationContext(),MainActivity.class);
                setResult(RESULT_OK, outIntent);
                Toast.makeText(count_hour.getContext(), name_str +"활동이 끝났습니다", Toast.LENGTH_SHORT).show();
                finish();
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        //뒤로 가기 막기
        Toast.makeText(getApplicationContext(),count_hour.getText()+"시간 "+ count_min.getText()+"분 "+ count_sec.getText()+"초 남았습니다.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onUserLeaveHint() {
        finish();
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        Toast.makeText(getApplicationContext(),"재시작 합니다.", Toast.LENGTH_SHORT).show();
        super.onUserLeaveHint();
        return;
    }
}
