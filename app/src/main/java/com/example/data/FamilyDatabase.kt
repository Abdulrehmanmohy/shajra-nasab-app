package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FamilyMember::class], version = 1, exportSchema = false)
abstract class FamilyDatabase : RoomDatabase() {
    abstract fun familyMemberDao(): FamilyMemberDao

    companion object {
        @Volatile
        private var INSTANCE: FamilyDatabase? = null

        fun getDatabase(context: Context): FamilyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FamilyDatabase::class.java,
                    "shajra_nasab_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
