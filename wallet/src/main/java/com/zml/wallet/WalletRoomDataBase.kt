package com.zml.wallet

import android.content.Context
import androidx.annotation.NonNull
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.zml.wallet.dao.TokenDao
import com.zml.wallet.dao.WalletDao
import com.zml.wallet.entity.TokenEntity
import com.zml.wallet.entity.WalletEntity


@Database(entities = [WalletEntity::class,TokenEntity::class], version = 2)
abstract class WalletRoomDataBase:RoomDatabase() {

    abstract fun walletDao():WalletDao

    abstract fun tokenDao():TokenDao


    companion object {
        private const val TAG = "WalletRoomDataBase"
        //DATABASE_NAME名称可以叫simple_app或simple_app.db，正常来说应该叫        //simple_app.db，但是名称叫simple_app也没问题
        const val DATABASE_NAME = "wallet_u.db"


        private val MIGRATION_1_2 = object :Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
                //将数据表device创建出来
                database.execSQL("alert table wallet_entity chainName text null chainID integer null")

            }
        }


        @Volatile
        private var databaseInstance: WalletRoomDataBase? = null

        @Synchronized
        @JvmStatic
        fun get(context:Context): WalletRoomDataBase {
            if (databaseInstance == null) {

                databaseInstance = Room.databaseBuilder(
                    context,
                    WalletRoomDataBase::class.java,
                    DATABASE_NAME
                )
                    .allowMainThreadQueries()//允许在主线程操作数据库，一般不推荐；设置这个后主线程调用增删改查不会报错，否则会报错
                //    .addMigrations(MIGRATION_1_2)//指定版本1-2升级时的升级策略
//                    .openHelperFactory(factory)
                    .build()
            }
            return databaseInstance!!
        }

    }
}

