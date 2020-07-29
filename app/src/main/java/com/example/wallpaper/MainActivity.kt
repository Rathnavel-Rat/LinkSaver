package com.example.wallpaper



import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.URLUtil
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wallpaper.Adaptor.CommonAdaptor
import com.example.wallpaper.Adaptor.SwipeToDeleteCallback
import com.example.wallpaper.Room.View_Model
import com.example.wallpaper.Room.link
import com.example.wallpaper.Room.view_model_facotry
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


var date = Comparator<link> { hotel1, hotel2 ->
    if (hotel1.date.compareTo(hotel2.date) == 0) {
        hotel1.date.compareTo(hotel2.date)
    } else {
        hotel1.date.compareTo(hotel2.date )
    }

}
var date1 = Comparator<link> { hotel1, hotel2 ->
    if (hotel2.date.compareTo(hotel1.date) == 0) {
        hotel2.date.compareTo(hotel1.date)
    } else {
        hotel2.date.compareTo(hotel1.date)
    }
}
class MainActivity : AppCompatActivity() {
    lateinit var viewModel: View_Model
    lateinit var popupView: Dialog
    lateinit var recyclerView: RecyclerView
    var copy = emptyList<link>()


        lateinit var recyclerAdaptor: CommonAdaptor<link>



    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val add = findViewById<FloatingActionButton>(R.id.add)
        viewModel = ViewModelProvider(
            this,
            view_model_facotry(this.application)
        ).get(View_Model::class.java)
        recyclerView = findViewById<RecyclerView>(R.id.RecyclerHome)

