package com.example.wallpaper.Room

import android.app.Application
import android.view.View
import android.widget.Filter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.Flowable
import io.reactivex.Maybe
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

public class View_Model(application: Application) :ViewModel(){
    lateinit var  dao: queryDao
    lateinit var executorService: ExecutorService

    init{
        dao= DB.getDatabase(application)!!.DBDao()
        executorService=Executors.newSingleThreadExecutor();

    }
    fun saveLinkEntry(data:link){
      executorService.execute { dao.saveLink(data)}
    }
    fun get_All():LiveData<List<link>>{
       return  dao.getAll()
    }
    fun DeleteOneItem(pk:Long){
        executorService.execute { dao.DeleteAnItem(pk) }
    }
    fun UpdateFavItem(pk:Long,value:Boolean)
    {
        executorService.execute { dao.UpdateAnItem(pk,value) }
    }
    fun getAllFavuorite(value: Boolean):Flowable<List<link>>{
        return dao.getFavouriteItem(value)
    }


}

public class view_model_facotry(var application: Application) :ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
       return  View_Model(application) as T
    }

}