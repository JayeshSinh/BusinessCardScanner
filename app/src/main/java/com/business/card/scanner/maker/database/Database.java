package com.business.card.scanner.maker.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.business.card.scanner.maker.database.dao.Database_Dao;
import com.business.card.scanner.maker.database.dao.Group_Dao;
import com.business.card.scanner.maker.database.dao.MiddleclassDao;

public abstract class Database extends RoomDatabase {
    public static final String DB_NAME = "User.DB";
    private static Database INSTANCE;

    public abstract Database_Dao database_dao();

    public abstract Group_Dao group_dao();

    public abstract MiddleclassDao middleclassDao();

    public static Database getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, Database.class, DB_NAME).fallbackToDestructiveMigration().allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
