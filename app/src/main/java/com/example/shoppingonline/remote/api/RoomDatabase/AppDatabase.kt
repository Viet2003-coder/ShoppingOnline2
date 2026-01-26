package com.example.shoppingonline.remote.api.RoomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.shoppingonline.Model.CartItem
import com.example.shoppingonline.Model.Oder
import com.example.shoppingonline.Model.Product

@Database(
    entities = [Product::class, CartItem::class, Oder::class],
    version = 9,
            exportSchema = false

)
@TypeConverters(OderCoverters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun oderDao(): OderDao
    companion object{
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "shopping.db"
                ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
        }
    }
}