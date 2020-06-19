package com.kimhello.isolationtimer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    public interface OnItemMoveListener {
        //boolean onItemDrag(int from_position, int to_position);
        //void onItemDragged(int from_position, int to_position);
        void onItemSwipe(int delete_position);
    }
    private OnItemMoveListener mItemMoveListener;

    private int dragFromPosition=-1;
    private int dragToPosition=-1;

    //생성자
    public ItemTouchHelperCallback(OnItemMoveListener listener) {
        mItemMoveListener = listener;
    }

    //드래그, 스와이프 무브먼트 가져옴
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int drag_flags = ItemTouchHelper.UP|ItemTouchHelper.DOWN;
        int swipe_flags = ItemTouchHelper.START|ItemTouchHelper.END;
        return makeMovementFlags(drag_flags,swipe_flags);
    }


    //드래그, 위치 이동
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        /*dragFromPosition=viewHolder.getAdapterPosition();
        dragToPosition=target.getAdapterPosition();
        return mItemMoveListener.onItemDrag(viewHolder.getAdapterPosition(), target.getAdapterPosition());*/
        return true;
    }

    /*@Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);

        switch (actionState) {
            case ItemTouchHelper.ACTION_STATE_DRAG :
                dragFromPosition=viewHolder.getAdapterPosition();
            case ItemTouchHelper.ACTION_STATE_IDLE : {
                if (dragFromPosition!=-1&&dragToPosition!=-1&&dragFromPosition!=dragToPosition) {
                    mItemMoveListener.onItemDragged(dragFromPosition, dragToPosition);
                    dragFromPosition=-1;
                    dragToPosition=-1;
                }
            }
        }
    }*/

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    //스와이프 -> 삭제니까 어댑터 위치 가져옴
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        mItemMoveListener.onItemSwipe(viewHolder.getAdapterPosition());
    }
}
