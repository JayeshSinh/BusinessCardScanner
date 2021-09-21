package com.business.card.scanner.maker.database.dao;

import android.annotation.SuppressLint;
import android.database.Cursor;

import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteStatement;


import com.business.card.scanner.maker.model.Group_tab;
import com.business.card.scanner.maker.model.Group_tb;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("RestrictedApi")
public final class Group_Dao_Impl implements Group_Dao {
    private final RoomDatabase __db;
    private final EntityDeletionOrUpdateAdapter __deletionAdapterOfGroup_tb;
    private final EntityInsertionAdapter __insertionAdapterOfGroup_tb;
    private final EntityDeletionOrUpdateAdapter __updateAdapterOfGroup_tb;

    public Group_Dao_Impl(RoomDatabase roomDatabase) {
        this.__db = roomDatabase;
        this.__insertionAdapterOfGroup_tb = new EntityInsertionAdapter<Group_tb>(roomDatabase) {
            public String createQuery() {
                return "INSERT OR ABORT INTO `Group_tb`(`group_id`,`group_name`,`defult`) VALUES (?,?,?)";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Group_tb group_tb) {
                if (group_tb.getGroup_id() == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, group_tb.getGroup_id());
                }
                if (group_tb.getGroup_name() == null) {
                    supportSQLiteStatement.bindNull(2);
                } else {
                    supportSQLiteStatement.bindString(2, group_tb.getGroup_name());
                }
                if (group_tb.getDefultcard() == null) {
                    supportSQLiteStatement.bindNull(3);
                } else {
                    supportSQLiteStatement.bindString(3, group_tb.getDefultcard());
                }
            }
        };
        this.__deletionAdapterOfGroup_tb = new EntityDeletionOrUpdateAdapter<Group_tb>(roomDatabase) {
            public String createQuery() {
                return "DELETE FROM `Group_tb` WHERE `group_id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Group_tb group_tb) {
                if (group_tb.getGroup_id() == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, group_tb.getGroup_id());
                }
            }
        };
        this.__updateAdapterOfGroup_tb = new EntityDeletionOrUpdateAdapter<Group_tb>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Group_tb` SET `group_id` = ?,`group_name` = ?,`defult` = ? WHERE `group_id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Group_tb group_tb) {
                if (group_tb.getGroup_id() == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, group_tb.getGroup_id());
                }
                if (group_tb.getGroup_name() == null) {
                    supportSQLiteStatement.bindNull(2);
                } else {
                    supportSQLiteStatement.bindString(2, group_tb.getGroup_name());
                }
                if (group_tb.getDefultcard() == null) {
                    supportSQLiteStatement.bindNull(3);
                } else {
                    supportSQLiteStatement.bindString(3, group_tb.getDefultcard());
                }
                if (group_tb.getGroup_id() == null) {
                    supportSQLiteStatement.bindNull(4);
                } else {
                    supportSQLiteStatement.bindString(4, group_tb.getGroup_id());
                }
            }
        };
    }

    public void insertGroup(Group_tb group_tb) {
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfGroup_tb.insert(group_tb);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public void deleteGroup(Group_tb group_tb) {
        this.__db.beginTransaction();
        try {
            this.__deletionAdapterOfGroup_tb.handle(group_tb);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public void updateGroup(Group_tb group_tb) {
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfGroup_tb.handle(group_tb);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public List<Group_tab> get_AllGroup() {
        Group_tb group_tb;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT Group_tb. * ,count(Business_Card.group_id) count FROM Group_tb LEFT JOIN Business_Card on Business_Card.group_id = Group_tb.group_id GROUP By Group_tb.group_id", 0);
        Cursor query = this.__db.query(acquire);
        try {
            int columnIndexOrThrow = query.getColumnIndexOrThrow("group_id");
            int columnIndexOrThrow2 = query.getColumnIndexOrThrow("group_name");
            int columnIndexOrThrow3 = query.getColumnIndexOrThrow("defult");
            int columnIndexOrThrow4 = query.getColumnIndexOrThrow("count");
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                if (query.isNull(columnIndexOrThrow) && query.isNull(columnIndexOrThrow2)) {
                    if (query.isNull(columnIndexOrThrow3)) {
                        group_tb = null;
                        Group_tab group_tab = new Group_tab();
                        group_tab.setCount(query.getInt(columnIndexOrThrow4));
                        group_tab.setGroup_tb(group_tb);
                        arrayList.add(group_tab);
                    }
                }
                group_tb = new Group_tb();
                group_tb.setGroup_id(query.getString(columnIndexOrThrow));
                group_tb.setGroup_name(query.getString(columnIndexOrThrow2));
                group_tb.setDefultcard(query.getString(columnIndexOrThrow3));
                Group_tab group_tab2 = new Group_tab();
                group_tab2.setCount(query.getInt(columnIndexOrThrow4));
                group_tab2.setGroup_tb(group_tb);
                arrayList.add(group_tab2);
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }
}