        var card = findViewById<CardView>(R.id.card)
        recyclerView.itemAnimator = null
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        setpopup()
        when (intent?.action) {
            Intent.ACTION_SEND -> {
                if ("text/plain" == intent.type) {
                    if (URLUtil.isValidUrl(intent.getStringExtra(Intent.EXTRA_TEXT))) {

                        show(intent.getStringExtra(Intent.EXTRA_TEXT))
                    } else {
                        Toast.makeText(this, "Not an Url ", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }


        val swipe = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val id = viewHolder.itemView.findViewById<TextView>(R.id.idSetter)
                viewModel.DeleteOneItem(id.text.toString().toLong())
            }
        }
        ItemTouchHelper(swipe).attachToRecyclerView(recyclerView)

        viewModel.get_All().subscribeOn(Schedulers.io()).delay(0, TimeUnit.SECONDS)
            .observeOn(Schedulers.newThread()).subscribe {
            this.runOnUiThread {
                recyclerAdaptor = CommonAdaptor<link>(
                    R.layout.single_row_home,
                    it,
                    { it, card -> bindItem(it, card) },
                    true,
                    filter()
                )
                recyclerView.adapter = recyclerAdaptor
                copy = it
            }
        }





        add.setOnClickListener {
            popupView.show()
            show("")
        }
    }

    private fun bindItem(item: link, itemView: View) {
        val title = itemView.findViewById<TextView>(R.id.TitleFetched)
        val link = itemView.findViewById<TextView>(R.id.LinkFethced)
        val id = itemView.findViewById<TextView>(R.id.idSetter)
        val favourite = itemView.findViewById<ImageView>(R.id.favImage)
        val share=itemView.findViewById<ImageView>(R.id.shareImage)
        share.setOnClickListener {
            val sendIntent: Intent = Intent().apply { action = Intent.ACTION_SEND;putExtra(Intent.EXTRA_TEXT, item.link);type = "text/plain" }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
        this.runOnUiThread {
            id.text = item.id.toString()
            title.text = item.title
            link.text = item.link
            if (!item.favourite) favourite.setImageResource(R.drawable.ic_baseline_favorite_border_24) else favourite.setImageResource(
                R.drawable.ic_baseline_favorite_24
            )

            favourite.setOnClickListener {
                viewModel.UpdateFavItem(item.id, !item.favourite)
            }
            link.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.link))
                link.scrollTo(0, 0)
                startActivity(intent)
            }
        }
    }

    private fun show(stringExtra: String) {
        this.runOnUiThread {
            popupView.show()
            val close = popupView.findViewById<Button>(R.id.close)
            val save = popupView.findViewById<Button>(R.id.save)
            val title = popupView.findViewById<EditText>(R.id.titleEntry)
            val linkEntry = popupView.findViewById<EditText>(R.id.linkEntry)
            println("$stringExtra jjnj")
            if (stringExtra.isNotEmpty())
                linkEntry.setText(stringExtra)

            save.setOnClickListener {
                if (Patterns.WEB_URL.matcher(linkEntry.text.toString())
                        .matches() && URLUtil.isValidUrl(linkEntry.text.toString()) && title.text.toString()
                        .isNotEmpty()
                ) {
                    val insert = link()
                    insert.title = title.text.toString();insert.link =
                        linkEntry.text.toString();insert.date =
                        Calendar.getInstance().timeInMillis;insert.favourite = false
                    viewModel.saveLinkEntry(insert)
                    linkEntry.setText("");title.setText("")
                    popupView.dismiss()
                } else {
                    Toast.makeText(this, "link and title is required", Toast.LENGTH_SHORT).show()
                }
            }
            close.setOnClickListener { popupView.dismiss() }
        }
    }

    private fun setpopup() {
        popupView = Dialog(this)
        popupView.setContentView(R.layout.pop_layout)
        popupView.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
   //option menu and filter
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        val searchItem = menu?.findItem(R.id.search)
        val searchView = searchItem!!.actionView as SearchView
        searchView.imeOptions = EditorInfo.IME_ACTION_SEARCH
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                recyclerAdaptor.filter.filter(query)
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                recyclerAdaptor.filter.filter(newText)
                return false
            }

        })

        return super.onCreateOptionsMenu(menu)
    }
   //filter Class
    private inner class filter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = arrayListOf<link>()
            println("lo $constraint")
            val results = FilterResults()
            if (constraint!!.isEmpty()) {
                filteredList.addAll(copy)
            } else {
                val filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim()
                for (mWords in copy) {
                    if (mWords.title.toLowerCase(Locale.ROOT).startsWith(filterPattern)) {
                        filteredList.add(mWords)
                    }
                }
            }
            results.values = filteredList
            results.count = filteredList.size
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            recyclerAdaptor.item = results!!.values as List<link>
            recyclerAdaptor.notifyDataSetChanged()
        }

    }
   //menu option select
    @SuppressLint("CheckResult")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.fav -> {
                if (!item.isChecked) {
                    viewModel.getAllFavuorite(true).observeOn(Schedulers.io())
                        .observeOn(Schedulers.newThread()).subscribe {
                        this.runOnUiThread {
                            recyclerAdaptor.item = it;recyclerAdaptor.notifyDataSetChanged()
                        }
                    }
                    item.isChecked = true
                } else {
                    viewModel.get_All().observeOn(Schedulers.io()).observeOn(Schedulers.newThread())
                        .subscribe {
                            this.runOnUiThread {
                                recyclerAdaptor.item = it;recyclerAdaptor.notifyDataSetChanged()
                            }
                        };item.isChecked = false
                }
            }
            R.id.info->{
                val intent=Intent(this,InfoActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val datemenu=menu.findItem(R.id.date)
        val favc=menu.findItem(R.id.fav)
        if(favc.isChecked){
            datemenu.isEnabled=false
        }
        if(!favc.isChecked){
            datemenu.isEnabled=true
        }
        datemenu.setOnMenuItemClickListener {
            this.runOnUiThread {
            if(!datemenu.isChecked){
                    Collections.sort(copy, date)
                    recyclerAdaptor.item= copy.asReversed(); recyclerAdaptor.notifyDataSetChanged()
                    datemenu.isChecked=true
                    Toast.makeText(this, "Oldest", Toast.LENGTH_SHORT).show()
            }
            else{
                    Collections.sort(copy, date1)
                    recyclerAdaptor.item= copy.asReversed();recyclerAdaptor.notifyDataSetChanged()
                    datemenu.isChecked=false
                    Toast.makeText(this, "Newest", Toast.LENGTH_SHORT).show()
                }
        }
            return@setOnMenuItemClickListener true
        }

        return super.onPrepareOptionsMenu(menu)
    }

    }









