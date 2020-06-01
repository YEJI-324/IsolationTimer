package com.kimhello.isolationtimer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MyViewHolder>
        implements ItemTouchHelperListener{

    private ArrayList<String> mDataset = new ArrayList<>();

    // 생성자
    public RVAdapter() {

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
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.onBind(mDataset.get(position));

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
    public boolean onItemMove(int from_position, int to_position) {
        //이동할 텍스트 저장
        String str_move = mDataset.get(from_position);
        //이동할 텍스트 삭제
        mDataset.remove(from_position);
        //이동하고 싶은 position에 추가
        mDataset.add(to_position,str_move);

        //Adapter에 데이터 이동알림
        notifyItemMoved(from_position,to_position);
        return true;

    }

    @Override
    public void onItemSwipe(int position) {
        mDataset.remove(position);
        notifyItemRemoved(position);
    }
}
