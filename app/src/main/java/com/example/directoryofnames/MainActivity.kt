package com.example.directoryofnames

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

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

        vm = ViewModelProvider(this, MainViewModelFactory())
            .get(MainViewModel::class.java)

        val searchObserver = Observer<String> { search ->
            searchWindow.setText(search)
        }

        vm.filterLive.observe(this, searchObserver)

        searchWindow.addTextChangedListener { vm.filter(it.toString()) }

        adapter = UsersAdapter()
        rvUsers.adapter = adapter

        vm.wordsLive.observe(this, { list ->
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