package com.kimhello.isolationtimer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

import static com.kimhello.isolationtimer.ItemDBContract.COL_ID;
import static com.kimhello.isolationtimer.ItemDBContract.COL_TITLE;
import static com.kimhello.isolationtimer.ItemDBContract.TABLE_ITEM;

public class mRecyclerViewAdapder extends RecyclerView.Adapter<mRecyclerViewAdapder.MyViewHolder>
        implements ItemTouchHelperCallback.OnItemMoveListener{

    private ArrayList<String> mDataset = new ArrayList<>();
    private DBHelpter dbHelpter;
    private final OnstartDragListener mStartDragListener;

    public interface OnstartDragListener {
        void onStartDrag(MyViewHolder holder);
    }

    // constructor
    public mRecyclerViewAdapder(ArrayList<String> ItemDataset, Context context, OnstartDragListener startDragListener) {
        this.mDataset = ItemDataset;
        mStartDragListener = startDragListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        RadioButton radioButton;
        EditText editText;
        ImageButton dragButton;

        public MyViewHolder(final View itemView) {
            super(itemView);
            dbHelpter = DBHelpter.getInstance(itemView.getContext());

            // 뷰 객체 참조
            this.radioButton = itemView.findViewById(R.id.rBtn_check_list);
            this.editText = itemView.findViewById(R.id.edit_text_list);
            this.dragButton = itemView.findViewById(R.id.btn_drag_list);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);

        return new MyViewHolder(view);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        String tmpItem = mDataset.get(position);
        holder.editText.setText(tmpItem);

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
    public boolean onItemDrag(int from_position, int to_position) {
        return false;
    }

    @Override
    public void onItemDragged(int from_position, int to_position) {
        String temp;
        for (int i =0 ; i<mDataset.size();i++) {
            temp = mDataset.get(i);
            dbHelpter.update(temp, i);
        }
        temp = mDataset.get(from_position);
        dbHelpter.update(temp, to_position);
        Log.d("swap1", "UPDATE " + TABLE_ITEM + " SET "+ COL_TITLE +" = " + temp + " WHERE " + COL_ID + " = " + (to_position) + " ");
        temp = mDataset.get(to_position);
        dbHelpter.update(temp, from_position-1);
        Log.d("swap2", "UPDATE " + TABLE_ITEM + " SET "+ COL_TITLE +" = " + temp + " WHERE " + COL_ID + " = " + (from_position) + " ");
        Collections.swap(mDataset, from_position, to_position);
        notifyItemMoved(from_position,to_position-1);
        for (int i =0 ; i<mDataset.size();i++) {
            temp = mDataset.get(i);
            dbHelpter.update(temp, i);
            Log.d("final", i + " : "+temp);
        }
    }

    @Override
    public void onItemSwipe(int position) {
        String temp = mDataset.get(position);
        mDataset.remove(position);
        notifyItemRemoved(position);
        dbHelpter.delete(temp);
    }
}
