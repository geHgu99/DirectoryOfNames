package com.example.directoryofnames

import com.example.directoryofnames.StaticData.names

class WordsRepository {

    companion object {

        fun filterRepository(filter: String): List<User> {
            val userList = mutableListOf<User>()
            for (name in names) {
                if (filter in name) {
                    userList.add(User(name))
                }
            }

            Thread.sleep(1000)
            return userList
        }
    }
}