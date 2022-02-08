package com.example.directoryofnames

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var searchWindow: EditText
    private var adapter: SearchAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        searchRecyclerView = findViewById(R.id.recycler_view)
        searchWindow = findViewById(R.id.search_bar)
        searchRecyclerView.layoutManager = LinearLayoutManager(this)

         Observable.create<CharSequence> { emitter ->
            val searchWatcher = object  : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        if (p0 != null) {
                            emitter.onNext(p0)
                        }
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            }

            searchWindow.addTextChangedListener(searchWatcher)

        }
             .debounce(1500, TimeUnit.MILLISECONDS)
             .subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
             .subscribe {
                 updateUI(it)
             }
    }

    override fun onStart() {
        super.onStart()
        updateUI()
    }

    private inner class SearchViewHolder(view: View)
        : RecyclerView.ViewHolder(view) {
            val nameTextView: TextView = itemView.findViewById(R.id.text_item)
        }

    private inner class SearchAdapter(var names: List<String>)
        : RecyclerView.Adapter<SearchViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
        : SearchViewHolder {
            val view = layoutInflater.inflate(R.layout.recyclerview_item, parent, false)
            return SearchViewHolder(view)
        }

        override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
            val name = names[position]
            holder.apply {
                nameTextView.text = name
            }
        }

        override fun getItemCount() = names.size

    }

    private fun updateUI(filter: CharSequence = "") {
        val names = Words.names.filter { name -> filter in name }
        adapter = SearchAdapter(names)
        searchRecyclerView.adapter = adapter
    }
}