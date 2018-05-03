package com.jsoft.es

import android.app.Application
import android.arch.persistence.room.Room

import com.jsoft.es.data.PosDatabase

class EasyShopApplication : Application() {

    lateinit var db: PosDatabase
        private set

    override fun onCreate() {
        super.onCreate()

        db = Room.databaseBuilder(this, PosDatabase::class.java, PosDatabase.DB_NAME)
                .build()
    }

}
