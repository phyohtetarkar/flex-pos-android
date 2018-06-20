package com.jsoft.pos

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import com.jsoft.pos.data.PosDatabase
import com.jsoft.pos.ui.utils.ContextWrapperUtil

class FlexPosApplication : Application() {

    private var db: PosDatabase? = null

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(ContextWrapperUtil.create(base))
    }

    override fun onCreate() {
        super.onCreate()

        initDb()
    }

    fun closeDb() {
        db?.close()
        db = null
    }

    fun initDb() {
        db = Room.databaseBuilder(this, PosDatabase::class.java, PosDatabase.DB_NAME)
                .build()
    }

}
