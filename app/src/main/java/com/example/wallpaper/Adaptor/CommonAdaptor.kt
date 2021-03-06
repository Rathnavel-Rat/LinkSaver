package com.example.wallpaper.Adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.ArrayList


class CommonAdaptor<T>(private val layoutId:Int, var listItems:ArrayList<T>, private val bindItem: (T, View) -> Unit, val hasStableIds:Boolean=false,val filterObject:Filter):RecyclerView.Adapter<CommonAdaptor.CviewHolder<T>>(),Filterable {
    private var item:ArrayList<T> = listItems

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
    fun removeAt(position: Int):T{
        val get=item.get(position)

        item.remove(get)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position,item.size)
        return get
    }
    fun add(element: T, layoutPosition: Int){
        item.add(layoutPosition,element)
        notifyItemInserted(layoutPosition)
        notifyItemRangeChanged(layoutPosition,item.size)
        notifyDataSetChanged()
    }


    fun setData(model: List<T>){
        val diffResult = DiffUtil.calculateDiff(DiffCallback(item,model))
        item.clear()
        item.addAll(model)
        diffResult.dispatchUpdatesTo(this)
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