package com.example.directoryofnames

import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class MainViewModelTest {

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        viewModel = MainViewModel(WordsRepository)
    }

    @Test
    fun getWordsLive() {
    }

    @Test
    fun filter() {
        viewModel.filter("")
        val expected = listOf<User>(
            User("Wallace"),
            User("Walter"),
            User("William"),
            User("Winifred"))
        assertEquals(expected, viewModel.wordsLive)
    }
}