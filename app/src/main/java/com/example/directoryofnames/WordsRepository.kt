package com.example.directoryofnames

import com.example.directoryofnames.StaticData.names

object WordsRepository {

    fun filterRepository(filter: CharSequence): List<User> {
        val userList = mutableListOf<User>()
        for (name in names) {
            if (filter in name) {
                userList.add(User(name))
            }
        }

        Thread.sleep(3000)
        return userList
    }
}