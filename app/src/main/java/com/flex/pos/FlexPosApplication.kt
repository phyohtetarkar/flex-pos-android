package com.flex.pos

import android.app.Application
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.migration.Migration
import android.content.Context
import com.flex.pos.data.PosDatabase
import com.flex.pos.ui.utils.ContextWrapperUtil

class FlexPosApplication : Application() {

    lateinit var db: PosDatabase
        private set

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(ContextWrapperUtil.create(base))
    }

    override fun onCreate() {
        super.onCreate()

        loadDatabase()
    }

    fun loadDatabase() {
        db = Room.databaseBuilder(this, PosDatabase::class.java, PosDatabase.DB_NAME)
                .build()
    }

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
            }
        }
    }

}
