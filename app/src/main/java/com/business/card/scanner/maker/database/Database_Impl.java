package com.business.card.scanner.maker.database;

import android.annotation.SuppressLint;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomMasterTable;
import androidx.room.RoomOpenHelper;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.business.card.scanner.maker.database.dao.MiddleclassDao_Impl;
import com.business.card.scanner.maker.database.dao.Database_Dao;
import com.business.card.scanner.maker.database.dao.Database_Dao_Impl;
import com.business.card.scanner.maker.database.dao.Group_Dao;
import com.business.card.scanner.maker.database.dao.Group_Dao_Impl;
import com.business.card.scanner.maker.database.dao.MiddleclassDao;
import com.google.android.gms.measurement.api.AppMeasurementSdk;

import java.util.HashMap;
import java.util.HashSet;

public final class Database_Impl extends Database {
    private volatile Database_Dao _databaseDao;
    private volatile Group_Dao _groupDao;
    private volatile MiddleclassDao _middleclassDao;

    @SuppressLint("RestrictedApi")
    public SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration databaseConfiguration) {
        return databaseConfiguration.sqliteOpenHelperFactory.create(SupportSQLiteOpenHelper.Configuration.builder(databaseConfiguration.context).name(databaseConfiguration.name).callback(new RoomOpenHelper(databaseConfiguration, new RoomOpenHelper.Delegate(1) {
            public void createAllTables( SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `Business_Card` (`id` TEXT NOT NULL, `group_id` TEXT, `name` TEXT, `job_title` TEXT, `company` TEXT, `phone` TEXT, `email` TEXT, `website` TEXT, `address` TEXT, `image_name` TEXT, `date` INTEGER, `fav` TEXT, `note` TEXT, PRIMARY KEY(`id`))");
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `Group_tb` (`group_id` TEXT NOT NULL, `group_name` TEXT, `defult` TEXT, PRIMARY KEY(`group_id`))");
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `MiddleClass` (`id` TEXT NOT NULL, `group_id` TEXT NOT NULL, PRIMARY KEY(`id`, `group_id`))");
                supportSQLiteDatabase.execSQL(RoomMasterTable.CREATE_QUERY);
                supportSQLiteDatabase.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"e38b4d9d4f9f07efe0ad884fd1fa925a\")");
            }

            public void dropAllTables(SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `Business_Card`");
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `Group_tb`");
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `MiddleClass`");
            }


            public void onCreate(SupportSQLiteDatabase supportSQLiteDatabase) {
                if (Database_Impl.this.mCallbacks != null) {
                    int size = Database_Impl.this.mCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        ((RoomDatabase.Callback) Database_Impl.this.mCallbacks.get(i)).onCreate(supportSQLiteDatabase);
                    }
                }
            }

            public void onOpen(SupportSQLiteDatabase supportSQLiteDatabase) {
                SupportSQLiteDatabase unused = Database_Impl.this.mDatabase = supportSQLiteDatabase;
                Database_Impl.this.internalInitInvalidationTracker(supportSQLiteDatabase);
                if (Database_Impl.this.mCallbacks != null) {
                    int size = Database_Impl.this.mCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        ((RoomDatabase.Callback) Database_Impl.this.mCallbacks.get(i)).onOpen(supportSQLiteDatabase);
                    }
                }
            }


            public void validateMigration(SupportSQLiteDatabase supportSQLiteDatabase) {
                HashMap hashMap = new HashMap(13);
                hashMap.put("id", new TableInfo.Column("id", "TEXT", true, 1));
                hashMap.put("group_id", new TableInfo.Column("group_id", "TEXT", false, 0));
                hashMap.put(AppMeasurementSdk.ConditionalUserProperty.NAME, new TableInfo.Column(AppMeasurementSdk.ConditionalUserProperty.NAME, "TEXT", false, 0));
                hashMap.put("job_title", new TableInfo.Column("job_title", "TEXT", false, 0));
                hashMap.put("company", new TableInfo.Column("company", "TEXT", false, 0));
                hashMap.put("phone", new TableInfo.Column("phone", "TEXT", false, 0));
                hashMap.put("email", new TableInfo.Column("email", "TEXT", false, 0));
                hashMap.put("website", new TableInfo.Column("website", "TEXT", false, 0));
                hashMap.put("address", new TableInfo.Column("address", "TEXT", false, 0));
                hashMap.put("image_name", new TableInfo.Column("image_name", "TEXT", false, 0));
                hashMap.put("date", new TableInfo.Column("date", "INTEGER", false, 0));
                hashMap.put("fav", new TableInfo.Column("fav", "TEXT", false, 0));
                hashMap.put("note", new TableInfo.Column("note", "TEXT", false, 0));
                TableInfo tableInfo = new TableInfo("Business_Card", hashMap, new HashSet(0), new HashSet(0));
                TableInfo read = TableInfo.read(supportSQLiteDatabase, "Business_Card");
                if (tableInfo.equals(read)) {
                    HashMap hashMap2 = new HashMap(3);
                    hashMap2.put("group_id", new TableInfo.Column("group_id", "TEXT", true, 1));
                    hashMap2.put("group_name", new TableInfo.Column("group_name", "TEXT", false, 0));
                    hashMap2.put("defult", new TableInfo.Column("defult", "TEXT", false, 0));
                    TableInfo tableInfo2 = new TableInfo("Group_tb", hashMap2, new HashSet(0), new HashSet(0));
                    TableInfo read2 = TableInfo.read(supportSQLiteDatabase, "Group_tb");
                    if (tableInfo2.equals(read2)) {
                        HashMap hashMap3 = new HashMap(2);
                        hashMap3.put("id", new TableInfo.Column("id", "TEXT", true, 1));
                        hashMap3.put("group_id", new TableInfo.Column("group_id", "TEXT", true, 2));
                        TableInfo tableInfo3 = new TableInfo("MiddleClass", hashMap3, new HashSet(0), new HashSet(0));
                        TableInfo read3 = TableInfo.read(supportSQLiteDatabase, "MiddleClass");
                        if (!tableInfo3.equals(read3)) {
                            throw new IllegalStateException("Migration didn't properly handle MiddleClass(com.fittech.bizcardscanner.model.MiddleClass).\n Expected:\n" + tableInfo3 + "\n Found:\n" + read3);
                        }
                        return;
                    }
                    throw new IllegalStateException("Migration didn't properly handle Group_tb(com.fittech.bizcardscanner.model.Group_tb).\n Expected:\n" + tableInfo2 + "\n Found:\n" + read2);
                }
                throw new IllegalStateException("Migration didn't properly handle Business_Card(com.fittech.bizcardscanner.model.Business_Card).\n Expected:\n" + tableInfo + "\n Found:\n" + read);
            }
        }, "e38b4d9d4f9f07efe0ad884fd1fa925a", "0ab56b64e07a66ebe124d5b845a2d904")).build());
    }


    public InvalidationTracker createInvalidationTracker() {
        return new InvalidationTracker(this, "Business_Card", "Group_tb", "MiddleClass");
    }

    public void clearAllTables() {
        super.assertNotMainThread();
        SupportSQLiteDatabase writableDatabase = super.getOpenHelper().getWritableDatabase();
        try {
            super.beginTransaction();
            writableDatabase.execSQL("DELETE FROM `Business_Card`");
            writableDatabase.execSQL("DELETE FROM `Group_tb`");
            writableDatabase.execSQL("DELETE FROM `MiddleClass`");
            super.setTransactionSuccessful();
        } finally {
            super.endTransaction();
            writableDatabase.query("PRAGMA wal_checkpoint(FULL)").close();
            if (!writableDatabase.inTransaction()) {
                writableDatabase.execSQL("VACUUM");
            }
        }
    }

    public Database_Dao database_dao() {
        Database_Dao database_Dao;
        if (this._databaseDao != null) {
            return this._databaseDao;
        }
        synchronized (this) {
            if (this._databaseDao == null) {
                this._databaseDao = new Database_Dao_Impl(this);
            }
            database_Dao = this._databaseDao;
        }
        return database_Dao;
    }

    public Group_Dao group_dao() {
        Group_Dao group_Dao;
        if (this._groupDao != null) {
            return this._groupDao;
        }
        synchronized (this) {
            if (this._groupDao == null) {
                this._groupDao = new Group_Dao_Impl(this);
            }
            group_Dao = this._groupDao;
        }
        return group_Dao;
    }

    public MiddleclassDao middleclassDao() {
        MiddleclassDao middleclassDao;
        if (this._middleclassDao != null) {
            return this._middleclassDao;
        }
        synchronized (this) {
            if (this._middleclassDao == null) {
                this._middleclassDao = new MiddleclassDao_Impl(this);
            }
            middleclassDao = this._middleclassDao;
        }
        return middleclassDao;
    }
}
