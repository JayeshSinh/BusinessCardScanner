package com.business.card.scanner.maker.database.dao;

import android.annotation.SuppressLint;

import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteStatement;


import com.business.card.scanner.maker.model.MiddleClass;

@SuppressLint("RestrictedApi")
public final class MiddleclassDao_Impl implements MiddleclassDao {
    private final RoomDatabase __db;
    private final EntityDeletionOrUpdateAdapter __deletionAdapterOfMiddleClass;
    private final EntityInsertionAdapter __insertionAdapterOfMiddleClass;
    private final EntityDeletionOrUpdateAdapter __updateAdapterOfMiddleClass;


    public MiddleclassDao_Impl(RoomDatabase roomDatabase) {
        this.__db = roomDatabase;
        this.__insertionAdapterOfMiddleClass = new EntityInsertionAdapter<MiddleClass>(roomDatabase) {
            @Override
            protected void bind(SupportSQLiteStatement supportSQLiteStatement, MiddleClass middleClass) {
                if (middleClass.getId() == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, middleClass.getId());
                }
                if (middleClass.getGroup_id() == null) {
                    supportSQLiteStatement.bindNull(2);
                } else {
                    supportSQLiteStatement.bindString(2, middleClass.getGroup_id());
                }
            }

            public String createQuery() {
                return "INSERT OR ABORT INTO `MiddleClass`(`id`,`group_id`) VALUES (?,?)";
            }


        };
        this.__deletionAdapterOfMiddleClass = new EntityDeletionOrUpdateAdapter<MiddleClass>(roomDatabase) {
            public String createQuery() {
                return "DELETE FROM `MiddleClass` WHERE `id` = ? AND `group_id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, MiddleClass middleClass) {
                if (middleClass.getId() == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, middleClass.getId());
                }
                if (middleClass.getGroup_id() == null) {
                    supportSQLiteStatement.bindNull(2);
                } else {
                    supportSQLiteStatement.bindString(2, middleClass.getGroup_id());
                }
            }
        };
        this.__updateAdapterOfMiddleClass = new EntityDeletionOrUpdateAdapter<MiddleClass>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `MiddleClass` SET `id` = ?,`group_id` = ? WHERE `id` = ? AND `group_id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, MiddleClass middleClass) {
                if (middleClass.getId() == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, middleClass.getId());
                }
                if (middleClass.getGroup_id() == null) {
                    supportSQLiteStatement.bindNull(2);
                } else {
                    supportSQLiteStatement.bindString(2, middleClass.getGroup_id());
                }
                if (middleClass.getId() == null) {
                    supportSQLiteStatement.bindNull(3);
                } else {
                    supportSQLiteStatement.bindString(3, middleClass.getId());
                }
                if (middleClass.getGroup_id() == null) {
                    supportSQLiteStatement.bindNull(4);
                } else {
                    supportSQLiteStatement.bindString(4, middleClass.getGroup_id());
                }
            }
        };
    }

    public void insertMiddle(MiddleClass middleClass) {
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfMiddleClass.insert(middleClass);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public void deleteMiddleClass(MiddleClass middleClass) {
        this.__db.beginTransaction();
        try {
            this.__deletionAdapterOfMiddleClass.handle(middleClass);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public void updateMiddleClass(MiddleClass middleClass) {
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfMiddleClass.handle(middleClass);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }
}
