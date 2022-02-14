package com.example.directoryofnames

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

private const val TAG = "MAIN_VIEW_MODEL"

class MainViewModel(private val repository: WordsRepository.Companion) : ViewModel() {

    private val wordsLiveMutable = MutableLiveData<List<User>>()
    val wordsLive: LiveData<List<User>> = wordsLiveMutable

    private val filterLiveMutable = MutableLiveData<String>()
    val filterLive: LiveData<String> = filterLiveMutable

    private val filterSubject = PublishSubject.create<String>()

    init {
        Log.d(TAG, "VM created")
        filterSubject
            .debounce(300, TimeUnit.MILLISECONDS, Schedulers.io())
            .observeOn(Schedulers.computation())
            .subscribe {
                wordsLiveMutable.postValue(repository.filterRepository(it))

            }
        if (BuildConfig.DEBUG) {
            wordsLiveMutable.value = listOf(User("Start to search names"))

        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "VM cleared")
    }

    fun filter(text: String) {
        Log.d(TAG, "VM filter")

        filterSubject.onNext(text)

    }


}