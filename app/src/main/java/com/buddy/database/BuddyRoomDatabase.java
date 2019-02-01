package com.buddy.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import com.buddy.dao.TaskDao;
import com.buddy.entity.Task;

@Database(entities = {Task.class}, version = 2)
public abstract class BuddyRoomDatabase extends RoomDatabase {

    public abstract TaskDao taskDao();

    private static volatile BuddyRoomDatabase INSTANCE;

    private static final String DATABASE_NAME = "buddy_database";

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Implement changes when migrating data to another version
        }
    };

    public static BuddyRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BuddyRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            BuddyRoomDatabase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}
