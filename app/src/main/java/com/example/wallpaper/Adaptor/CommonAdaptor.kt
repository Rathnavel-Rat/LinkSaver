package com.example.wallpaper.Adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import java.util.*


class CommonAdaptor<T>(private val layoutId:Int, var listItems:List<T>, private val bindItem: (T, View) -> Unit, val hasStableIds:Boolean=false,val filterObject:Filter):RecyclerView.Adapter<CommonAdaptor.CviewHolder<T>>(),Filterable {
    var item:List<T> = listItems
    var filtered_item:List<T> = listItems

    init{
        setHasStableIds(hasStableIds)
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CviewHolder<T> {
       val view=LayoutInflater.from(parent.context).inflate(layoutId,parent,false)
        return CviewHolder<T>(
            view
        )
    }

    override fun getItemCount(): Int {
       return item.size

    }



    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {

        return if(hasStableIds) position.toLong() else super.getItemId(position)}




     override fun onBindViewHolder(holder: CviewHolder<T>, position: Int) {
          bindItem(item[position],holder.itemView)
    }



    class CviewHolder<T>(view: View):RecyclerView.ViewHolder(view){

    }

    override fun getFilter(): Filter {
        return filterObject
    }


}