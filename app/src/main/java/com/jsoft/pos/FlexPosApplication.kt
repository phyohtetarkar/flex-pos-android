package com.jsoft.pos

import android.app.Application
import android.arch.persistence.room.Room
import com.jsoft.pos.data.PosDatabase

class FlexPosApplication : Application() {

    lateinit var db: PosDatabase
        private set

    override fun onCreate() {
        super.onCreate()

        db = Room.databaseBuilder(this, PosDatabase::class.java, PosDatabase.DB_NAME)
                .build()


    }

}
