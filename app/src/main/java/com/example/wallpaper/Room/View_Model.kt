package com.example.wallpaper.Room

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
    fun getFavourite():LiveData<List<link>> {
        return dao.getFavourited(i = true)
    }



}

public class view_model_facotry(var application: Application) :ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
       return  View_Model(application) as T
    }

}