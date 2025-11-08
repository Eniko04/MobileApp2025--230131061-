package com.example.movieapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Главната база данни на приложението.
 * Тук дефинираме всички ентитети (таблици) и достъпът до тях (DAO).
 */
@Database(entities = [Movie::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // DAO интерфейсът, който ще използваме за работа с филмите
    abstract fun movieDao(): MovieDao

    companion object {
        // Използваме Singleton, за да има само една инстанция на базата в приложението
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Метод за достъп до базата данни.
         * Ако базата още не е създадена, тя се инициализира тук.
         */
        fun getDatabase(context: Context): AppDatabase {
            // Проверяваме дали вече имаме създадена база
            return INSTANCE ?: synchronized(this) {
                // Ако не, създаваме нова с помощта на Room
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "movie_database" // име на файла, в който се съхранява базата
                )
                    // Ако структурата на таблиците се промени — старата база ще се изтрие и пресъздаде
                    .fallbackToDestructiveMigration()
                    .build()

                // Записваме инстанцията за бъдещи извиквания
                INSTANCE = instance
                instance
            }
        }
    }
}
