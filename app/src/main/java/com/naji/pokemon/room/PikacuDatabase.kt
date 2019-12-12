package com.naji.pokemon.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.naji.pokemon.api.Pokemon
import com.naji.pokemon.model.PikacuDao

@Database(
        entities = [Pokemon::class],
        version = 1,
        exportSchema = false
)
abstract class PikacuDatabase : RoomDatabase() {

    abstract fun pokemonDao(): PikacuDao

    companion object {
        @Volatile
        private var INSTANCE: PikacuDatabase? = null

        /**
         * Create an unique instance of the DB.
         *
         * @param context application's context
         * @return a database instance
         */
        fun getInstance(context: Context): PikacuDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE
                        ?: buildDatabase(context).also { INSTANCE = it }
                }

        /**
         * Helper to build the PikacuDatabase object
         *
         * @param context application's context
         * @return a database object
         */
        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(
                        context.applicationContext,
                        PikacuDatabase::class.java,
                        "PokemonDB.db"
                ).build()
    }
}