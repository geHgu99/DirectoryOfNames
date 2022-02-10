package com.example.directoryofnames

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

private const val TAG = "MAIN_VIEW_MODEL"

class MainViewModel : ViewModel(){

    private val wordsLiveMutable = MutableLiveData<List<User>>()
    val wordsLive: LiveData<List<User>> = wordsLiveMutable

    init {
        Log.d(TAG, "VM created")
        if (BuildConfig.DEBUG) {
            wordsLiveMutable.value = listOf(User("Start to search names"))

        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "VM cleared")
    }

    fun filter(text: CharSequence) {
        Log.d(TAG, "VM filter")

        PublishSubject.just(WordsRepository.names)
            .map { names ->
                val userList = mutableListOf<User>()
                for (name in names) {
                    if (text in name) {
                        userList.add(User(name))
                    }
                }
                userList
            }
            .debounce(1000, TimeUnit.MILLISECONDS, Schedulers.computation())
            .observeOn((AndroidSchedulers.mainThread()))
            .subscribe {
                wordsLiveMutable.value = it
            }

    }


}