package com.example.wallpaper.Room

import androidx.lifecycle.LiveData
import androidx.room.*
import io.reactivex.Flowable
import java.util.*

@Entity(tableName = "linkStore")
data class link(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pk")
    var id:Long=0,
    @ColumnInfo(name="title")
    var title:String="",
    @ColumnInfo(name = "Link")
    var link:String="",
    @ColumnInfo(name="date")
    var date:Long= Calendar.getInstance().timeInMillis,
    @ColumnInfo(name = "Favourites")
    var favourite:Boolean=false
)


@Dao
interface  queryDao{
    @Insert
    fun saveLink(Entity:link)

    @Query("select * from linkStore")
    fun getAll():Flowable<List<link>>

    @Query("DELETE from linkStore where pk=(:pk)")
    fun DeleteAnItem(pk:Long)


    @Query("update linkStore set Favourites=(:value) where pk=(:pk)")
    fun UpdateAnItem(pk:Long,value:Boolean)

    @Query("select * from linkStore where Favourites=:value")
    fun getFavouriteItem(value: Boolean):Flowable<List<link>>

}