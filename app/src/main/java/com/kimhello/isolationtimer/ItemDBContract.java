package com.kimhello.isolationtimer;

public class ItemDBContract {
    private ItemDBContract() {}

    public static final String TABLE_ITEM = "item";
    public static final String COL_ID = "item_id"; // 아이디
    public static final String COL_TITLE = "item_title"; // 이름
    public static final String COL_TIME = "item_time"; // 활동 시간 분 단위로 저장

    public static final String SQL_CREATE_TBL = "CREATE TABLE IF NOT EXISTS "
            + TABLE_ITEM + " " + "("
            + COL_ID + " INTEGER PRIMARY KEY" + ", "
            + COL_TITLE + " CHAR(20)" + ", "
            + COL_TIME + " INTEGER" + ")";

    public static final String SQL_DROP_TBL = "DROP TABLE IF EXISTS " + TABLE_ITEM;

    public static final String SQL_SELECT_TITLE = "SELECT " + COL_TITLE + " FROM " + TABLE_ITEM;
    public static final String SQL_SELECT_TIME = "SELECT " + COL_TIME + " FROM " + TABLE_ITEM;

    public static final String SQL_INSERT = "INSERT OR REPLACE INTO " + TABLE_ITEM +" VALUES ";

    public static final String SQL_UPDATE = "UPDATE " + TABLE_ITEM + " SET " + COL_TIME + "=" ;
    public static final String SQL_WHERE = " WHERE " + COL_TITLE + "=";

    public static final String SQL_DELETED = "DELETE FROM " + TABLE_ITEM + " WHERE " + COL_TITLE + " = ";
}