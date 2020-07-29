package com.example.wallpaper.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [link::class],version = 5)
abstract  class DB:RoomDatabase(){
    abstract fun DBDao():queryDao
            companion object{
        @Volatile
        private  var instance:DB?=null
        fun getDatabase(context: Context):DB?{
            if(instance==null){
                synchronized(DB::class){
                    instance=Room.databaseBuilder(context.applicationContext,DB::class.java,"Link-Db").fallbackToDestructiveMigration().build()
                }
            }
        return instance
        }

    }
}