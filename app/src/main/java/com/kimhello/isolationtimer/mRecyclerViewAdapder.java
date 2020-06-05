package com.kimhello.isolationtimer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

public class mRecyclerViewAdapder extends RecyclerView.Adapter<mRecyclerViewAdapder.MyViewHolder>
        implements ItemTouchHelperCallback.OnItemMoveListener{

    public interface OnstartDragListener {
        void onStartDrag(MyViewHolder holder);
    }

    private ArrayList<String> mDataset = new ArrayList<>();

    private final Context mContext;
    private final OnstartDragListener mStartDragListener;

    // 생성자
    public mRecyclerViewAdapder(Context context, OnstartDragListener startDragListener) {
        mStartDragListener = startDragListener;
        mContext = context;
    }

    //아이템 뷰 저장 뷰홀터 클래스
    class MyViewHolder extends RecyclerView.ViewHolder {
        RadioButton radioButton;
        ImageButton dragButton;

        public MyViewHolder(View itemView) {
            super(itemView);

            // 뷰 객체 참조
            radioButton = itemView.findViewById(R.id.rbtn_text_list);
            dragButton = itemView.findViewById(R.id.btn_drag_list);
        }

        public void onBind(String text) {
            radioButton.setText(text);
        } //데이터 셋과 라디오 버튼 텍스트에 연결
    }

    //레이아웃 인플레이터
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view);

    }

    //position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        String item = mDataset.get(position);
        holder.radioButton.setText(item);

        holder.dragButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    mStartDragListener.onStartDrag(holder);
                }
                return false;
            }
        });

    }

    //item 개수 리턴
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    //아이템 추가
    public void addItem(String text) {
        mDataset.add(text);
        notifyDataSetChanged();
    }

    @Override
    public void onItemMove(int from_position, int to_position) {
        Collections.swap(mDataset, from_position, to_position);
        //Adapter에 데이터 이동알림
        notifyItemMoved(from_position,to_position);
    }

    @Override
    public void onItemSwipe(int position) {
        mDataset.remove(position);
        notifyItemRemoved(position);
    }
}
