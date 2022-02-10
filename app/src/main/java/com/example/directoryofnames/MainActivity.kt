package com.example.directoryofnames

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var rvUsers: RecyclerView
    private lateinit var searchWindow: EditText
    private lateinit var vm: MainViewModel
    private lateinit var adapter: UsersAdapter


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rvUsers = findViewById(R.id.recycler_view)
        searchWindow = findViewById(R.id.search_bar)
        rvUsers.layoutManager = LinearLayoutManager(this)

        vm = ViewModelProvider(this)[MainViewModel::class.java]


        PublishSubject.create<CharSequence> { emitter ->
            val searchWatcher = object : TextWatcher {
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
                vm.filter(it)
            }
    }

    override fun onStart() {
        super.onStart()
        adapter = UsersAdapter()
        rvUsers.adapter = adapter

        vm.wordsLive.observe(this, {list ->
            adapter.submitList(list)
        })
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindTo(user: User) {
            val nameTextView: TextView = itemView.findViewById(R.id.text_item)
            nameTextView.text = user.name
        }
    }

    class UserItemDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }

    class UsersAdapter : ListAdapter<User, UserViewHolder>
        (UserItemDiffCallback()) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
            return UserViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)
            )
        }

        override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
            holder.bindTo(getItem(position))
        }
    }
}